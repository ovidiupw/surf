package utils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class Logger {

    private final LambdaLogger logger;

    public Logger(@Nonnull final LambdaLogger lambdaLogger) {
        Preconditions.checkNotNull(lambdaLogger);
        this.logger = lambdaLogger;
    }

    public Logger() {
        this.logger = null;
    }

    public String error(@Nonnull final String message) {
        return Logger.log(this.logger, String.format("[ERROR]: %s", message));
    }

    public String info(@Nonnull final String message) {
        return Logger.log(this.logger, String.format("[INFO]: %s", message));
    }

    public String warn(@Nonnull final String message) {
        return Logger.log(this.logger, String.format("[WARN]: %s", message));
    }

    public String log(@Nonnull final String message) {
        return Logger.log(this.logger, message);
    }


    public String error(@Nonnull final String message, @Nonnull final Object ...args) {
        return Logger.log(this.logger, String.format("[ERROR]: %s", message), args);
    }

    public String info(@Nonnull final String message, @Nonnull final Object ...args) {
        return Logger.log(this.logger, String.format("[INFO]: %s", message), args);
    }

    public String warn(@Nonnull final String message, @Nonnull final Object ...args) {
        return Logger.log(this.logger, String.format("[WARN]: %s", message), args);
    }

    public String log(@Nonnull final String message, @Nonnull final Object ...args) {
        return Logger.log(this.logger, message, args);
    }

    private static String log(
            @Nonnull final LambdaLogger lambdaLogger,
            @Nonnull final String message) {
        lambdaLogger.log(message);
        return message;
    }

    private static String log(
            @Nonnull final LambdaLogger lambdaLogger,
            @Nonnull final String message,
            @Nonnull final Object ...args) {
        final String formattedMessage = String.format(message, args);
        lambdaLogger.log(formattedMessage);
        return formattedMessage;
    }
}
