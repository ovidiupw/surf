package converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import models.exceptions.BadRequestException;
import models.exceptions.InternalServerException;
import models.workflow.WorkflowExecutionFailure;

import java.io.IOException;
import java.util.Set;

public class WorkflowExecutionTaskFailureConverter
        implements DynamoDBTypeConverter<String, Set<WorkflowExecutionFailure>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Set<WorkflowExecutionFailure> executionFailure) {
        try {
            return objectMapper.writeValueAsString(executionFailure);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Set<WorkflowExecutionFailure> unconvert(String rawWorkflowExecutionTaskFailure) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final CollectionType stringCollectionType = TypeFactory
                    .defaultInstance()
                    .constructCollectionType(Set.class, WorkflowExecutionFailure.class);
            return objectMapper.readerFor(stringCollectionType).readValue(rawWorkflowExecutionTaskFailure);
        } catch (IOException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
