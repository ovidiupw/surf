package surf.deployment;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaArns {

    private String helloWorldLambdaArn;

    public String getHelloWorldLambdaArn() {
        return helloWorldLambdaArn;
    }

    public static class Builder {

        private String helloWorldLambdaArn;

        public LambdaArns build() {
            Preconditions.checkNotNull(helloWorldLambdaArn);

            final LambdaArns lambdaArns = new LambdaArns();
            lambdaArns.setHelloWorldLambdaArn(helloWorldLambdaArn);
            return lambdaArns;
        }

        public Builder withHelloWorldLambdaArn(@Nonnull final String lambdaArn) {
            Preconditions.checkNotNull(lambdaArn);
            this.helloWorldLambdaArn = lambdaArn;
            return this;
        }
    }

    private void setHelloWorldLambdaArn(final String helloWorldLambdaArn) {
        this.helloWorldLambdaArn = helloWorldLambdaArn;
    }

    @Override
    public String toString() {
        return "LambdaArns{" +
                "helloWorldLambdaArn='" + helloWorldLambdaArn + '\'' +
                '}';
    }
}
