package models.workflow;

import com.google.common.base.Preconditions;
import models.Validateable;

import java.util.Objects;
import java.util.Set;

/**
 * A retry policy is just a grouping of {@link ExponentialBackoffRetrier exponentialBackoffRetriers} that
 * will be used in conjunction in order to implement the retry process.
 * Some use-cases may require using only one {@link ExponentialBackoffRetrier}, which
 * should lead to disregarding this class in favor of using a {@link ExponentialBackoffRetrier}.
 */
public class RetryPolicy implements Validateable {

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

    @Override
    public void validate() throws RuntimeException {
        Preconditions.checkNotNull(
                exponentialBackoffRetriers,
                "The RetryPolicy 'exponentialBackoffRetriers' must not be null but can be empty!"
        );
        for (final ExponentialBackoffRetrier exponentialBackoffRetrier : exponentialBackoffRetriers) {
            exponentialBackoffRetrier.validate();
        }
    }
}
