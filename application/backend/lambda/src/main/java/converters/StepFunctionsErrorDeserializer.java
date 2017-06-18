package converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import models.workflow.StepFunctionsError;

import java.io.IOException;

public class StepFunctionsErrorDeserializer extends StdDeserializer<StepFunctionsError> {

    public StepFunctionsErrorDeserializer() {
        this(null);
    }

    public StepFunctionsErrorDeserializer(final Class<?> valueClass) {
        super(valueClass);
    }

    @Override
    public StepFunctionsError deserialize(final JsonParser jsonParser, final DeserializationContext context)
            throws IOException {

        final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
        final String error = rootNode.get("Error").asText();

        final ObjectMapper objectMapper = new ObjectMapper();
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(StepFunctionsError.Cause.class, new StepFunctionsErrorCauseDeserializer());
        objectMapper.registerModule(simpleModule);

        final StepFunctionsError.Cause cause = objectMapper.readValue(
                rootNode.get("Cause").asText(),
                StepFunctionsError.Cause.class);

        final StepFunctionsError stepFunctionsError = new StepFunctionsError();
        stepFunctionsError.setError(error);
        stepFunctionsError.setCause(cause);

        return stepFunctionsError;
    }
}