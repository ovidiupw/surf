package models.workflow;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import javax.annotation.Nonnull;
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
     * The S3 link to the object where the crawl data is stored.
     */
    private String s3Link;

    /**
     * The time, as epoch milliseconds, when the crawled data was inserted into the S3 bucket.
     */
    private long creationDateMillis;

    static final String DDB_WORKFLOW_EXECUTION_ID = "WorkflowExecutionId";
    static final String DDB_ID = "Id";
    static final String DDB_S3_BUCKET = "S3Link";
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
        return s3Link;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Link = s3Bucket;
    }

    @DynamoDBAttribute(attributeName = DDB_CREATION_DATE_MILLIS)
    public long getCreationDateMillis() {
        return creationDateMillis;
    }

    public void setCreationDateMillis(long creationDateMillis) {
        this.creationDateMillis = creationDateMillis;
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
        CrawlMetadata metadata = (CrawlMetadata) o;
        return creationDateMillis == metadata.creationDateMillis &&
                Objects.equals(workflowExecutionId, metadata.workflowExecutionId) &&
                Objects.equals(id, metadata.id) &&
                Objects.equals(s3Link, metadata.s3Link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowExecutionId, id, s3Link, creationDateMillis);
    }

    @Override
    public String toString() {
        return "CrawlMetadata{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", id='" + id + '\'' +
                ", s3Bucket='" + s3Link + '\'' +
                ", creationDateMillis=" + creationDateMillis +
                '}';
    }

    public enum CategoryName {
        CSSSelectors("css-selectors"),
        TextSelectors("text-selectors");

        private final String name;

        CategoryName(@Nonnull final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static CategoryName fromName(@Nonnull final String name) {
            for (final CategoryName categoryName : CategoryName.values()) {
                if (categoryName.getName().equals(name)) {
                    return categoryName;
                }
            }

            throw new IllegalArgumentException("There is no associated CategoryName for the supplied value!");
        }

        @Override
        public String toString() {
            return "CategoryName{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
