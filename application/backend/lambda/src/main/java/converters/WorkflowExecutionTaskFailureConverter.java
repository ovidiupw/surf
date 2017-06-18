package converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.workflow.WorkflowExecutionTaskFailure;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;

import java.io.IOException;

public class WorkflowExecutionTaskFailureConverter
        implements DynamoDBTypeConverter<String, WorkflowExecutionTaskFailure> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(WorkflowExecutionTaskFailure workflowExecutionTaskFailure) {
        try {
            return objectMapper.writeValueAsString(workflowExecutionTaskFailure);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public WorkflowExecutionTaskFailure unconvert(String rawWorkflowExecutionTaskFailure) {
        try {
            return objectMapper.readValue(rawWorkflowExecutionTaskFailure, WorkflowExecutionTaskFailure.class);
        } catch (IOException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
