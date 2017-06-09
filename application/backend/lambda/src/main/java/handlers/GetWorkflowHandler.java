package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetWorkflowHandler implements RequestHandler<GetWorkflowHandler.Input, GetWorkflowHandler.Output> {

    private LambdaLogger LOG;

    public GetWorkflowHandler.Output handleRequest(final GetWorkflowHandler.Input input, final Context context) {
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
            return "GetWorkflow Input{" +
                    "userArn='" + userArn + '\'' +
                    '}';
        }
    }

    public static class Output {
    }
}
