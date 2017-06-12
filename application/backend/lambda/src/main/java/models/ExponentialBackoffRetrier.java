package models;

import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates information about the retry logic of some failed task.
 */
public class ExponentialBackoffRetrier {
    /**
     * The names of the errors which should be retried by using this retrier.
     */
    private Set<String> errors;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExponentialBackoffRetrier exponentialBackoffRetrier = (ExponentialBackoffRetrier) o;
        return intervalSeconds == exponentialBackoffRetrier.intervalSeconds &&
                Double.compare(exponentialBackoffRetrier.backoffRate, backoffRate) == 0 &&
                maxAttempts == exponentialBackoffRetrier.maxAttempts &&
                Objects.equals(errors, exponentialBackoffRetrier.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors, intervalSeconds, backoffRate, maxAttempts);
    }

    @Override
    public String toString() {
        return "ExponentialBackoffRetrier{" +
                "errors=" + errors +
                ", intervalSeconds=" + intervalSeconds +
                ", backoffRate=" + backoffRate +
                ", maxAttempts=" + maxAttempts +
                '}';
    }
}
