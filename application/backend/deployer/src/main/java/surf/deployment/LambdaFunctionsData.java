package surf.deployment;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaFunctionsData {

    private LambdaData helloWorldLambdaData;

    public LambdaData getHelloWorldLambdaData() {
        return helloWorldLambdaData;
    }

    public static class Builder {

        private LambdaData helloWorldLambdaData;

        public LambdaFunctionsData build() {
            Preconditions.checkNotNull(helloWorldLambdaData);

            final LambdaFunctionsData lambdaFunctionsData = new LambdaFunctionsData();
            lambdaFunctionsData.setHelloWorldLambdaData(helloWorldLambdaData);
            return lambdaFunctionsData;
        }

        public Builder withHelloWorldFunctionData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.helloWorldLambdaData = lambdaData;
            return this;
        }
    }

    private void setHelloWorldLambdaData(final LambdaData helloWorldLambdaData) {
        this.helloWorldLambdaData = helloWorldLambdaData;
    }

    @Override
    public String toString() {
        return "LambdaFunctionsData{" +
                "helloWorldLambdaData='" + helloWorldLambdaData + '\'' +
                '}';
    }
}
