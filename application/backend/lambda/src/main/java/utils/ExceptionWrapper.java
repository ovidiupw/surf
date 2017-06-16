package utils;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;

import javax.annotation.Nonnull;

public class ExceptionWrapper<I, O> {

    private final I input;
    private final Context context;

    public ExceptionWrapper(final I input, final Context context) {
        this.input = input;
        this.context = context;
    }

    /**
     * Wraps the supplied argument's 'doHandleRequest' method with server custom logic exception handling.
     *
     * @param handler A strategy for handling the requests. Must implement the interface {@link WrappableRequestHandler}
     *
     * @return The result of calling the 'doHandleRequest' of the supplied 'handler' strategy.
     */
    public O doHandleRequest(@Nonnull final WrappableRequestHandler<I, O> handler) {
        try {
            return handler.doHandleRequest(input, context);
        } catch (IllegalArgumentException | NullPointerException | BadRequestException e) {
            e.printStackTrace();
            final String log = Logger.log(
                    context.getLogger(), "Exception while trying to handle request: '%s'!", e.getMessage());

            if (e.getMessage() == null) {
                throw new InternalServerException("Internal server error!");
            }
            throw new BadRequestException(log);

        } catch (RuntimeException e) {
            e.printStackTrace();
            final String log = Logger.log(
                    context.getLogger(), "Exception while trying to handle request: '%s'!", e.getMessage());

            if (e.getMessage() == null) {
                throw new InternalServerException("Internal server error!");
            }
            throw new InternalServerException(log);
        }
    }
}
