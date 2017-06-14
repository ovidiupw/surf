package models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import models.converters.StatusConverter;

import java.util.Objects;

/**
 * A WorkflowExecution represents a running instance of a {@link Workflow}.
 * An useful analogy is that the WorkflowExecution is to a {@link Workflow}
 * the same thing as an object is to a class (in Object Oriented Programming).
 */
@DynamoDBTable(tableName = WorkflowExecution.TABLE_NAME)
public class WorkflowExecution {
    static final String TABLE_NAME = "WorkflowExecutions";

    /**
     * The {@link Workflow workflow} id from which this execution was instantiated.
     */
    private String workflowId;

    /**
     * The unique id of this workflow's execution.
     */
    private String id;

    /**
     * The id of the owner of the {@link Workflow}. This should be equal to what the
     * {@link Workflow#getOwnerId()} provides.
     */
    private String ownerId;

    /**
     * The {@link Status status} of the workflow execution.
     */
    private Status status;

    /**
     * The date, as epoch milliseconds, when the workflow execution was created.
     */
    private long creationDateMillis;

    /**
     * The date, as epoch milliseconds, when the workflow execution was started.
     */
    private long startDateMillis;

    public static final String DDB_WORKFLOW_ID = "WorkflowId";
    public static final String DDB_ID = "Id";
    public static final String DDB_OWNER_ID = "OwnerId";
    public static final String DDB_START_DATE_MILLIS = "StartDateMillis";
    public static final String DDB_STATUS = "Status";

    @DynamoDBHashKey(attributeName = DDB_WORKFLOW_ID)
    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    @DynamoDBAttribute(attributeName = DDB_ID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = DDB_OWNER_ID)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @DynamoDBAttribute(attributeName = DDB_STATUS)
    @DynamoDBTypeConverted(converter = StatusConverter.class)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @DynamoDBIgnore
    public long getCreationDateMillis() {
        return creationDateMillis;
    }

    public void setCreationDateMillis(long creationDateMillis) {
        this.creationDateMillis = creationDateMillis;
    }

    @DynamoDBAttribute(attributeName = DDB_START_DATE_MILLIS)
    public long getStartDateMillis() {
        return startDateMillis;
    }

    public void setStartDateMillis(long startDateMillis) {
        this.startDateMillis = startDateMillis;
    }

    @DynamoDBIgnore
    public static String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowExecution that = (WorkflowExecution) o;
        return creationDateMillis == that.creationDateMillis &&
                startDateMillis == that.startDateMillis &&
                Objects.equals(workflowId, that.workflowId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(ownerId, that.ownerId) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowId, id, ownerId, status, creationDateMillis, startDateMillis);
    }

    @Override
    public String toString() {
        return "WorkflowExecution{" +
                "workflowId='" + workflowId + '\'' +
                ", id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", status=" + status +
                ", creationDateMillis=" + creationDateMillis +
                ", startDateMillis=" + startDateMillis +
                '}';
    }
}
