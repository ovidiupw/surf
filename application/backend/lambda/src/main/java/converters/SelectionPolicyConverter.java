package converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.workflow.SelectionPolicy;
import models.workflow.WorkflowMetadata;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;

import java.io.IOException;

public class SelectionPolicyConverter implements DynamoDBTypeConverter<String, SelectionPolicy> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(final SelectionPolicy selectionPolicy) {
        try {
            return objectMapper.writeValueAsString(selectionPolicy);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public SelectionPolicy unconvert(final String rawSelectionPolicy) {
        try {
            return objectMapper.readValue(rawSelectionPolicy, SelectionPolicy.class);
        } catch (IOException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
