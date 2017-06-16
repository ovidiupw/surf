package models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import converters.StatusConverter;
import converters.WorkflowExecutionTaskFailureConverter;

import java.util.List;
import java.util.Objects;

/**
 * Represents a unit of work that a TaskCrawler is able to fetch.
 * The TaskCrawler will execute the crawling process based on this task.
 */
@DynamoDBTable(tableName = WorkflowExecutionTask.TABLE_NAME)
public class WorkflowExecutionTask {
    static final String TABLE_NAME = "WorkflowExecutionTasks";

    /**
     * The id of the owner of the {@link Workflow}. This should be equal to what the
     * {@link Workflow#getOwnerId()} provides.
     */
    private String ownerId;

    /**
     * The unique id of this workflow's execution.
     */
    private String id;

    /**
     * The date, as epoch milliseconds, when the workflow task was created.
     */
    private long creationDateMillis;

    /**
     * The {@link WorkflowExecution workflow execution} id from which this
     * task was created.
     */
    private String workflowExecutionId;

    /**
     * The {@link Status status} of the workflow task.
     */
    private Status status;

    /**
     * The depth (for details about what the depth is, see {@link WorkflowMetadata#maxRecursionDepth} and
     * {@link WorkflowMetadata#maxPagesPerDepthLevel}) which this task has been assigned to. This is
     * important for quick task lookup based on the current crawling depth (keep in mind that there
     * is a {@link WorkflowMetadata#maxPagesPerDepthLevel maximum number of pages allowed to be visited at
     * a certain depth level}.
     */
    private long crawlDepth;

    /**
     * The URL which should be crawled by this task.
     */
    private String crawlURL;

    /**
     * The date at which this task was picked up by a web crawler instance.
     */
    private long startDate;

    /**
     * The date at which this task was finished by a web crawler instance.
     */
    private long endDate;

    /**
     * The list of {@link WorkflowExecutionTaskFailure failures} that were encountered while trying
     * to execute the web crawling task.
     */
    private List<WorkflowExecutionTaskFailure> failures;

    /**
     * The version of task. Used as a mutex via optimistic locking. See
     * <a href="http://www.orafaq.com/papers/locking.pdf">Oracle Optimistic Locking</a>
     * and/or
     * <a href="http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.OptimisticLocking.html">
     *     DynamoDB Optimistic Locking</a>
     * for more details.
     */
    private Long version;

    public static final String DDB_WORKFLOW_EXECUTION_ID = "WorkflowExecutionId";
    public static final String DDB_ID = "Id";
    public static final String DDB_CREATION_DATE_MILLIS = "CreationDateMillis";
    public static final String DDB_OWNER_ID = "OwnerId";
    public static final String DDB_STATUS = "Status";
    public static final String DDB_CRAWL_URL = "CrawlURL";
    public static final String DDB_CRAWL_DEPTH = "CrawlDepth";
    public static final String DDB_STATUS_AND_DEPTH = "StatusAndDepth";
    public static final String DDB_VERSION = "Version";
    public static final String DDB_START_DATE = "StartDate";
    public static final String DDB_END_DATE = "EndDate";
    public static final String DDB_FAILURES = "Failures";

    public static final String DDB_STATUS_AND_DEPTH_LSI = "CrawlStatusAndDepthLSI";

    @DynamoDBHashKey(attributeName = DDB_WORKFLOW_EXECUTION_ID)
    public String getWorkflowExecutionId() {
        return workflowExecutionId;
    }

    public void setWorkflowExecutionId(String workflowExecutionId) {
        this.workflowExecutionId = workflowExecutionId;
    }

    @DynamoDBRangeKey(attributeName = DDB_ID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = DDB_CREATION_DATE_MILLIS)
    public long getCreationDateMillis() {
        return creationDateMillis;
    }

    public void setCreationDateMillis(long creationDateMillis) {
        this.creationDateMillis = creationDateMillis;
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

    @DynamoDBIndexRangeKey(
            localSecondaryIndexName = DDB_STATUS_AND_DEPTH_LSI,
            attributeName = DDB_STATUS_AND_DEPTH)
    //TODO watch for this; 99% broken
    public String getStatusAndDepth() {
        return String.join("-", status.getName(), String.valueOf(crawlDepth));
    }

    @DynamoDBVersionAttribute(attributeName = DDB_VERSION)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    @DynamoDBAttribute(attributeName = DDB_START_DATE)
    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @DynamoDBAttribute(attributeName = DDB_END_DATE)
    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @DynamoDBAttribute(attributeName = DDB_CRAWL_URL)
    public String getCrawlURL() {
        return crawlURL;
    }

    public void setCrawlURL(String crawlURL) {
        this.crawlURL = crawlURL;
    }

    @DynamoDBAttribute(attributeName = DDB_CRAWL_DEPTH)
    public long getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(long crawlDepth) {
        this.crawlDepth = crawlDepth;
    }

    @DynamoDBAttribute(attributeName = DDB_FAILURES)
    @DynamoDBTypeConverted(converter = WorkflowExecutionTaskFailureConverter.class)
    public List<WorkflowExecutionTaskFailure> getFailures() {
        return failures;
    }

    public void setFailures(List<WorkflowExecutionTaskFailure> failures) {
        this.failures = failures;
    }


    @DynamoDBIgnore
    public static String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowExecutionTask that = (WorkflowExecutionTask) o;
        return crawlDepth == that.crawlDepth &&
                startDate == that.startDate &&
                endDate == that.endDate &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(workflowExecutionId, that.workflowExecutionId) &&
                status == that.status &&
                Objects.equals(crawlURL, that.crawlURL) &&
                Objects.equals(failures, that.failures) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, id, workflowExecutionId, status, crawlURL, crawlDepth, startDate, endDate, failures, version);
    }

    @Override
    public String toString() {
        return "WorkflowExecutionTask{" +
                "ownerId='" + ownerId + '\'' +
                ", id='" + id + '\'' +
                ", workflowExecutionId='" + workflowExecutionId + '\'' +
                ", status=" + status +
                ", crawlURL='" + crawlURL + '\'' +
                ", crawlDepth=" + crawlDepth +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", failures=" + failures +
                ", version=" + version +
                '}';
    }
}
