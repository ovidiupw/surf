package converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.WorkflowMetadata;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;

import java.io.IOException;

public class WorkflowMetadataConverter implements DynamoDBTypeConverter<String, WorkflowMetadata> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(final WorkflowMetadata workflowMetadata) {
        try {
            return objectMapper.writeValueAsString(workflowMetadata);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public WorkflowMetadata unconvert(final String rawWorkflowMetadata) {
        try {
            return objectMapper.readValue(rawWorkflowMetadata, WorkflowMetadata.class);
        } catch (IOException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
