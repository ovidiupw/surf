package utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import handlers.InitializeCrawlSessionHandler;
import models.workflow.*;

import javax.annotation.Nonnull;

public class SurfObjectMother {
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

        return String.join(
                "@",
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
            @Nonnull final WorkflowMetadata workflowMetadata,
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
        Preconditions.checkNotNull(
                workflowMetadata,
                "The 'workflowMetadata' cannot be null when trying to create a new WorkflowTask!"
        );
        Preconditions.checkArgument(
                taskDepthLevel >= 0,
                "The 'taskDepthLevel' must be an integer >= 0 when trying to create a new WorkflowTask!"
        );

        final WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setId(RandomGenerator.randomUUIDWithTimestamp());
        workflowTask.setCreationDateMillis(System.currentTimeMillis());
        workflowTask.setWorkflowExecutionId(workflowExecutionId);
        workflowTask.setOwnerId(ownerId);
        workflowTask.setStatus(Status.Pending);
        workflowTask.setDepth(taskDepthLevel);
        workflowTask.setFailures(null);
        workflowTask.setMaxWebPageSizeBytes(workflowMetadata.getMaxWebPageSizeBytes());
        workflowTask.setSelectionPolicy(workflowMetadata.getSelectionPolicy());
        workflowTask.setUrlMatcher(workflowMetadata.getUrlMatcher());
        workflowTask.setUrl(urlToVisit);

        return workflowTask;
    }
}
