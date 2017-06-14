package models;

public interface Validateable {
    /**
     * Classes implementing this interface's method should define, in the method, an
     * RuntimeException based validation.
     */
    void validate() throws RuntimeException;
}
