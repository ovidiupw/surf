package surf.deployers.lambda.config;

import com.amazonaws.services.identitymanagement.model.Role;
import com.google.common.base.Preconditions;
import surf.deployment.Context;

import javax.annotation.Nonnull;

public class CrawlWebPageLambdaConfig implements LambdaFunctionConfig {

    private static final String NAME = "crawlWebPage";
    private static final String DESCRIPTION = "Crawls the given webpage, updates the global progress and outputs data to S3.";
    private static final String HANDLER_NAME = "handlers.CrawlWebPageHandler";
    private static final Integer MEMORY_MEGABYTES = 128;
    private static final Integer TIMEOUT_SECONDS = 300;

    private Context context;

    public CrawlWebPageLambdaConfig(@Nonnull final Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getIAMRoles());
        Preconditions.checkNotNull(context.getIAMRoles().getCrawlWebPageLambdaRole());
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
        return context.getIAMRoles().getCrawlWebPageLambdaRole();
    }

    @Override
    public Integer getTimeoutSeconds() {
        return TIMEOUT_SECONDS;
    }
}
