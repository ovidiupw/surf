package models.workflow;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import converters.StatusConverter;
import converters.WorkflowExecutionTaskFailureConverter;

import java.util.Objects;
import java.util.Set;

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

    /**
     * The date, as epoch milliseconds, when the workflow execution ended.
     */
    private long endDateMillis;

    /**
     * The list of {@link WorkflowExecutionFailure executionFailures} that were encountered while trying
     * to execute the web crawling task.
     */
    private Set<WorkflowExecutionFailure> executionFailures;

    /**
     * The ARN of the state machine started by {@link handlers.InitializeCrawlSessionHandler}
     */
    private String stateMachineArn;

    /**
     * The version of task. Used as a mutex via optimistic locking. See
     * <a href="http://www.orafaq.com/papers/locking.pdf">Oracle Optimistic Locking</a>
     * and/or
     * <a href="http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.OptimisticLocking.html">
     *     DynamoDB Optimistic Locking</a>
     * for more details.
     */
    private Long version;

    public static final String DDB_WORKFLOW_ID = "WorkflowId";
    public static final String DDB_WORKFLOW_ID_GSI = "WorkflowIdGSI";
    public static final String DDB_ID = "Id";
    public static final String DDB_CREATION_DATE_MILLIS = "CreationDateMillis";
    public static final String DDB_OWNER_ID = "OwnerId";
    public static final String DDB_START_DATE_MILLIS = "StartDateMillis";
    public static final String DDB_END_DATE_MILLIS = "EndDateMillis";
    public static final String DDB_STATUS = "Status";
    public static final String DDB_EXECUTION_FAILURES = "ExecutionFailure";
    public static final String DDB_STATE_MACHINE_ARN = "StateMachineArn";
    public static final String DDB_VERSION = "Version";

    @DynamoDBIndexHashKey(
            attributeName = DDB_WORKFLOW_ID,
            globalSecondaryIndexName = DDB_WORKFLOW_ID_GSI
    )
    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    @DynamoDBHashKey(attributeName = DDB_ID)
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

    @DynamoDBAttribute(attributeName = DDB_CREATION_DATE_MILLIS)
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

    @DynamoDBAttribute(attributeName = DDB_END_DATE_MILLIS)
    public long getEndDateMillis() {
        return endDateMillis;
    }

    public void setEndDateMillis(long endDateMillis) {
        this.endDateMillis = endDateMillis;
    }

    @DynamoDBAttribute(attributeName = DDB_EXECUTION_FAILURES)
    @DynamoDBTypeConverted(converter = WorkflowExecutionTaskFailureConverter.class)
    public Set<WorkflowExecutionFailure> getExecutionFailures() {
        return executionFailures;
    }

    public void setExecutionFailures(Set<WorkflowExecutionFailure> executionFailures) {
        this.executionFailures = executionFailures;
    }

    @DynamoDBAttribute(attributeName = DDB_STATE_MACHINE_ARN)
    public String getStateMachineArn() {
        return stateMachineArn;
    }

    public void setStateMachineArn(String stateMachineArn) {
        this.stateMachineArn = stateMachineArn;
    }

    @DynamoDBVersionAttribute(attributeName = DDB_VERSION)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
                endDateMillis == that.endDateMillis &&
                Objects.equals(workflowId, that.workflowId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(ownerId, that.ownerId) &&
                status == that.status &&
                Objects.equals(executionFailures, that.executionFailures) &&
                Objects.equals(stateMachineArn, that.stateMachineArn) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowId, id, ownerId, status, creationDateMillis, startDateMillis, endDateMillis, executionFailures, stateMachineArn, version);
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
                ", endDateMillis=" + endDateMillis +
                ", executionFailures=" + executionFailures +
                ", stateMachineArn='" + stateMachineArn + '\'' +
                ", version=" + version +
                '}';
    }
}
