package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import models.config.LambdaConfigurationConstants;
import models.workflow.CrawlWebPageStateInput;
import models.workflow.CrawlWebPageStateOutput;
import models.workflow.Crawler;
import models.workflow.StepFunctionsError;
import utils.Constants;
import utils.FileReader;
import utils.Logger;
import validators.CrawlWebPageInputValidator;

public class CrawlWebPageHandler implements RequestHandler<CrawlWebPageStateInput, CrawlWebPageStateOutput> {

    private LambdaConfigurationConstants config;
    private CrawlWebPageInputValidator inputValidator;
    private Logger LOG;

    /**
     * Any exceptions raised in this will be caught as a {@link StepFunctionsError} object
     * in FinalizeCrawlSessionHandler.
     */
    public CrawlWebPageStateOutput handleRequest(final CrawlWebPageStateInput input, final Context context) {
        initializeInstance(context);
        LOG.info("Input='%s'", input.toString());

        inputValidator.validate(input);

        final Crawler crawler = new Crawler(config, context);
        return crawler.crawl(input);
    }

    private void initializeInstance(Context context) {
        LOG = new Logger(context.getLogger());

        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        LOG.info("Using lambda config '%s'", config);

        inputValidator = new CrawlWebPageInputValidator(context);
    }

}