package surf;

public enum ExitCode {
    Success(0),
    Error(1);

    private final int code;

    ExitCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public ExitCode fromCode(int code) {
        for (ExitCode exitCode : ExitCode.values()) {
            if (exitCode.getCode() == code) {
                return exitCode;
            }
        }
        throw new IllegalArgumentException("There is no associated ExitCode for the supplied value!");
    }

    @Override
    public String toString() {
        return "ExitCode{" +
                "code=" + code +
                '}';
    }
}
