package converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import models.workflow.Status;

public class StatusConverter implements DynamoDBTypeConverter<String, Status> {
    @Override
    public String convert(Status status) {
        return status.getName();
    }

    @Override
    public Status unconvert(String rawStatus) {
        return Status.fromName(rawStatus);
    }
}
