package surf.deployers.lambda.config;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

public class ListCoreWorkersLambdaConfig implements LambdaFunctionConfig {

    private static final String NAME = "listCoreWorkers";
    private static final String DESCRIPTION = "Returns a list of all core workers registered with the crawler.";
    private static final String HANDLER_NAME = "handlers.ListCoreWorkersHandler";
    private static final Integer MEMORY_MEGABYTES = 512;
    private static final Integer TIMEOUT_SECONDS = 25;

    private Context context;

    public ListCoreWorkersLambdaConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getListCoreWorkersLambdaRole());
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
        return context.getIAMRoles().getListCoreWorkersLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
