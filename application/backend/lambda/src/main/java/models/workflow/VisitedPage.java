package models.workflow;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Objects;

@DynamoDBTable(tableName = VisitedPage.TABLE_NAME)
public class VisitedPage {
    static final String TABLE_NAME = "VisitedPages";
    /**
     * The workflow execution id that generated the current page visit data.
     */
    private String workflowExecutionId;

    /**
     * The url which was visited or scheduled to visit
     */
    private String url;

    /**
     * The depth (in the crawling process) at which the page was visited
     */
    private long pageVisitDepth;

    /**
     * The version of the VisitedPage. Used as a mutex via optimistic locking. See
     * <a href="http://www.orafaq.com/papers/locking.pdf">Oracle Optimistic Locking</a>
     * and/or
     * <a href="http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.OptimisticLocking.html">
     *     DynamoDB Optimistic Locking</a>
     * for more details.
     */
    private Long version;

    private static final String DDB_WORKFLOW_EXECUTION_ID = "WorkflowExecutionId";
    private static final String DDB_URL = "Url";
    private static final String DDB_VERSION = "Version";
    public static final String DDB_PAGE_VISIT_DEPTH = "PageVisitDepth";
    public static final String DDB_PAGE_VISIT_DEPTH_LSI = "PageVisitDepthLSI";

    @DynamoDBHashKey(attributeName = DDB_WORKFLOW_EXECUTION_ID)
    public String getWorkflowExecutionId() {
        return workflowExecutionId;
    }

    public void setWorkflowExecutionId(String workflowExecutionId) {
        this.workflowExecutionId = workflowExecutionId;
    }

    @DynamoDBRangeKey(attributeName = DDB_URL)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @DynamoDBVersionAttribute(attributeName = DDB_VERSION)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @DynamoDBIndexRangeKey(
            attributeName = DDB_PAGE_VISIT_DEPTH,
            localSecondaryIndexName = DDB_PAGE_VISIT_DEPTH_LSI
    )
    public long getPageVisitDepth() {
        return pageVisitDepth;
    }

    public void setPageVisitDepth(long pageVisitDepth) {
        this.pageVisitDepth = pageVisitDepth;
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
        VisitedPage that = (VisitedPage) o;
        return Objects.equals(workflowExecutionId, that.workflowExecutionId) &&
                Objects.equals(url, that.url) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowExecutionId, url, version);
    }

    @Override
    public String toString() {
        return "VisitedPage{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", url='" + url + '\'' +
                ", version=" + version +
                '}';
    }
}
