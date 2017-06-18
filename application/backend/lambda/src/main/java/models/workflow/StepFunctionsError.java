package models.workflow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import converters.StepFunctionsErrorDeserializer;

@JsonDeserialize(using = StepFunctionsErrorDeserializer.class)
public class StepFunctionsError {
    private String error;
    private StepFunctionsError.Cause cause;

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

    @Override
    public String toString() {
        return "StepFunctionsError{" +
                "error='" + error + '\'' +
                ", cause=" + cause +
                '}';
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
    }
}
