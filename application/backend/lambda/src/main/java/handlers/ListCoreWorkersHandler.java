package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class ListCoreWorkersHandler implements RequestHandler<ListCoreWorkersHandler.Input, ListCoreWorkersHandler.Output> {

    private LambdaLogger LOG;

    public ListCoreWorkersHandler.Output handleRequest(final ListCoreWorkersHandler.Input input, final Context context) {
        LOG = context.getLogger();
        LOG.log(input.toString());

        final Output output = new Output();
        output.setCoreWorkersArns(new ArrayList<>());

        return output;
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
            return "ListCoreWorkers Input{" +
                    "userArn='" + userArn + '\'' +
                    '}';
        }
    }

    public static class Output {
        private List<String> coreWorkersArns;

        public List<String> getCoreWorkersArns() {
            return coreWorkersArns;
        }

        public void setCoreWorkersArns(List<String> coreWorkersArns) {
            this.coreWorkersArns = coreWorkersArns;
        }
    }
}
