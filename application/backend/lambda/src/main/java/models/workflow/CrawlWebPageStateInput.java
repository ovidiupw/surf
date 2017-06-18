package models.workflow;

/**
 * Represents an AWS Step Functions state input for the {@link handlers.CrawlWebPageHandler} lambda task
 */
public class CrawlWebPageStateInput {
    private String workflowExecutionId;
    private String workflowTaskId;
    private String url;
    private long depthLevel;
    private String urlMatcher;
    private long maxWebPageSizeBytes;
    private SelectionPolicy selectionPolicy;
    private String errorInfo;

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

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString() {
        return "CrawlWebPageStateInput{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", workflowTaskId='" + workflowTaskId + '\'' +
                ", url='" + url + '\'' +
                ", depthLevel=" + depthLevel +
                ", urlMatcher='" + urlMatcher + '\'' +
                ", maxWebPageSizeBytes=" + maxWebPageSizeBytes +
                ", selectionPolicy=" + selectionPolicy +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }
}
