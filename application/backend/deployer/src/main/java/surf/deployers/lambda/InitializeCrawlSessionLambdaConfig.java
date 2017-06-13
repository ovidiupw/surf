package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

public class InitializeCrawlSessionLambdaConfig implements LambdaFunctionConfig {

    private static final String NAME = "initializeCrawlSession";
    private static final String DESCRIPTION = "Initializes a new crawling session based on the supplied arguments.";
    private static final String HANDLER_NAME = "handlers.InitializeCrawlSessionHandler";
    private static final Integer MEMORY_MEGABYTES = 128;
    private static final Integer TIMEOUT_SECONDS = 10;

    private Context context;

    public InitializeCrawlSessionLambdaConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getInitializeCrawlSessionLambdaRole());
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
        return context.getIAMRoles().getInitializeCrawlSessionLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
