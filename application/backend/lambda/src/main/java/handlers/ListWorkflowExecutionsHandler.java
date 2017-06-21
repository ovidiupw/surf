package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import utils.Logger;

public class ListWorkflowExecutionsHandler
        implements RequestHandler<ListWorkflowExecutionsHandler.Input, ListWorkflowExecutionsHandler.Output> {

    private Logger LOG;

    public ListWorkflowExecutionsHandler.Output handleRequest(
            final ListWorkflowExecutionsHandler.Input input,
            final Context context) {
        LOG = new Logger(context.getLogger());
        LOG.info(("Input='%s'"), input.toString());
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
