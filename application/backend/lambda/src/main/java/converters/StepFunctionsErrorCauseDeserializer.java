package converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import models.workflow.StepFunctionsError;

import java.io.IOException;
import java.util.List;

public class StepFunctionsErrorCauseDeserializer extends StdDeserializer<StepFunctionsError.Cause> {

    public StepFunctionsErrorCauseDeserializer() {
        this(null);
    }

    public StepFunctionsErrorCauseDeserializer(final Class<?> valueClass) {
        super(valueClass);
    }

    @Override
    public StepFunctionsError.Cause deserialize(final JsonParser jsonParser, final DeserializationContext context)
            throws IOException {

        final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
        final String errorMessage = rootNode.get("errorMessage").asText();
        final String errorType = rootNode.get("errorType").asText();

        final StringBuilder stackTraceBuilder = new StringBuilder();
        final JsonNode stackTraceNode = rootNode.get("stackTrace");

        final ObjectMapper objectMapper = new ObjectMapper();
        final CollectionType stringCollectionType = TypeFactory
                .defaultInstance()
                .constructCollectionType(List.class, String.class);
        final List<String> stackTraces = objectMapper.readerFor(stringCollectionType).readValue(stackTraceNode);

        for (final String stackTrace : stackTraces) {
            stackTraceBuilder.append(stackTrace);
            stackTraceBuilder.append(",\n");
        }
        stackTraceBuilder.setLength(stackTraceBuilder.length() - 2); // remove last , and s\n

        final StepFunctionsError.Cause stepFunctionsErrorCause = new StepFunctionsError.Cause();
        stepFunctionsErrorCause.setErrorMessage(errorMessage);
        stepFunctionsErrorCause.setErrorType(errorType);
        stepFunctionsErrorCause.setStackTrace(stackTraceBuilder.toString());

        return stepFunctionsErrorCause;
    }
}