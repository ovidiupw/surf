package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InitializeCrawlSessionHandler
        implements RequestHandler<InitializeCrawlSessionHandler.Input, InitializeCrawlSessionHandler.Output> {

    private LambdaLogger LOG;

    public InitializeCrawlSessionHandler.Output handleRequest(
            final InitializeCrawlSessionHandler.Input input,
            final Context context) {

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
            return "InitializeCrawlSessionHandler Input{" +
                    "userArn='" + userArn + '\'' +
                    '}';
        }
    }

    public static class Output {
    }
}
