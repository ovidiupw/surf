package models.workflow;

import java.util.Objects;

public class WorkflowExecutionFailure {
    private String error;
    private CrawlWebPageError.Cause cause;
    private long timeStamp;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public CrawlWebPageError.Cause getCause() {
        return cause;
    }

    public void setCause(CrawlWebPageError.Cause cause) {
        this.cause = cause;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "WorkflowExecutionFailure{" +
                "error='" + error + '\'' +
                ", cause=" + cause +
                ", timeStamp=" + timeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowExecutionFailure that = (WorkflowExecutionFailure) o;
        return timeStamp == that.timeStamp &&
                Objects.equals(error, that.error) &&
                Objects.equals(cause, that.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, cause, timeStamp);
    }
}
