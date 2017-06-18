package converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class WorkflowTaskStatusAndDepthConverter implements DynamoDBTypeConverter<String, String> {
    @Override
    public String convert(String statusDepth) {
        return statusDepth;
    }

    @Override
    public String unconvert(String rawStatus) {
        return rawStatus;
    }
}
