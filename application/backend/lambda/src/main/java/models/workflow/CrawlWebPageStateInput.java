package models.workflow;

import com.amazonaws.util.StringUtils;
import com.google.common.base.Preconditions;
import models.Validateable;

import java.util.Objects;

/**
 * Represents an AWS Step Functions state input for the {@link handlers.CrawlWebPageHandler} lambda task
 */
public class CrawlWebPageStateInput implements Validateable {
    private String workflowExecutionId;
    private String workflowTaskId;
    private String url;
    private String urlMatcher;
    private SelectionPolicy selectionPolicy;
    private long depthLevel;
    private long maxWebPageSizeBytes;
    private String ownerId;
    private long maxPagesPerDepthLevel;
    private long crawlerTimeoutSeconds;
    private long maxDepthLevel;
    private boolean followRobotsTxt;

    public long getMaxDepthLevel() {
        return maxDepthLevel;
    }

    public void setMaxDepthLevel(long maxDepthLevel) {
        this.maxDepthLevel = maxDepthLevel;
    }

    public String getWorkflowExecutionId() {
        return workflowExecutionId;
    }

    public void setWorkflowExecutionId(String workflowExecutionId) {
        this.workflowExecutionId = workflowExecutionId;
    }

    public String getWorkflowTaskId() {
        return workflowTaskId;
    }

    public void setWorkflowTaskId(String workflowTaskId) {
        this.workflowTaskId = workflowTaskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(long depthLevel) {
        this.depthLevel = depthLevel;
    }

    public String getUrlMatcher() {
        return urlMatcher;
    }

    public void setUrlMatcher(String urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    public long getMaxWebPageSizeBytes() {
        return maxWebPageSizeBytes;
    }

    public void setMaxWebPageSizeBytes(long maxWebPageSizeBytes) {
        this.maxWebPageSizeBytes = maxWebPageSizeBytes;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public long getMaxPagesPerDepthLevel() {
        return maxPagesPerDepthLevel;
    }

    public void setMaxPagesPerDepthLevel(long maxPagesPerDepthLevel) {
        this.maxPagesPerDepthLevel = maxPagesPerDepthLevel;
    }

    public long getCrawlerTimeoutSeconds() {
        return crawlerTimeoutSeconds;
    }

    public void setCrawlerTimeoutSeconds(long crawlerTimeoutSeconds) {
        this.crawlerTimeoutSeconds = crawlerTimeoutSeconds;
    }

    public boolean getFollowRobotsTxt() {
        return followRobotsTxt;
    }

    public void setFollowRobotsTxt(boolean followRobotsTxt) {
        this.followRobotsTxt = followRobotsTxt;
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(
                !StringUtils.isNullOrEmpty(workflowExecutionId),
                "The 'workflowExecutionId' cannot be null or empty"
        );
        Preconditions.checkArgument(
                !StringUtils.isNullOrEmpty(workflowTaskId),
                "The 'workflowTaskId' cannot be null or empty"
        );
        Preconditions.checkArgument(
                !StringUtils.isNullOrEmpty(ownerId),
                "The 'ownerId' cannot be null or empty"
        );
        Preconditions.checkArgument(
                !StringUtils.isNullOrEmpty(url),
                "The 'url' cannot be null or empty"
        );
        Preconditions.checkArgument(
                !StringUtils.isNullOrEmpty(urlMatcher),
                "The 'urlMatcher' cannot be null or empty"
        );
        Preconditions.checkArgument(
                !StringUtils.isNullOrEmpty(urlMatcher),
                "The 'urlMatcher' cannot be null or empty"
        );

        Preconditions.checkNotNull(selectionPolicy, "The 'selectionPolicy' cannot be null");
        selectionPolicy.validate();

        Preconditions.checkArgument(
                depthLevel >= 0,
                "The 'depthLevel' must be >= 0"
        );
        Preconditions.checkArgument(
                maxDepthLevel >= 0 && maxDepthLevel >= depthLevel,
                "The 'maxDepthLevel' must be >= 0 and >= than 'depthLevel"
        );
        Preconditions.checkArgument(
                maxWebPageSizeBytes >= 1,
                "The 'maxWebPageSizeBytes' must be >= 1"
        );
        Preconditions.checkArgument(
                maxPagesPerDepthLevel >= 1,
                "The 'maxPagesPerDepthLevel' must be >= 1"
        );
        Preconditions.checkArgument(
                crawlerTimeoutSeconds >= 15 && crawlerTimeoutSeconds < 115,
                "The 'crawlerTimeoutSeconds' must be between 15 and 115 seconds!"
        );
    }

    @Override
    public String toString() {
        return "CrawlWebPageStateInput{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", workflowTaskId='" + workflowTaskId + '\'' +
                ", url='" + url + '\'' +
                ", urlMatcher='" + urlMatcher + '\'' +
                ", selectionPolicy=" + selectionPolicy +
                ", depthLevel=" + depthLevel +
                ", maxWebPageSizeBytes=" + maxWebPageSizeBytes +
                ", ownerId='" + ownerId + '\'' +
                ", maxPagesPerDepthLevel=" + maxPagesPerDepthLevel +
                ", crawlerTimeoutSeconds=" + crawlerTimeoutSeconds +
                ", maxDepthLevel=" + maxDepthLevel +
                ", followRobotsTxt=" + followRobotsTxt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrawlWebPageStateInput that = (CrawlWebPageStateInput) o;
        return depthLevel == that.depthLevel &&
                maxWebPageSizeBytes == that.maxWebPageSizeBytes &&
                maxPagesPerDepthLevel == that.maxPagesPerDepthLevel &&
                crawlerTimeoutSeconds == that.crawlerTimeoutSeconds &&
                maxDepthLevel == that.maxDepthLevel &&
                followRobotsTxt == that.followRobotsTxt &&
                Objects.equals(workflowExecutionId, that.workflowExecutionId) &&
                Objects.equals(workflowTaskId, that.workflowTaskId) &&
                Objects.equals(url, that.url) &&
                Objects.equals(urlMatcher, that.urlMatcher) &&
                Objects.equals(selectionPolicy, that.selectionPolicy) &&
                Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowExecutionId, workflowTaskId, url, urlMatcher, selectionPolicy, depthLevel, maxWebPageSizeBytes, ownerId, maxPagesPerDepthLevel, crawlerTimeoutSeconds, maxDepthLevel, followRobotsTxt);
    }
}
