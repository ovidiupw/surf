package utils;

import com.google.common.base.Preconditions;
import handlers.InitializeCrawlSessionHandler;
import models.Status;
import models.Workflow;
import models.WorkflowExecution;

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
        workflowExecution.setId(RandomGenerator.randomUUID());
        workflowExecution.setCreationDateMillis(System.currentTimeMillis());
        workflowExecution.setWorkflowId(workflow.getId());
        workflowExecution.setOwnerId(getOwnerId(userArn));
        workflowExecution.setStatus(Status.Pending);

        return workflowExecution;
    }

    public static String getOwnerId(@Nonnull final String userArn) {
        Preconditions.checkNotNull(userArn, "The userArn must not be null in order to build ownerId with authProvider!");
        return String.join(
                "@",
                ArnHelper.getOwnerIdFromUserArn(userArn),
                ArnHelper.getAuthProviderFromUserArn(userArn));
    }

    public static InitializeCrawlSessionHandler.Input generateInitializeCrawlSessionInput(
            @Nonnull final String workflowExecutionId,
            @Nonnull final String currentRootAddress,
            int currentRecursionDepth) {
        Preconditions.checkNotNull(
                workflowExecutionId,
                "The 'workflowExecutionId' cannot be null when trying to generate 'InitializeCrawlSessionHandler.Input'!"
        );
        Preconditions.checkNotNull(
                currentRootAddress,
                "The 'currentRootAddress' cannot be null when trying to generate 'InitializeCrawlSessionHandler!"
        );
        Preconditions.checkArgument(
                currentRecursionDepth >= 0,
                "The current recursion depth must be a positive (>=0) integer!"
        );

        final InitializeCrawlSessionHandler.Input input = new InitializeCrawlSessionHandler.Input();
        input.setWorkflowExecutionId(workflowExecutionId);
        input.setCurrentRootAddress(currentRootAddress);
        input.setCurrentDepthLevel(currentRecursionDepth);

        return input;
    }
}
