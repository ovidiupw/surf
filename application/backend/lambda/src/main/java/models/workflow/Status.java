package models.workflow;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;

public enum Status {
    Pending("Pending"),
    Active("Active"),
    Failed("Failed"),
    Cancelled("Cancelled"),
    Completed("Completed"),
    TimedOut("TimedOut");

    private final String name;

    Status(@Nonnull final String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static Status fromName(@Nonnull final String name) {
        for (final Status status : Status.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }

        throw new IllegalArgumentException("There is no associated Status for the supplied value!");
    }

    @Override
    public String toString() {
        return "Status{" +
                "name='" + name + '\'' +
                '}';
    }
}
