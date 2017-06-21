package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import utils.Logger;

public class GetWorkflowHandler implements RequestHandler<GetWorkflowHandler.Input, GetWorkflowHandler.Output> {

    private Logger LOG;

    public GetWorkflowHandler.Output handleRequest(final GetWorkflowHandler.Input input, final Context context) {
        LOG = new Logger(context.getLogger());
        LOG.info("Input='%s'", input.toString());
        
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
