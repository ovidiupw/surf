package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;

import java.io.IOException;

public class InitializeCrawlSessionHandler
        implements RequestHandler<SNSEvent, InitializeCrawlSessionHandler.Output> {

    @Override
    public InitializeCrawlSessionHandler.Output handleRequest(
            final SNSEvent snsEvent,
            final Context context) {

        try {
            final Input input = new ObjectMapper().readValue(
                    snsEvent.getRecords().get(0).getSNS().getMessage(),
                    Input.class
            );
            Logger.log(context.getLogger(), "Input payload from SNS event='%s'", input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("Could not deserialize SNS event's message to Input because of an IOException!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new BadRequestException("Could not get SNS event payload! Make sure the SNS object is correct!");
        }

        return null;
    }

    public static class Input {
        private String workflowExecutionId;
        private String currentRootAddress;
        private int currentDepthLevel;

        public String getWorkflowExecutionId() {
            return workflowExecutionId;
        }

        public void setWorkflowExecutionId(String workflowExecutionId) {
            this.workflowExecutionId = workflowExecutionId;
        }

        public String getCurrentRootAddress() {
            return currentRootAddress;
        }

        public void setCurrentRootAddress(String currentRootAddress) {
            this.currentRootAddress = currentRootAddress;
        }

        public int getCurrentDepthLevel() {
            return currentDepthLevel;
        }

        public void setCurrentDepthLevel(int currentDepthLevel) {
            this.currentDepthLevel = currentDepthLevel;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "workflowExecutionId='" + workflowExecutionId + '\'' +
                    ", currentRootAddress='" + currentRootAddress + '\'' +
                    ", currentDepthLevel=" + currentDepthLevel +
                    '}';
        }
    }

    public static class Output {
    }
}
