package surf.deployers.lambda;

import com.amazonaws.services.identitymanagement.model.Role;

interface LambdaFunctionConfig {
    String getFunctionName();

    String getDescription();

    String getHandlerName();

    Integer getMemoryMegabytes();

    Role getIAMRole();

    Integer getTimeoutSeconds();
}
