package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ListWorkflowsHandler implements RequestHandler<ListWorkflowsHandler.Input, ListWorkflowsHandler.Output> {

    private LambdaLogger LOG;

    public ListWorkflowsHandler.Output handleRequest(final ListWorkflowsHandler.Input input, final Context context) {
        LOG = context.getLogger();
        LOG.log(input.toString());
        return null;
    }

    public static class Input {
        private String userArn;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        @Override
        public String toString() {
            return "ListWorkflows Input{" +
                    "userArn='" + userArn + '\'' +
                    '}';
        }
    }

    public static class Output {
    }
}
