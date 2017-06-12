package models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = CrawlMetadata.TABLE_NAME)
public class CrawlMetadata {
    static final String TABLE_NAME = "CrawlMetadata";

    /**
     * The workflow execution id that generated the current crawl metadata.
     */
    private String workflowExecutionId;

    /**
     * The unique id associated with the this crawl metadata.
     */
    private String id;

    /**
     * The S3 bucket where the crawl data is stored.
     */
    private String s3Bucket;

    /**
     * The time, as epoch milliseconds, when the crawled data was inserted into the S3 bucket.
     */
    private String creationDateMillis;

    static final String DDB_WORKFLOW_EXECUTION_ID = "WorkflowExecutionId";
    static final String DDB_ID = "Id";
    static final String DDB_S3_BUCKET = "S3Bucket";
    static final String DDB_CREATION_DATE_MILLIS = "CreationDateMillis";

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

    @DynamoDBAttribute(attributeName = DDB_S3_BUCKET)
    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    @DynamoDBAttribute(attributeName = DDB_CREATION_DATE_MILLIS)
    public String getCreationDateMillis() {
        return creationDateMillis;
    }

    public void setCreationDateMillis(String creationDateMillis) {
        this.creationDateMillis = creationDateMillis;
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
        CrawlMetadata that = (CrawlMetadata) o;
        return Objects.equals(workflowExecutionId, that.workflowExecutionId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(s3Bucket, that.s3Bucket) &&
                Objects.equals(creationDateMillis, that.creationDateMillis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowExecutionId, id, s3Bucket, creationDateMillis);
    }

    @Override
    public String toString() {
        return "CrawlMetadata{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", id='" + id + '\'' +
                ", s3Bucket='" + s3Bucket + '\'' +
                ", creationDateMillis='" + creationDateMillis + '\'' +
                '}';
    }
}
