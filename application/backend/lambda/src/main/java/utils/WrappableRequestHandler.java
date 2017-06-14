package utils;

import com.amazonaws.services.lambda.runtime.Context;

public interface WrappableRequestHandler<I, O> {
    O doHandleRequest(final I input, final Context context);
}
