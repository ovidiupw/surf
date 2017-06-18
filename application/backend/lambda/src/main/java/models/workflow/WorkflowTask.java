package models.workflow;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import converters.SelectionPolicyConverter;
import converters.StatusConverter;
import converters.WorkflowExecutionTaskFailureConverter;
import converters.WorkflowTaskStatusAndDepthConverter;

import java.util.List;
import java.util.Objects;

/**
 * Represents a unit of work that a TaskCrawler is able to fetch.
 * The TaskCrawler will execute the crawling process based on this task.
 */
@DynamoDBTable(tableName = WorkflowTask.TABLE_NAME)
public class WorkflowTask {
    static final String TABLE_NAME = "WorkflowExecutionTasks";

    /**
     * The unique id of this task.
     */
    private String id;

    /**
     * The date, as epoch milliseconds, when the task was created.
     */
    private long creationDateMillis;

    /**
     * The {@link WorkflowExecution workflow execution} id from which this
     * task was created.
     */
    private String workflowExecutionId;

    /**
     * The id of the owner of the {@link Workflow}. This should be equal to what the
     * {@link Workflow#getOwnerId()} provides.
     */
    private String ownerId;

    /**
     * The {@link Status status} of the task.
     */
    private Status status;

    /**
     * The maximum number of bytes that a web page may have in order to be crawled.
     */
    private long maxWebPageSizeBytes;

    /**
     * The depth (for details about what the depth is, see {@link WorkflowMetadata#maxRecursionDepth} and
     * {@link WorkflowMetadata#maxPagesPerDepthLevel}) which this task has been assigned to. This is
     * important for quick task lookup based on the current crawling depth (keep in mind that there
     * is a {@link WorkflowMetadata#maxPagesPerDepthLevel maximum number of pages allowed to be visited at
     * a certain depth level}.
     */
    private long depth;

    /**
     * The url which should be crawled by this task.
     */
    private String url;

    /**
     * A regular expression used to select what links are followed throughout
     * the crawling process. Any link url that is not matched by the regular
     * expression will not be crawled.
     */
    private String urlMatcher;

    /**
     * Defines how the data in the crawled web-page is to be selected for extraction and
     * persisted. See {@link SelectionPolicy} for more details.
     */
    private SelectionPolicy selectionPolicy;

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
     * Corroboration between status and depth fields in order to be able to do useful queries on index.
     */
    private String statusAndDepth;

    /**
     * The version of task. Used as a mutex via optimistic locking. See
     * <a href="http://www.orafaq.com/papers/locking.pdf">Oracle Optimistic Locking</a>
     * and/or
     * <a href="http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.OptimisticLocking.html">
     *     DynamoDB Optimistic Locking</a>
     * for more details.
     */
    private Long version;

    public static final String DDB_ID = "Id";
    public static final String DDB_CREATION_DATE_MILLIS = "CreationDateMillis";
    public static final String DDB_WORKFLOW_EXECUTION_ID = "WorkflowExecutionId";
    public static final String DDB_WORKFLOW_EXECUTION_ID_STATUS_DEPTH_GSI = "WorkflowExecutionIdStatusDepthGSI";
    public static final String DDB_OWNER_ID = "OwnerId";
    public static final String DDB_STATUS = "Status";
    public static final String DDB_CRAWL_URL = "CrawlURL";
    public static final String DDB_CRAWL_DEPTH = "CrawlDepth";
    public static final String DDB_STATUS_AND_DEPTH = "StatusAndDepth";
    public static final String DDB_MAX_WEBPAGE_SIZE_BYTES = "MaxWebPageSizeBytes";
    public static final String DDB_SELECTION_POLICY = "SelectionPolicy";
    public static final String DDB_VERSION = "Version";
    public static final String DDB_URL_MATCHER = "URLMatcher";
    public static final String DDB_START_DATE = "StartDate";
    public static final String DDB_END_DATE = "EndDate";
    public static final String DDB_FAILURES = "Failures";

    @DynamoDBHashKey(attributeName = DDB_ID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexHashKey(
            attributeName = DDB_WORKFLOW_EXECUTION_ID,
            globalSecondaryIndexName = DDB_WORKFLOW_EXECUTION_ID_STATUS_DEPTH_GSI)
    public String getWorkflowExecutionId() {
        return workflowExecutionId;
    }

    public void setWorkflowExecutionId(String workflowExecutionId) {
        this.workflowExecutionId = workflowExecutionId;
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
            globalSecondaryIndexName = DDB_WORKFLOW_EXECUTION_ID_STATUS_DEPTH_GSI,
            attributeName = DDB_STATUS_AND_DEPTH)
    @DynamoDBTypeConverted(converter = WorkflowTaskStatusAndDepthConverter.class)
    public String getStatusAndDepth() {
        return String.join("-", status.getName(), String.valueOf(depth));
    }

    public void setStatusAndDepth(String statusAndDepth) {
        this.statusAndDepth = statusAndDepth;
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
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @DynamoDBAttribute(attributeName = DDB_CRAWL_DEPTH)
    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    @DynamoDBAttribute(attributeName = DDB_FAILURES)
    @DynamoDBTypeConverted(converter = WorkflowExecutionTaskFailureConverter.class)
    public List<WorkflowExecutionTaskFailure> getFailures() {
        return failures;
    }

    public void setFailures(List<WorkflowExecutionTaskFailure> failures) {
        this.failures = failures;
    }

    @DynamoDBAttribute(attributeName = DDB_MAX_WEBPAGE_SIZE_BYTES)
    public long getMaxWebPageSizeBytes() {
        return maxWebPageSizeBytes;
    }

    public void setMaxWebPageSizeBytes(long maxWebPageSizeBytes) {
        this.maxWebPageSizeBytes = maxWebPageSizeBytes;
    }

    @DynamoDBAttribute(attributeName = DDB_URL_MATCHER)
    public String getUrlMatcher() {
        return urlMatcher;
    }

    public void setUrlMatcher(String urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    @DynamoDBAttribute(attributeName = DDB_SELECTION_POLICY)
    @DynamoDBTypeConverted(converter = SelectionPolicyConverter.class)
    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    @DynamoDBIgnore
    public static String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowTask that = (WorkflowTask) o;
        return creationDateMillis == that.creationDateMillis &&
                maxWebPageSizeBytes == that.maxWebPageSizeBytes &&
                depth == that.depth &&
                startDate == that.startDate &&
                endDate == that.endDate &&
                Objects.equals(id, that.id) &&
                Objects.equals(workflowExecutionId, that.workflowExecutionId) &&
                Objects.equals(ownerId, that.ownerId) &&
                status == that.status &&
                Objects.equals(url, that.url) &&
                Objects.equals(urlMatcher, that.urlMatcher) &&
                Objects.equals(selectionPolicy, that.selectionPolicy) &&
                Objects.equals(failures, that.failures) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDateMillis, workflowExecutionId, ownerId, status, maxWebPageSizeBytes, depth, url, urlMatcher, selectionPolicy, startDate, endDate, failures, version);
    }

    @Override
    public String toString() {
        return "WorkflowTask{" +
                "id='" + id + '\'' +
                ", creationDateMillis=" + creationDateMillis +
                ", workflowExecutionId='" + workflowExecutionId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", status=" + status +
                ", maxWebPageSizeBytes=" + maxWebPageSizeBytes +
                ", depth=" + depth +
                ", url='" + url + '\'' +
                ", urlMatcher='" + urlMatcher + '\'' +
                ", selectionPolicy=" + selectionPolicy +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", failures=" + failures +
                ", version=" + version +
                '}';
    }
}
