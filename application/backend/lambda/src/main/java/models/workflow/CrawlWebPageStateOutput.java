package models.workflow;

import java.util.Objects;

public class CrawlWebPageStateOutput {
    private String workflowExecutionId;
    private String workflowTaskId;
    private CrawlWebPageError error;
    private long taskDepthLevel;
    private String url;

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

    public CrawlWebPageError getError() {
        return error;
    }

    public void setError(CrawlWebPageError error) {
        this.error = error;
    }

    public long getTaskDepthLevel() {
        return taskDepthLevel;
    }

    public void setTaskDepthLevel(long taskDepthLevel) {
        this.taskDepthLevel = taskDepthLevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrawlWebPageStateOutput output = (CrawlWebPageStateOutput) o;
        return taskDepthLevel == output.taskDepthLevel &&
                Objects.equals(workflowExecutionId, output.workflowExecutionId) &&
                Objects.equals(workflowTaskId, output.workflowTaskId) &&
                Objects.equals(error, output.error) &&
                Objects.equals(url, output.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowExecutionId, workflowTaskId, error, taskDepthLevel, url);
    }

    @Override
    public String toString() {
        return "CrawlWebPageStateOutput{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", workflowTaskId='" + workflowTaskId + '\'' +
                ", error=" + error +
                ", taskDepthLevel=" + taskDepthLevel +
                ", url='" + url + '\'' +
                '}';
    }
}
