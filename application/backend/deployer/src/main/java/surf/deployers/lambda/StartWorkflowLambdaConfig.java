package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

public class StartWorkflowLambdaConfig implements LambdaFunctionConfig {
    private static final String NAME = "startWorkflow";
    private static final String DESCRIPTION = "Starts a new crawling workflow.";
    private static final String HANDLER_NAME = "handlers.StartWorkflowHandler";
    private static final Integer MEMORY_MEGABYTES = 128;
    private static final Integer TIMEOUT_SECONDS = 5;

    private Context context;

    public StartWorkflowLambdaConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getStartWorkflowLambdaRole());
        this.context = context;
    }

    @Override
    public String getFunctionName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getHandlerName() {
        return HANDLER_NAME;
    }

    @Override
    public Integer getMemoryMegabytes() {
        return MEMORY_MEGABYTES;
    }

    @Override
    public Role getIAMRole() {
        return context.getIAMRoles().getStartWorkflowLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
