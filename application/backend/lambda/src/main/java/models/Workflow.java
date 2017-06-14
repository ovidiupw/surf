package models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.google.common.base.Preconditions;
import models.converters.WorkflowMetadataConverter;

import java.util.Objects;

/**
 * A workflow represents the basic unit of configuration for the web
 * crawler. A workflow is uniquely identified by its id and always has
 * an associated owner. The workflow's representation contains metadata
 * used to describe any quirks and details about a {@link WorkflowExecution
 * workflow's execution}
 */
@DynamoDBTable(tableName = Workflow.TABLE_NAME)
public class Workflow implements Validateable {
    static final String TABLE_NAME = "Workflows";

    /**
     * The workflow's unique identifier.
     */
    private String id;

    /**
     * The timestamp, in millis, when the workflow was created.
     */
    private long creationDateMillis;

    /**
     * The unique id of the workflow's owner. This may be represented as
     * a concatenation of the authorizer's name and the id the authorizer
     * assigned to the owner (user).
     */
    private String ownerId;

    /**
     * A friendly name associated to the workflow.
     */
    private String name;

    /**
     * Workflow metadata for the current workflow. Represent as a
     * {@link WorkflowMetadata}.
     */
    private WorkflowMetadata metadata;

    public static final String DDB_ID = "Id";
    public static final String DDB_CREATION_DATE_MILLIS = "CreationDateMillis";
    public static final String DDB_NAME = "Name";
    public static final String DDB_OWNER_ID = "OwnerId";
    public static final String DDB_METADATA = "Metadata";

    public static final String DDB_OWNER_GSI = "OwnerGSI";

    @DynamoDBHashKey(attributeName = DDB_ID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexRangeKey(
            globalSecondaryIndexName = DDB_OWNER_GSI,
            attributeName = DDB_CREATION_DATE_MILLIS)
    public long getCreationDateMillis() {
        return creationDateMillis;
    }

    public void setCreationDateMillis(long creationDateMillis) {
        this.creationDateMillis = creationDateMillis;
    }

    @DynamoDBIndexHashKey(
            globalSecondaryIndexName = DDB_OWNER_GSI,
            attributeName = DDB_OWNER_ID)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @DynamoDBAttribute(attributeName = DDB_METADATA)
    @DynamoDBTypeConverted(converter = WorkflowMetadataConverter.class)
    public WorkflowMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(WorkflowMetadata metadata) {
        this.metadata = metadata;
    }

    @DynamoDBAttribute(attributeName = DDB_NAME)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The DynamoDB table name associated with this class.
     */
    @DynamoDBIgnore
    public static String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workflow workflow = (Workflow) o;
        return creationDateMillis == workflow.creationDateMillis &&
                Objects.equals(id, workflow.id) &&
                Objects.equals(ownerId, workflow.ownerId) &&
                Objects.equals(name, workflow.name) &&
                Objects.equals(metadata, workflow.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDateMillis, ownerId, name, metadata);
    }

    @Override
    public String toString() {
        return "Workflow{" +
                "id='" + id + '\'' +
                ", creationDateMillis=" + creationDateMillis +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", metadata=" + metadata +
                '}';
    }

    @Override
    public void validate() throws RuntimeException {
        Preconditions.checkNotNull(
                getId(),
                "The workflow 'id' must not be null!"
        );
        Preconditions.checkArgument(
                getCreationDateMillis() >= 0,
                "The workflow 'creationDateMillis' must be >= 0!"
        );
        Preconditions.checkNotNull(
                getOwnerId(),
                "The workflow 'ownerId' must not be null!"
        );
        Preconditions.checkNotNull(
                getMetadata(),
                "The workflow 'metadata' must not be null!"
        );

        getMetadata().validate();
    }
}
