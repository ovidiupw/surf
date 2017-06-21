package models.workflow;

import com.google.common.base.Preconditions;
import models.Validateable;

import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates information about the retry logic of some failed task.
 */
public class ExponentialBackoffRetrier implements Validateable {
    /**
     * The names of the errors which should be retried by using this retrier.
     * The name of the errors represent fully qualified Java class names.
     */
    private Set<String> errors;

    /**
     * Flag indicating if all errors should be retried. The definition of {@link #errors} becomes irrelevant
     * if this is set to true.
     */
    private boolean retryAllErrors;

    /**
     * The number of seconds to wait before the first retry attempt.
     */
    private int intervalSeconds;

    /**
     * The rate at which the delay between retries is increased. Calculated as follows:
     * new_delay = previous_delay * backoffRate, if a previous_delay exists
     * new_delay = intervalSeconds, if a previous_delay does not exist
     */
    private double backoffRate;

    /**
     * The maximum number of attempts retry the failed task.
     */
    private int maxAttempts;

    public Set<String> getErrors() {
        return errors;
    }

    public void setErrors(Set<String> errors) {
        this.errors = errors;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public double getBackoffRate() {
        return backoffRate;
    }

    public void setBackoffRate(double backoffRate) {
        this.backoffRate = backoffRate;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public boolean getRetryAllErrors() {
        return retryAllErrors;
    }

    public void setRetryAllErrors(boolean retryAllErrors) {
        this.retryAllErrors = retryAllErrors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExponentialBackoffRetrier that = (ExponentialBackoffRetrier) o;
        return retryAllErrors == that.retryAllErrors &&
                intervalSeconds == that.intervalSeconds &&
                Double.compare(that.backoffRate, backoffRate) == 0 &&
                maxAttempts == that.maxAttempts &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors, retryAllErrors, intervalSeconds, backoffRate, maxAttempts);
    }

    @Override
    public String toString() {
        return "ExponentialBackoffRetrier{" +
                "errors=" + errors +
                ", retryAllErrors=" + retryAllErrors +
                ", intervalSeconds=" + intervalSeconds +
                ", backoffRate=" + backoffRate +
                ", maxAttempts=" + maxAttempts +
                '}';
    }

    public void validate() {
        if (!retryAllErrors) {
            Preconditions.checkNotNull(
                    errors,
                    "ExponentialBackoffRetrier 'errors' must not be null but can be an empty set! " +
                            "If 'retryAllErrors' is set to 'true', then 'errors' can be null."
            );
        }
        Preconditions.checkArgument(
                intervalSeconds >= 0,
                "ExponentialBackoffRetrier 'intervalSeconds' must be >= 0!"
        );
        Preconditions.checkArgument(
                backoffRate >= 1,
                "ExponentialBackoffRetrier 'backoffRate' must be >= 1!"
        );
        Preconditions.checkArgument(
                maxAttempts >= 0,
                "ExponentialBackoffRetrier 'maxAttempts' must be >= 0!"
        );
    }
}
