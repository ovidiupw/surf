package models.workflow;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.google.common.base.Preconditions;
import com.panforge.robotstxt.RobotsTxt;
import models.config.LambdaConfigurationConstants;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import utils.CrawlDataValidator;
import utils.Logger;
import utils.SurfObjectMother;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import utils.aws.s3.S3ClientHelper;
import utils.aws.s3.S3OperationsHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Crawler {

    public static final int CRAWL_TIMEOUT_SAFETY_MARGIN_SECONDS = 10;
    public static final String CRAWL_DATA_SEPARATOR = "-{{#}}-";

    private final DynamoDBOperationsHelper dynamoOperationsHelper;
    private final LambdaConfigurationConstants config;
    private final S3OperationsHelper s3OperationsHelper;
    private final Logger LOG;
    private final Context context;
    private long crawlBeginTimeMillis;

    public Crawler(
            @Nonnull final LambdaConfigurationConstants config,
            @Nonnull final Context context) {
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(context);

        this.LOG = new Logger(context.getLogger());

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(LOG).getDynamoDBClient(config);
        this.dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, LOG);

        final AmazonS3 s3Client = new S3ClientHelper(context.getLogger()).getS3Client(config);
        this.s3OperationsHelper = new S3OperationsHelper(s3Client, LOG, config);

        this.config = config;
        this.context = context;
    }

    public CrawlWebPageStateOutput crawl(@Nonnull final CrawlWebPageStateInput input) {
        Preconditions.checkNotNull(input);
        LOG.info("Crawler is configured with a timeout safety margin of '%d' seconds",
                CRAWL_TIMEOUT_SAFETY_MARGIN_SECONDS);

        this.crawlBeginTimeMillis = System.currentTimeMillis();

        final WorkflowTask workflowTask = dynamoOperationsHelper.getWorkflowTask(input.getWorkflowTaskId());

        try {
            changeCrawlTaskStatus(workflowTask, Status.Active);
            doCrawl(input, workflowTask);
            changeCrawlTaskStatus(workflowTask, Status.Completed);
            return getCrawlOutput(input);

        } catch (CrawlingTimeExceeded e) {
            changeCrawlTaskStatus(workflowTask, Status.TimedOut);
            final CrawlWebPageError error = getCrawlWebPageError(e);
            return getCrawlOutput(input, error);
        } catch (IOException | URISyntaxException | RuntimeException e) {
            changeCrawlTaskStatus(workflowTask, Status.Failed);
            final CrawlWebPageError error = getCrawlWebPageError(e);
            return getCrawlOutput(input, error);
        } finally {
            finalizeCrawl(input);
        }
    }

    private CrawlWebPageError getCrawlWebPageError(Exception e) {
        final CrawlWebPageError.Cause errorCause = new CrawlWebPageError.Cause();
        errorCause.setErrorMessage(e.getMessage());
        errorCause.setErrorType(e.getClass().getName());
        errorCause.setStackTrace(ExceptionUtils.getStackTrace(e));

        final CrawlWebPageError error = new CrawlWebPageError();
        error.setError(e.getClass().getName());
        error.setCause(errorCause);
        return error;
    }

    private void doCrawl(
            @Nonnull final CrawlWebPageStateInput input,
            @Nonnull final WorkflowTask workflowTask) throws IOException, URISyntaxException {
        final URI uriToCrawl = new URI(input.getUrl());

        if (!hasRobotAccess(uriToCrawl)) {
            throw new UnsupportedOperationException(
                    String.format("RobotsTxtAccessDeniedUri:'%s':TaskId:'%s'", uriToCrawl, input.getWorkflowTaskId()));
        }

        final Document crawledDocument = Jsoup.connect(uriToCrawl.toString()).get();

        final String cssSelectorsData = getCSSSelectorsData(input, crawledDocument);
        saveCrawlData(input, workflowTask, cssSelectorsData, CrawlMetadata.CategoryName.CSSSelectors);

        final String textSelectorsData = getTextSelectorsData(input, crawledDocument);
        saveCrawlData(input, workflowTask, textSelectorsData, CrawlMetadata.CategoryName.TextSelectors);

        try {
            // Do not generate crawling tasks if this is the final crawling depth level
            if (input.getDepthLevel() < input.getMaxDepthLevel()) {
                generateCrawlTasks(input, crawledDocument);
            } else {
                LOG.warn("Will not generate additional crawling tasks because the next crawling depth level='%s' " +
                        "exceeds the maximum depth level='%s'", input.getDepthLevel(), input.getMaxDepthLevel());
            }
        } catch (PageVisitLimitExceededException ignored) {
        }
    }

    private String getCSSSelectorsData(@Nonnull CrawlWebPageStateInput input, Document crawledDocument) {
        StringBuilder stringBuilder = new StringBuilder();
        for (final CSSSelector cssSelector : input.getSelectionPolicy().getCssSelectors()) {
            try {
                final Elements select = crawledDocument.select(cssSelector.getCssSelector());
                stringBuilder.append(select).append(CRAWL_DATA_SEPARATOR);
            } catch (Selector.SelectorParseException e) {
                LOG.error("Failed to select information using cssSelector='%s'", cssSelector.getCssSelector());
            }
        }
        return stringBuilder.toString();
    }

    private String getTextSelectorsData(@Nonnull CrawlWebPageStateInput input, Document crawledDocument) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String rawCrawledDocument = crawledDocument.text();
        for (final TextSelector textSelector : input.getSelectionPolicy().getTextSelectors()) {
            try {
                final Pattern patter = Pattern.compile(textSelector.getTextMatcher());
                final Matcher matcher = patter.matcher(rawCrawledDocument);
                LOG.info("Trying to find textual matches using matcher='%s'", textSelector.getTextMatcher());
                while (matcher.find()) {
                    final String matchedString = matcher.group();
                    stringBuilder.append(matchedString).append(CRAWL_DATA_SEPARATOR);
                    LOG.info("Found textual match: '%s'", matchedString);
                }
            } catch (PatternSyntaxException e) {
                LOG.error("Pattern syntax for text matcher's regexp was invalid!");
            }
        }
        return stringBuilder.toString();
    }

    private void saveCrawlData(
            @Nonnull final CrawlWebPageStateInput input,
            @Nonnull final WorkflowTask workflowTask,
            @Nonnull final String data,
            @Nonnull final CrawlMetadata.CategoryName category) {
        try {
            final URI uri = new URI(workflowTask.getUrl());
            String crawlDataPath = uri.getHost() + uri.getPath();
            crawlDataPath = crawlDataPath.replace("//", "/");

            final String s3CrawlDataKey = SurfObjectMother.getS3CrawlDataKey(
                    input.getOwnerId(),
                    input.getWorkflowExecutionId(),
                    crawlDataPath,
                    category.getName());
            LOG.info("S3 data key='%s'", s3CrawlDataKey);

            s3OperationsHelper.putData(
                    config.getApplicationS3BucketName(),
                    s3CrawlDataKey,
                    data);
            final CrawlMetadata crawlMetadata = SurfObjectMother.createCrawlMetadata(
                    workflowTask.getWorkflowExecutionId(),
                    config.getApplicationS3BucketName(),
                    s3CrawlDataKey);
            dynamoOperationsHelper.saveCrawlMetadata(crawlMetadata);

        } catch (SdkClientException e) {
            LOG.error("Failed to add selected information to S3!");
        } catch (URISyntaxException e) {
            LOG.error("Failed to obtain URI from url='%s'", workflowTask.getUrl());
        }
    }

    private void generateCrawlTasks(@Nonnull CrawlWebPageStateInput input, Document crawledDocument) throws IOException {
        final Elements links = crawledDocument.select("a[href]");

        for (final Element link : links) {
            long crawlingTimeElapsedSeconds = getCrawlingTimeElapsedSeconds();

            if (crawlingTimeElapsedSeconds > input.getCrawlerTimeoutSeconds() - CRAWL_TIMEOUT_SAFETY_MARGIN_SECONDS) {
                LOG.error(
                        "Crawler has been executing for '%s' seconds out of the timeout of '%s' seconds",
                        crawlingTimeElapsedSeconds,
                        input.getCrawlerTimeoutSeconds());
                LOG.error("Cannot generate any more crawling tasks because crawler would exceed the timeout seconds!");
                throw new CrawlingTimeExceeded(String.format("TaskTimeout:TaskId:'%s'", input.getWorkflowTaskId()));
            }

            final String linkURL = link.attr("abs:href");
            LOG.info("Identified link='%s' on crawled webpage.", linkURL);

            final List<VisitedPage> visitedPageList = dynamoOperationsHelper.listVisitedPages(
                    input.getWorkflowExecutionId(),
                    input.getDepthLevel());

            if (visitedPageList.size() > input.getMaxPagesPerDepthLevel()) {
                LOG.warn("The pages visited for depthLevel='%s' exceeded the maximum value!", input.getDepthLevel());
                LOG.warn("Will terminate this crawler's execution...");
                throw new PageVisitLimitExceededException();
            }

            if (!CrawlDataValidator.isValidUrl(linkURL)) {
                LOG.info("Link is not a valid url: '%s'! Skipping link.", linkURL);
                continue;
            }

            if (!linkURL.matches(input.getUrlMatcher())) {
                LOG.info("Link url='%s' does not match urlMatcher='%s'! Skipping link.", linkURL, input.getUrlMatcher());
                continue;
            }

            int linkWebPageSize = Jsoup.connect(linkURL).execute().bodyAsBytes().length;

            if (linkWebPageSize > input.getMaxWebPageSizeBytes()) {
                LOG.info("The webPage at url='%s' ('%s' bytes) exceeds the maximum allowed size of '%s' bytes.",
                        linkURL, linkWebPageSize, input.getMaxWebPageSizeBytes()
                );
                continue;
            }

            final PageToBeVisited pageToBeVisited
                    = dynamoOperationsHelper.getPageToBeVisited(input.getWorkflowExecutionId(), linkURL);
            if (pageToBeVisited != null) {
                LOG.warn("Link was already scheduled by another crawler to be visited! Skipping");
                continue;
            }

            final VisitedPage visitedPage
                    = dynamoOperationsHelper.getVisitedPage(input.getWorkflowExecutionId(), linkURL);
            if (visitedPage == null) {
                LOG.info("Link seems to have not been previously visited. Adding it to pages to be visited.");
                final WorkflowTask workflowTask = SurfObjectMother.createWorkflowTask(
                        input.getWorkflowExecutionId(),
                        input.getOwnerId(),
                        input.getMaxWebPageSizeBytes(),
                        input.getSelectionPolicy(),
                        input.getUrlMatcher(),
                        linkURL,
                        (int) (input.getDepthLevel() + 1));

                dynamoOperationsHelper.saveWorkflowTask(workflowTask);

                try {
                    dynamoOperationsHelper.savePageToBeVisited(SurfObjectMother.createPageToBeVisited(
                            input.getWorkflowExecutionId(),
                            linkURL));
                } catch (ConditionalCheckFailedException e) {
                    LOG.error(e.getMessage());
                    LOG.error("Page to be visited was already added to database!");
                }
            } else {
                LOG.info("Link was already visited: '%s'!", linkURL);
            }
        }
    }

    private void finalizeCrawl(@Nonnull CrawlWebPageStateInput input) {
        VisitedPage visitedPage = dynamoOperationsHelper.getVisitedPage(
                input.getWorkflowExecutionId(),
                input.getUrl()
        );
        if (visitedPage == null) {
            visitedPage = SurfObjectMother.createVisitedPage(
                    input.getWorkflowExecutionId(),
                    input.getUrl(),
                    input.getDepthLevel());
            dynamoOperationsHelper.saveVisitedPage(visitedPage);
        } else {
            LOG.warn("Did not save visited page because it already existed in the database!");
        }

        final PageToBeVisited pageToBeVisited = dynamoOperationsHelper.getPageToBeVisited(
                input.getWorkflowExecutionId(),
                input.getUrl()
        );
        if (pageToBeVisited != null) {
            dynamoOperationsHelper.deletePageToBeVisited(pageToBeVisited);
        } else {
            LOG.warn("Did not delete pageToBeVisited because we found no such record for the url='%s'", input.getUrl());
        }
    }

    private long getCrawlingTimeElapsedSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.crawlBeginTimeMillis);
    }

    private boolean hasRobotAccess(@Nonnull final URI uriToCrawl) throws MalformedURLException {
        LOG.info("Trying to determine if crawler has access to uri='%s'", uriToCrawl);
        final List<String> urlSchemesAsList = CrawlDataValidator.getUrlSchemesAsList();

        for (final String urlScheme : urlSchemesAsList) {
            LOG.info("Trying to get robots.txt file using urlScheme='%s'", urlScheme);
            final String robotsTxtUrl = String.format("%s://%s/robots.txt", urlScheme, uriToCrawl.getHost());

            try (InputStream robotsTxtStream = new URL(robotsTxtUrl).openStream()) {
                LOG.info("Trying to read robots.txt file from url='%s'", robotsTxtUrl);
                final RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);

                LOG.info("robots.txt file read successfully!");
                LOG.info("Checking for access to uri='%s' with robots.txt...", uriToCrawl);
                boolean hasAccessToURI = robotsTxt.query(null, uriToCrawl.getPath());

                LOG.info("Access permitted to uri='%s' '%s'", uriToCrawl, hasAccessToURI);

                if (hasAccessToURI) {
                    return true;
                }
            } catch (IOException e) {
                LOG.info("Failed to read robots.txt file! Error was e='%s'", e.getMessage());
                LOG.info("Assuming that the crawler has access to the uri='%s'", uriToCrawl);
                return true;
            }
        }

        LOG.info("Could not obtain access to uri='%s' by checking with robots.txt", uriToCrawl);
        return false;
    }

    private void changeCrawlTaskStatus(@Nonnull final WorkflowTask task, @Nonnull final Status status) {
        LOG.info("Changing task status to '%s' for taskId='%s'", status.getName(), task.getId());
        long currentTimeMillis = System.currentTimeMillis();

        task.setStatus(status);
        switch (status) {
            case Active: {
                LOG.info("Changing task start date to '%s' for taskId='%s'", currentTimeMillis, task.getId());
                task.setStartDate(currentTimeMillis);
                break;
            }
            case Completed:
            case TimedOut:
            case Failed: {
                LOG.info("Changing task end date to '%s' for taskId='%s'", currentTimeMillis, task.getId());
                task.setEndDate(currentTimeMillis);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Case not covered!");
            }
        }

        LOG.info("Trying to update workflow taskId='%s'", task.getId());
        dynamoOperationsHelper.saveWorkflowTask(task);
        LOG.info("Successfully updated workflow task with taskId='%s'", task.getId());
    }

    private CrawlWebPageStateOutput getCrawlOutput(@Nonnull final CrawlWebPageStateInput input) {
        return getCrawlOutput(input, null);
    }

    private CrawlWebPageStateOutput getCrawlOutput(final CrawlWebPageStateInput input, final CrawlWebPageError error) {
        final CrawlWebPageStateOutput output = new CrawlWebPageStateOutput();
        output.setUrl(input.getUrl());
        output.setWorkflowTaskId(input.getWorkflowTaskId());
        output.setWorkflowExecutionId(input.getWorkflowExecutionId());
        output.setTaskDepthLevel(input.getDepthLevel());
        output.setError(error);

        LOG.info("Lambda execution time remaining before StepFunctions timeout: '%d'",
                input.getCrawlerTimeoutSeconds() - getCrawlingTimeElapsedSeconds());

        return output;
    }

    private class PageVisitLimitExceededException extends RuntimeException {
    }

    private class CrawlingTimeExceeded extends RuntimeException {
        public CrawlingTimeExceeded(String error) {
            super(error);
        }
    }
}
