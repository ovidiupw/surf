package utils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import javax.annotation.Nonnull;

public class Logger {

    private Logger() {
        throw new UnsupportedOperationException("This class is not instantiable!");
    }

    public static String log(
            @Nonnull final LambdaLogger logger,
            @Nonnull final String message) {
        logger.log(message);
        return message;
    }

    public static String log(
            @Nonnull final LambdaLogger logger,
            @Nonnull final String message,
            @Nonnull final Object ...args) {
        final String formattedMessage = String.format(message, args);
        logger.log(formattedMessage);
        return formattedMessage;
    }
}
