package surf.deployers.lambda.config;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

public class GetS3PresignedUrlConfig implements LambdaFunctionConfig {
    private static final String NAME = "generateS3CrawledDataUrl";
    private static final String DESCRIPTION = "Returns a temporary S3 URL to crawled data";
    private static final String HANDLER_NAME = "handlers.GetS3CrawledDataHandler";
    private static final Integer MEMORY_MEGABYTES = 256;
    private static final Integer TIMEOUT_SECONDS = 15;

    private Context context;

    public GetS3PresignedUrlConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getGetS3CrawledDataLambdaRole());
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
        return context.getIAMRoles().getGetS3CrawledDataLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
