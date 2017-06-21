package converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Strings;
import models.workflow.CrawlWebPageStateInput;
import models.workflow.StateMachineExecutionDefinition;
import models.workflow.StepFunctionsError;

import java.io.IOException;
import java.util.List;

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
        final JsonNode errorInfoNode = rootNode.get(StateMachineExecutionDefinition.ERROR_INFO);

        final String error = errorInfoNode.get("Error").asText();
        final StepFunctionsError.Cause cause = deserializeCause(errorInfoNode);

        final JsonNode stateInputsNode = rootNode.get(StateMachineExecutionDefinition.STATE_INPUTS);
        final ObjectMapper objectMapper = new ObjectMapper();
        final CollectionType stringCollectionType = TypeFactory
                .defaultInstance()
                .constructCollectionType(List.class, CrawlWebPageStateInput.class);
        final List<CrawlWebPageStateInput> stateInputs
                = objectMapper.readerFor(stringCollectionType).readValue(stateInputsNode);

        final StepFunctionsError stepFunctionsError = new StepFunctionsError();
        stepFunctionsError.setError(error);
        stepFunctionsError.setCause(cause);
        stepFunctionsError.setStateInputs(stateInputs);

        return stepFunctionsError;
    }

    private StepFunctionsError.Cause deserializeCause(JsonNode rootNode) throws IOException {

        if (Strings.isNullOrEmpty(rootNode.get("Cause").asText())) {
            return null;
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(StepFunctionsError.Cause.class, new StepFunctionsErrorCauseDeserializer());
        objectMapper.registerModule(simpleModule);

        return objectMapper.readValue(
                rootNode.get("Cause").asText(),
                StepFunctionsError.Cause.class);
    }
}