package parsers;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.exceptions.BadRequestException;
import utils.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonParser {

    private final Context context;

    public JsonParser(@Nonnull final Context context) {
        this.context = context;
    }

    public <T> T parse(@Nonnull final String json, @Nonnull final Class<T> clazz, final String errorMessage) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(json),
                errorMessage);

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonParseException
                | JsonMappingException e) {
            final String log = Logger.log(
                    context.getLogger(),
                    "Error while trying to parse json to class '%s': '%s'",
                    e.getMessage(),
                    clazz.getName());
            throw new BadRequestException(log);
        } catch (IOException e) {
            final String log = Logger.log(
                    context.getLogger(),
                    "Error while trying to parse json to class '%s': '%s'",
                    e.getMessage(),
                    clazz.getName());
            throw new BadRequestException(log);
        }
    }
}