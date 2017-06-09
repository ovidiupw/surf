package surf.deployment;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class LambdaData {
    private final String functionName;
    private final String functionArn;
    private final String functionDescription;

    public LambdaData(
            @Nonnull final String functionName,
            @Nonnull final String functionArn,
            @Nonnull final String functionDescription) {
        Preconditions.checkArgument(Strings.isNotBlank(functionName));
        Preconditions.checkArgument(Strings.isNotBlank(functionArn));
        Preconditions.checkArgument(Strings.isNotBlank(functionDescription));

        this.functionName = functionName;
        this.functionArn = functionArn;
        this.functionDescription = functionDescription;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionArn() {
        return functionArn;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    @Override
    public String toString() {
        return "LambdaData{" +
                "functionName='" + functionName + '\'' +
                ", functionArn='" + functionArn + '\'' +
                ", functionDescription='" + functionDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LambdaData that = (LambdaData) o;
        return Objects.equals(functionName, that.functionName) &&
                Objects.equals(functionArn, that.functionArn) &&
                Objects.equals(functionDescription, that.functionDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, functionArn, functionDescription);
    }
}