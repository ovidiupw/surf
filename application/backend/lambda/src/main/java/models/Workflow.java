package models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
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
public class Workflow {
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
     * The authorization provider which the user used to authenticate.
     */
    private String ownerAuthProvider;

    /**
     * Workflow metadata for the current workflow. Represent as a
     * {@link WorkflowMetadata}.
     */
    private WorkflowMetadata metadata;

    static final String DDB_ID = "Id";
    static final String DDB_CREATION_DATE_MILLIS = "CreationDateMillis";
    static final String DDB_OWNER_ID = "OwnerId";
    static final String DDB_OWNER_AUTH_PROVIDER = "OwnerAuthProvider";
    static final String DDB_METADATA = "Metadata";

    public static final String DDB_OWNER_GSI = "OwnerGSI";

    @DynamoDBHashKey(attributeName = DDB_ID)
    public String getId() {
        return String.join("-", id, String.valueOf(creationDateMillis));
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName = DDB_CREATION_DATE_MILLIS)
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
        return String.join("-", ownerId, ownerAuthProvider);
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @DynamoDBAttribute(attributeName = DDB_OWNER_AUTH_PROVIDER)
    public String getOwnerAuthProvider() {
        return ownerAuthProvider;
    }

    public void setOwnerAuthProvider(String ownerAuthProvider) {
        this.ownerAuthProvider = ownerAuthProvider;
    }

    @DynamoDBAttribute(attributeName = DDB_METADATA)
    @DynamoDBTypeConverted(converter = WorkflowMetadataConverter.class)
    public WorkflowMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(WorkflowMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * @return The DynamoDB table name associated with this class.
     */
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
                Objects.equals(ownerAuthProvider, workflow.ownerAuthProvider) &&
                Objects.equals(metadata, workflow.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDateMillis, ownerId, ownerAuthProvider, metadata);
    }

    @Override
    public String toString() {
        return "Workflow{" +
                "id='" + id + '\'' +
                ", creationDateMillis=" + creationDateMillis +
                ", ownerId='" + ownerId + '\'' +
                ", ownerAuthProvider='" + ownerAuthProvider + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
