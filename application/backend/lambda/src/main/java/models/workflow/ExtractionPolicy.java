package models.workflow;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Preconditions;
import models.Validateable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a way of extracting a sub-text from a super-text that
 * is richer in content (i.e. longer).
 */
public class ExtractionPolicy implements Validateable {

    /**
     * The {@link Type} of the extraction policy.
     */
    private Type type;

    /**
     * The associated value for the specified extraction policy {@link Type}.
     */
    private int value;

    @Override
    public void validate() throws RuntimeException {
        Preconditions.checkNotNull(
                type,
                "The type of the extraction policy must not be one of " + Arrays.asList(Type.values())
        );
        switch (type) {
            case TextAfter:
                Preconditions.checkArgument(
                        value > 0,
                        "When TextAfter policy type is used, the policy's 'value' must be > 0"
                );
                break;
            case TextBefore:
                Preconditions.checkArgument(
                        value > 0,
                        "When TextBefore policy type is used, the policy's 'value' must be > 0"
                );
                break;
        }
    }

    public enum Type {
        /**
         * Specifies that text before the identified selection is to be extracted.
         * Must be associated with a positive integer value.
         */
        TextBefore("TextBefore"),

        /**
         * Specifies that text after the identified selection is to be extracted.
         * Must be associated with a positive integer value.
         */
        TextAfter("TextAfter");

        private final String name;

        Type(@Nonnull final String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        public static Type fromName(@Nonnull final String name) {
            for (final Type extractionPolicyType : Type.values()) {
                if (extractionPolicyType.getName().equals(name)) {
                    return extractionPolicyType;
                }
            }

            throw new IllegalArgumentException("There is no associated ExtractionPolicy.Type for the supplied value!");
        }

        @Override
        public String toString() {
            return "ExtractionPolicy.Type{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractionPolicy that = (ExtractionPolicy) o;
        return type == that.type &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return "ExtractionPolicy{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}

