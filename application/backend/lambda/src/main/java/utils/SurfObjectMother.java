package utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import handlers.InitializeCrawlSessionHandler;
import models.workflow.*;

import javax.annotation.Nonnull;

public class SurfObjectMother {

    public static final String S3_CRAWL_DATA_ROOT_FOLDER_NAME = "crawl-data";

    private SurfObjectMother() {
        throw new UnsupportedOperationException("This class is not instantiable. Use the static methods instead.");
    }

    public static WorkflowExecution createWorkflowExecution(
            @Nonnull final Workflow workflow,
            @Nonnull final String userArn) {
        Preconditions.checkNotNull(workflow, "The workflow execution cannot be created from a null workflow!");
        Preconditions.checkNotNull(userArn, "The userArn must not be null in order to build ownerId with authProvider!");

        final WorkflowExecution workflowExecution = new WorkflowExecution();
        workflowExecution.setId(RandomGenerator.randomUUIDWithTimestamp());
        workflowExecution.setCreationDateMillis(System.currentTimeMillis());
        workflowExecution.setWorkflowId(workflow.getId());
        workflowExecution.setOwnerId(getOwnerId(userArn));
        workflowExecution.setStatus(Status.Pending);

        return workflowExecution;
    }

    public static String getOwnerId(@Nonnull final String userArn) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(userArn),
                "The userArn must not be null or empty in order to build ownerId with authProvider!"
        );

        return String.format(
                "urn:%s@%s",
                ArnHelper.getOwnerIdFromUserArn(userArn),
                ArnHelper.getAuthProviderFromUserArn(userArn));
    }

    public static InitializeCrawlSessionHandler.Input generateInitializeCrawlSessionInput(
            @Nonnull final String workflowExecutionId,
            int currentRecursionDepth) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "The 'workflowExecutionId' cannot be null or empty when trying to generate 'InitializeCrawlSessionHandler.Input'!"
        );
        Preconditions.checkArgument(
                currentRecursionDepth >= 0,
                "The current recursion depth must be a positive (>=0) integer!"
        );

        final InitializeCrawlSessionHandler.Input input = new InitializeCrawlSessionHandler.Input();
        input.setWorkflowExecutionId(workflowExecutionId);
        input.setCurrentDepthLevel(currentRecursionDepth);

        return input;
    }

    public static WorkflowTask createWorkflowTask(
            @Nonnull final String workflowExecutionId,
            @Nonnull final String ownerId,
            @Nonnull final long maxWebPageSizeBytes,
            @Nonnull final SelectionPolicy selectionPolicy,
            @Nonnull final String urlMatcher,
            @Nonnull final String urlToVisit,
            final int taskDepthLevel) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "The 'workflowExecutionId' cannot be null or empty when trying to create a new WorkflowTask!"
        );
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(ownerId),
                "The 'ownerId' cannot be null or empty when trying to create a new WorkflowTask!"
        );
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(urlToVisit),
                "The 'urlToVisit' cannot be null or empty when trying to create a new WorkflowTask!"
        );
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(urlMatcher),
                "The 'urlMatcher' cannot be null or empty when trying to create a new WorkflowTask!"
        );
        Preconditions.checkArgument(
                taskDepthLevel >= 0,
                "The 'taskDepthLevel' must be an integer >= 0 when trying to create a new WorkflowTask!"
        );
        Preconditions.checkArgument(
                maxWebPageSizeBytes > 0,
                "The 'maxWebPageSizeBytes' must be an integer >= 0 when trying to create a new WorkflowTask!"
        );
        Preconditions.checkNotNull(
                selectionPolicy,
                "The 'selectionPolicy' must be non-null when trying to create a new WorkflowTask!"
        );

        final WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setId(RandomGenerator.randomUUIDWithTimestamp());
        workflowTask.setCreationDateMillis(System.currentTimeMillis());
        workflowTask.setWorkflowExecutionId(workflowExecutionId);
        workflowTask.setOwnerId(ownerId);
        workflowTask.setStatus(Status.Pending);
        workflowTask.setDepth(taskDepthLevel);
        workflowTask.setExecutionFailures(null);
        workflowTask.setMaxWebPageSizeBytes(maxWebPageSizeBytes);
        workflowTask.setSelectionPolicy(selectionPolicy);
        workflowTask.setUrlMatcher(urlMatcher);
        workflowTask.setUrl(urlToVisit);

        return workflowTask;
    }

    public static WorkflowExecutionFailure getWorkflowExecutionFailure(
            @Nonnull final CrawlWebPageError error) {
        Preconditions.checkNotNull(error);

        final WorkflowExecutionFailure failure = new WorkflowExecutionFailure();
        failure.setError(error.getError());
        failure.setCause(error.getCause());
        failure.setTimeStamp(System.currentTimeMillis());
        return failure;
    }

    public static VisitedPage createVisitedPage(
            @Nonnull final String workflowExecutionId,
            @Nonnull final String url,
            final long pageVisitDepth) {
        Preconditions.checkNotNull(workflowExecutionId);
        Preconditions.checkNotNull(url);
        Preconditions.checkArgument(pageVisitDepth >= 0);

        final VisitedPage visitedPage = new VisitedPage();
        visitedPage.setWorkflowExecutionId(workflowExecutionId);
        visitedPage.setPageVisitDepth(pageVisitDepth);
        visitedPage.setUrl(url);
        return visitedPage;
    }

    public static PageToBeVisited createPageToBeVisited(
            @Nonnull final String workflowExecutionId,
            @Nonnull final String url) {
        Preconditions.checkNotNull(workflowExecutionId);
        Preconditions.checkNotNull(url);

        final PageToBeVisited pageToBeVisited = new PageToBeVisited();
        pageToBeVisited.setWorkflowExecutionId(workflowExecutionId);
        pageToBeVisited.setUrl(url);
        return pageToBeVisited;
    }

    public static String getS3CrawlDataKey(
            @Nonnull final String ownerId,
            @Nonnull final String workflowExecutionId,
            @Nonnull final String url,
            final String category) {
        Preconditions.checkNotNull(ownerId);
        Preconditions.checkNotNull(workflowExecutionId);
        Preconditions.checkNotNull(url);

        if (category == null || category.isEmpty()) {
            return String.format(
                    "%s/%s/%s/%s", S3_CRAWL_DATA_ROOT_FOLDER_NAME, ownerId, workflowExecutionId, url);
        } else {
            return String.format(
                    "%s/%s/%s/%s/%s", S3_CRAWL_DATA_ROOT_FOLDER_NAME, ownerId, workflowExecutionId, url, category);
        }
    }

    public static CrawlMetadata createCrawlMetadata(
            @Nonnull final String workflowExecutionId,
            @Nonnull final String workflowTaskId,
            @Nonnull final String bucketName,
            @Nonnull final String objectKey) {
        Preconditions.checkNotNull(workflowExecutionId);
        Preconditions.checkNotNull(workflowTaskId);
        Preconditions.checkNotNull(bucketName);
        Preconditions.checkNotNull(objectKey);

        final CrawlMetadata metadata = new CrawlMetadata();
        metadata.setWorkflowExecutionId(workflowExecutionId);
        metadata.setWorkflowTaskId(workflowTaskId);
        metadata.setId(RandomGenerator.randomUUIDWithTimestamp());
        metadata.setCreationDateMillis(System.currentTimeMillis());
        metadata.setS3Link(String.format("https://%s.s3.amazonaws.com/%s", bucketName, objectKey));

        return metadata;
    }
}
