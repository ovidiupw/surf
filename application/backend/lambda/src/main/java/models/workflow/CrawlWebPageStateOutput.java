package models.workflow;

public class CrawlWebPageStateOutput {
    private String workflowExecutionId;
    private String workflowTaskId;
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

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString() {
        return "CrawlWebPageStateOutput{" +
                "workflowExecutionId='" + workflowExecutionId + '\'' +
                ", workflowTaskId='" + workflowTaskId + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }
}
