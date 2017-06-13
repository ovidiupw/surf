package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

public class FinalizeCrawlSessionLambdaConfig implements LambdaFunctionConfig {

    private static final String NAME = "finalizeCrawlSession";
    private static final String DESCRIPTION = "Performs finalizing tasks on the crawl session. " +
            "May recursively call initializeCrawlSession through SNS";
    private static final String HANDLER_NAME = "handlers.FinalizeCrawlSessionHandler";
    private static final Integer MEMORY_MEGABYTES = 128;
    private static final Integer TIMEOUT_SECONDS = 10;

    private Context context;

    public FinalizeCrawlSessionLambdaConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getFinalizeCrawlSessionLambdaRole());
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
        return context.getIAMRoles().getFinalizeCrawlSessionLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
