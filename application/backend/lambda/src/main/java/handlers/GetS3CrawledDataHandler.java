package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.config.LambdaConfigurationConstants;
import models.exceptions.BadRequestException;
import models.workflow.CrawlMetadata;
import models.workflow.Workflow;
import utils.*;
import utils.aws.s3.S3ClientHelper;
import utils.aws.s3.S3OperationsHelper;
import validators.GetS3CrawledDataInputValidator;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class GetS3CrawledDataHandler implements
        RequestHandler<GetS3CrawledDataHandler.Input, GetS3CrawledDataHandler.Output>,
        WrappableRequestHandler<GetS3CrawledDataHandler.Input, GetS3CrawledDataHandler.Output>  {

    private Logger LOG;
    private S3OperationsHelper s3OperationsHelper;
    private GetS3CrawledDataInputValidator inputValidator;
    private LambdaConfigurationConstants config;

    public GetS3CrawledDataHandler.Output handleRequest(final GetS3CrawledDataHandler.Input input, final Context context) {
        final ExceptionWrapper<Input, Output> exceptionWrapper = new ExceptionWrapper<>(input, context);
        return exceptionWrapper.doHandleRequest(this);
    }

    @Override
    public Output doHandleRequest(final Input input, final Context context) {
        initializeInstance(context);
        LOG.info("Input=%s", input.toString());

        inputValidator.validate(input);

        try {
            final URI uri = new URI(input.getPageUrl());
            String crawlDataPath = uri.getHost() + uri.getPath();
            crawlDataPath = crawlDataPath.replace("//", "/");

            final String s3CSSCrawlDataKey = SurfObjectMother.getS3CrawlDataKey(
                    SurfObjectMother.getOwnerId(input.getUserArn()),
                    input.getWorkflowExecutionId(),
                    crawlDataPath,
                    CrawlMetadata.CategoryName.CSSSelectors.getName());
            LOG.info("S3 CSS data key='%s'", s3CSSCrawlDataKey);

            final String s3TextCrawlDataKey = SurfObjectMother.getS3CrawlDataKey(
                    SurfObjectMother.getOwnerId(input.getUserArn()),
                    input.getWorkflowExecutionId(),
                    crawlDataPath,
                    CrawlMetadata.CategoryName.TextSelectors.getName());
            LOG.info("S3 Text data key='%s'", s3TextCrawlDataKey);

            final URL cssPresignedUrl = s3OperationsHelper.generateCrawledDataPresignedUrl(
                    config.getApplicationS3BucketName(),
                    s3CSSCrawlDataKey);
            final URL textPresignedUrl = s3OperationsHelper.generateCrawledDataPresignedUrl(
                    config.getApplicationS3BucketName(),
                    s3TextCrawlDataKey);

            final Output output = new Output();
            output.setS3CSSUrl(cssPresignedUrl.toString());
            output.setS3TextUrl(textPresignedUrl.toString());
            return output;

        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new BadRequestException("The supplied 'pageUrl' could not be parsed as an URI!");
        }
    }

    private void initializeInstance(final Context context) {
        LOG = new Logger(context.getLogger());

        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        final AmazonS3 s3Client = new S3ClientHelper(context.getLogger()).getS3Client(config);
        s3OperationsHelper = new S3OperationsHelper(s3Client, LOG, config);
        inputValidator = new GetS3CrawledDataInputValidator(context);
    }

    public static class Input {
        private String userArn;
        private String workflowExecutionId;
        private String pageUrl;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public String getWorkflowExecutionId() {
            return workflowExecutionId;
        }

        public void setWorkflowExecutionId(String workflowExecutionId) {
            this.workflowExecutionId = workflowExecutionId;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public void validate() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(userArn), "The userArn cannot be null or empty!");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(workflowExecutionId), "The workflowExecutionId cannot be null or empty!");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(pageUrl), "The pageUrl cannot be null or empty!");
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflowExecutionId='" + workflowExecutionId + '\'' +
                    ", pageUrl='" + pageUrl + '\'' +
                    '}';
        }
    }

    public static class Output {
        private String s3CSSUrl;
        private String s3TextUrl;

        public String getS3CSSUrl() {
            return s3CSSUrl;
        }

        public void setS3CSSUrl(String s3CSSUrl) {
            this.s3CSSUrl = s3CSSUrl;
        }

        public String getS3TextUrl() {
            return s3TextUrl;
        }

        public void setS3TextUrl(String s3TextUrl) {
            this.s3TextUrl = s3TextUrl;
        }

        @Override
        public String toString() {
            return "Output{" +
                    "s3CSSUrl='" + s3CSSUrl + '\'' +
                    ", s3TextUrl='" + s3TextUrl + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Output output = (Output) o;
            return Objects.equals(s3CSSUrl, output.s3CSSUrl) &&
                    Objects.equals(s3TextUrl, output.s3TextUrl);
        }

        @Override
        public int hashCode() {
            return Objects.hash(s3CSSUrl, s3TextUrl);
        }
    }
}
