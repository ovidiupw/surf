package models;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nonnull;

public enum Status {
    Pending("Pending"),
    Active("Active"),
    Failed("Failed"),
    Completed("Completed");

    private final String name;

    Status(@Nonnull final String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static Status fromName(@Nonnull final String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

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
