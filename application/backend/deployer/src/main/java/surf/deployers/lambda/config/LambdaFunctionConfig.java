package surf.deployers.lambda.config;

import com.amazonaws.services.identitymanagement.model.Role;

public interface LambdaFunctionConfig {
    String getFunctionName();

    String getDescription();

    String getHandlerName();

    Integer getMemoryMegabytes();

    Role getIAMRole();

    Integer getTimeoutSeconds();
}
