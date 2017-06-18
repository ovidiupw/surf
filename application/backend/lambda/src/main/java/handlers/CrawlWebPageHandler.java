package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import models.workflow.CrawlWebPageStateInput;
import models.workflow.CrawlWebPageStateOutput;

public class CrawlWebPageHandler implements RequestHandler<CrawlWebPageStateInput, CrawlWebPageStateOutput> {

    private LambdaLogger LOG;

    public CrawlWebPageStateOutput handleRequest(final CrawlWebPageStateInput input, final Context context) {
        /* Any exceptions raised in this will be caught as a {@link StepFunctionsError} object
        in FinalizeCrawlSessionHandler */

        LOG = context.getLogger();
        LOG.log(input.toString());

        final CrawlWebPageStateOutput output = new CrawlWebPageStateOutput();
        output.setWorkflowExecutionId(input.getWorkflowExecutionId());
        output.setWorkflowTaskId(input.getWorkflowTaskId());

        return output;
    }

}