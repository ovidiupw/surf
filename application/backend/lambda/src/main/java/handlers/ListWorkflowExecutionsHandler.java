package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ListWorkflowExecutionsHandler
        implements RequestHandler<ListWorkflowExecutionsHandler.Input, ListWorkflowExecutionsHandler.Output> {

    private LambdaLogger LOG;

    public ListWorkflowExecutionsHandler.Output handleRequest(
            final ListWorkflowExecutionsHandler.Input input,
            final Context context) {
        LOG = context.getLogger();
        LOG.log(input.toString());
        return null;
    }

    public static class Input {
        private String userArn;
        private long createdBefore;

        public String getUserArn() {
            return userArn;
        }

        public void setUserArn(String userArn) {
            this.userArn = userArn;
        }

        public long getCreatedBefore() {
            return createdBefore;
        }

        public void setCreatedBefore(long createdBefore) {
            this.createdBefore = createdBefore;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userArn='" + userArn + '\'' +
                    ", createdBefore=" + createdBefore +
                    '}';
        }
    }

    public static class Output {
    }
}
