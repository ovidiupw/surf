package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HelloWorldHandler implements RequestHandler<String, String> {
    public String handleRequest(String input, Context context) {
        return String.format("Hello World! Did you just say '%s'?", input);
    }
}
