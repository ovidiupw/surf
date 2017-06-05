package surf.deployment;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class LambdaData {
    private String functionName;
    private String functionArn;

    public LambdaData(@Nonnull final String functionName, @Nonnull final String functionArn) {
        Preconditions.checkArgument(Strings.isNotBlank(functionName));
        Preconditions.checkArgument(Strings.isNotBlank(functionArn));

        this.functionName = functionName;
        this.functionArn = functionArn;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionArn() {
        return functionArn;
    }

    @Override
    public String toString() {
        return "LambdaData{" +
                "functionName='" + functionName + '\'' +
                ", functionArn='" + functionArn + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LambdaData that = (LambdaData) o;
        return Objects.equals(functionName, that.functionName) &&
                Objects.equals(functionArn, that.functionArn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, functionArn);
    }
}