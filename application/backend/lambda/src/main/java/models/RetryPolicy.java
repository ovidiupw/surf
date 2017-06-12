package models;

import java.util.Objects;
import java.util.Set;

/**
 * A retry policy is just a grouping of {@link ExponentialBackoffRetrier exponentialBackoffRetriers} that
 * will be used in conjunction in order to implement the retry process.
 * Some use-cases may require using only one {@link ExponentialBackoffRetrier}, which
 * should lead to disregarding this class in favor of using a {@link ExponentialBackoffRetrier}.
 */
public class RetryPolicy {

    /**
     * The set of {@link ExponentialBackoffRetrier exponentialBackoffRetriers} configured for this retry policy.
     */
    private Set<ExponentialBackoffRetrier> exponentialBackoffRetriers;


    public Set<ExponentialBackoffRetrier> getExponentialBackoffRetriers() {
        return exponentialBackoffRetriers;
    }

    public void setExponentialBackoffRetriers(Set<ExponentialBackoffRetrier> exponentialBackoffRetriers) {
        this.exponentialBackoffRetriers = exponentialBackoffRetriers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetryPolicy that = (RetryPolicy) o;
        return Objects.equals(exponentialBackoffRetriers, that.exponentialBackoffRetriers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exponentialBackoffRetriers);
    }

    @Override
    public String toString() {
        return "RetryPolicy{" +
                "exponentialBackoffRetriers=" + exponentialBackoffRetriers +
                '}';
    }
}
