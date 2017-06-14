package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import models.Workflow;
import models.WorkflowMetadata;

public class StartWorkflowHandler implements RequestHandler<StartWorkflowHandler.Input, StartWorkflowHandler.Output> {

    private LambdaLogger LOG;

    public StartWorkflowHandler.Output handleRequest(final StartWorkflowHandler.Input input, final Context context) {
        LOG = context.getLogger();
        LOG.log(input.toString());
        return null;
    }

    public static class Input {
        private String userArn;
        private String workflowId;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public String getWorkflowId() {
            return workflowId;
        }

        public void setWorkflowId(String workflowId) {
            this.workflowId = workflowId;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", workflowId='" + workflowId + '\'' +
                    '}';
        }
    }

    public static class Output {
    }
}
