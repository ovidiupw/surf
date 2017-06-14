package models.parsers;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import utils.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonParser {

    private final Context context;

    public JsonParser(@Nonnull final Context context) {
        this.context = context;
    }

    public <T> T parse(@Nonnull final String json, @Nonnull final Class<T> clazz) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(json));

        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonParseException
                | JsonMappingException e) {
            final String errorMessage = Logger.LOG(
                    context.getLogger(),
                    "Error while trying to parse json to class '%s': '%s'",
                    e.getMessage(),
                    clazz.getName());
            throw new BadRequestException(errorMessage);
        } catch (IOException e) {
            final String errorMessage = Logger.LOG(
                    context.getLogger(),
                    "Error while trying to parse json to class '%s': '%s'",
                    e.getMessage(),
                    clazz.getName());
            throw new InternalServerException(errorMessage);
        }
    }
}
