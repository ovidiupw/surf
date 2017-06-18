package handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.config.LambdaConfigurationConstants;
import models.workflow.CrawlWebPageStateOutput;
import models.workflow.StepFunctionsError;
import utils.*;
import utils.FileReader;
import utils.aws.dynamo.DynamoDBClientHelper;
import utils.aws.dynamo.DynamoDBOperationsHelper;
import utils.aws.sns.SNSClientHelper;
import utils.aws.sns.SNSOperationsHelper;

import java.io.*;
import java.util.List;

public class FinalizeCrawlSessionHandler implements
        RequestStreamHandler,
        WrappableRequestHandler<List<CrawlWebPageStateOutput>, Object> {

    private LambdaConfigurationConstants config;
    private DynamoDBOperationsHelper dynamoOperationsHelper;
    private SNSOperationsHelper snsOperationsHelper;


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(inputStream, byteArrayOutputStream);
            byte[] inputStreamBytes = byteArrayOutputStream.toByteArray();
            inputStream.close(); // Prevent inputStream from being used again

            ByteArrayInputStream byteArrayInputStream = null;

            try {
                byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);
                Logger.log(context.getLogger(), "Raw input: '%s'", IOUtils.toString(byteArrayInputStream));
                byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);

                final List<CrawlWebPageStateOutput> crawlWebPageStateOutputs
                        = objectMapper.readValue(byteArrayInputStream, objectMapper.getTypeFactory().constructCollectionType(
                        List.class, CrawlWebPageStateOutput.class));

                final ExceptionWrapper<List<CrawlWebPageStateOutput>, Object> exceptionWrapper
                        = new ExceptionWrapper<>(crawlWebPageStateOutputs, context);
                exceptionWrapper.doHandleRequest(this);

            } catch (JsonMappingException e) {
                e.printStackTrace();
                Logger.log(context.getLogger(),
                        "Trying to see if it's an exception we've got from the StepFunctions workflow...");

                byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);
                final StepFunctionsError stepFunctionsError
                        = objectMapper.readValue(byteArrayInputStream, StepFunctionsError.class);

                Logger.log(context.getLogger(),
                        "Error in step functions execution: error='%s', cause='%s'",
                        stepFunctionsError.getError(),
                        stepFunctionsError.getCause());

                // TODO set workflow execution status to failed if exception not recognized as being a retriable
                // TODO error raised in CrawlWebPageHandler class; else decide what to call initialize crawl session
                // TODO with.

            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(context.getLogger(), "Exception while trying to finalize crawl session: '%s'", e.getMessage());
        }

    }

    @Override
    public Object doHandleRequest(List<CrawlWebPageStateOutput> input, Context context) {
        initializeInstance(context);
        Logger.log(context.getLogger(), "Input='%s'", input);

        return null;
    }

    private void initializeInstance(Context context) {
        config = FileReader.readObjectFromFile(Constants.CONFIG_FILE_PATH, LambdaConfigurationConstants.class);
        Logger.log(context.getLogger(), "Using lambda config '%s'", config);

        final AmazonDynamoDB dynamoClient = new DynamoDBClientHelper(context.getLogger()).getDynamoDBClient(config);
        final AmazonSNS snsClient = new SNSClientHelper(context.getLogger()).getSNSClient(config);

        dynamoOperationsHelper = new DynamoDBOperationsHelper(dynamoClient, context.getLogger());
        snsOperationsHelper = new SNSOperationsHelper(snsClient, context);
    }

    public static class Input {
        private List<CrawlWebPageStateOutput> crawlWebPageStateOutputs;

        public List<CrawlWebPageStateOutput> getCrawlWebPageStateOutputs() {
            return crawlWebPageStateOutputs;
        }

        public void setCrawlWebPageStateOutputs(List<CrawlWebPageStateOutput> crawlWebPageStateOutputs) {
            this.crawlWebPageStateOutputs = crawlWebPageStateOutputs;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "crawlWebPageStateOutputs=" + crawlWebPageStateOutputs +
                    '}';
        }
    }

    public static class Output {

    }
}