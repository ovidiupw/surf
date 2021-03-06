package models.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import converters.StepFunctionsErrorDeserializer;

import java.util.List;
import java.util.Objects;

@JsonDeserialize(using = StepFunctionsErrorDeserializer.class)
public class StepFunctionsError {
    @JsonProperty("Error")
    private String error;

    @JsonProperty("Cause")
    private StepFunctionsError.Cause cause;

    @JsonProperty("stateInputs")
    private List<CrawlWebPageStateInput> stateInputs;

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

    public List<CrawlWebPageStateInput> getStateInputs() {
        return stateInputs;
    }

    public void setStateInputs(List<CrawlWebPageStateInput> stateInputs) {
        this.stateInputs = stateInputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFunctionsError that = (StepFunctionsError) o;
        return Objects.equals(error, that.error) &&
                Objects.equals(cause, that.cause) &&
                Objects.equals(stateInputs, that.stateInputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, cause, stateInputs);
    }

    @Override
    public String toString() {
        return "StepFunctionsError{" +
                "error='" + error + '\'' +
                ", cause=" + cause +
                ", stateInputs=" + stateInputs +
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
