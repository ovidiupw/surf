package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import models.FBSuccessResponse;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiAuthorizerHandler implements RequestHandler<ApiAuthorizerHandler.Input, String> {

    private LambdaLogger lambdaLogger;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String handleRequest(final ApiAuthorizerHandler.Input input, final Context context) {
        lambdaLogger = context.getLogger();
        lambdaLogger.log(input.toString());

        tryAuthorizeWithFacebook(input);

        // Allow access on all APIs and routes
        return "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Action\": [\n" +
                "        \"execute-api:Invoke\"\n" +
                "      ],\n" +
                "      \"Resource\": [\n" +
                "        \"arn:aws:execute-api:*:*:*\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private void tryAuthorizeWithFacebook(Input input) {
        final URL facebookAuthValidatorURL;
        try {
            facebookAuthValidatorURL = new URL(String.format(
                    "https://graph.facebook.com/debug_token?%%20input_token=%s&access_token=%s",
                    input.accessToken,
                    input.accessToken));

        } catch (MalformedURLException e) {
            final String log = Logger.LOG(lambdaLogger, "Error while trying to construct facebook URL: '%s'", e.getMessage());
            throw new InternalServerException(log);
        }

        final HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) facebookAuthValidatorURL.openConnection();
        } catch (IOException e) {
            final String log = Logger.LOG(lambdaLogger, "Error while trying to open connection with facebook: '%s'", e.getMessage());
            throw new InternalServerException(log);
        }

        String response = null;
        FBSuccessResponse fbSuccessResponse = null;
        try {
            response = new String(ByteStreams.toByteArray(connection.getInputStream()), Charsets.UTF_8.name());
            fbSuccessResponse = objectMapper.readValue(response, FBSuccessResponse.class);
        } catch (IOException e) {
            Logger.LOG(lambdaLogger, "Could not map response '%s' to FBSuccessResponse: '%s'",
                    Strings.nullToEmpty(response),
                    e.getMessage());
            throw new BadRequestException("Unauthorized!");
        }

        if (fbSuccessResponse.getData() == null) {
            final String log = Logger.LOG(lambdaLogger, "fbSuccessResponse.getData() was null!");
            throw new InternalServerException(log);
        }

        if (fbSuccessResponse.getData().getUserId() == null) {
            final String log = Logger.LOG(lambdaLogger, "fbSuccessResponse.getData().getUserId() was null!");
            throw new InternalServerException(log);
        }

        if (!fbSuccessResponse.getData().getUserId().equals(input.userId)) {
            final String log = Logger.LOG(lambdaLogger, "Supplied fbId='%s' did not equal to fbQueriedId='%s'",
                    fbSuccessResponse.getData().getUserId(),
                    input.userId);
            throw new BadRequestException("Unauthorized!");
        }
    }

    public static class Input {

        private String userId;
        private String type;
        private String accessToken;
        private String methodArn;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getMethodArn() {
            return methodArn;
        }

        public void setMethodArn(String methodArn) {
            this.methodArn = methodArn;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "userId='" + userId + '\'' +
                    ", type='" + type + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", methodArn='" + methodArn + '\'' +
                    '}';
        }
    }

}