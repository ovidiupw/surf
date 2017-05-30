package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

class HelloWorldLambdaConfig implements LambdaFunctionConfig {

    private static final String NAME = "helloWorld";
    private static final String DESCRIPTION = "Hello world test function";
    private static final String HANDLER_NAME = "handlers.HelloWorldHandler";
    private static final Integer MEMORY_MEGABYTES = 128;
    private static final Integer TIMEOUT_SECONDS = 5;

    private Context context;

    public HelloWorldLambdaConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getHelloWorldLambdaRole());
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
        return context.getIAMRoles().getHelloWorldLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
