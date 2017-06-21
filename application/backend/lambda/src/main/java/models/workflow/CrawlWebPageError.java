package models.workflow;

import java.util.Objects;

public class CrawlWebPageError {
    private String error;

    private CrawlWebPageError.Cause cause;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Cause getCause() {
        return cause;
    }

    public void setCause(Cause cause) {
        this.cause = cause;
    }

    public static class Cause {
        private String errorMessage;
        private String errorType;
        private String stackTrace;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        public void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }

        @Override
        public String toString() {
            return "Cause{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", errorType='" + errorType + '\'' +
                    ", stackTrace='" + stackTrace + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cause cause = (Cause) o;
            return Objects.equals(errorMessage, cause.errorMessage) &&
                    Objects.equals(errorType, cause.errorType) &&
                    Objects.equals(stackTrace, cause.stackTrace);
        }

        @Override
        public int hashCode() {
            return Objects.hash(errorMessage, errorType, stackTrace);
        }
    }
}
