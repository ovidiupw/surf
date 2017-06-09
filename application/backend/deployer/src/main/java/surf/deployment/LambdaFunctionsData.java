package surf.deployment;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaFunctionsData {

    private LambdaData listCoreWorkersData;
    private LambdaData listWorkflowsData;
    private LambdaData startWorkflowData;
    private LambdaData getWorkflowData;

    public LambdaData getListCoreWorkersData() {
        return listCoreWorkersData;
    }

    public LambdaData getListWorkflowsData() {
        return listWorkflowsData;
    }

    public LambdaData getStartWorkflowData() {
        return startWorkflowData;
    }

    public LambdaData getGetWorkflowData() {
        return getWorkflowData;
    }

    public static class Builder {

        private LambdaData listCoreWorkersData;
        private LambdaData listWorkflowsData;
        private LambdaData startWorkflowData;
        private LambdaData getWorkflowData;

        public LambdaFunctionsData build() {
            Preconditions.checkNotNull(listCoreWorkersData);
            Preconditions.checkNotNull(listWorkflowsData);
            Preconditions.checkNotNull(startWorkflowData);
            Preconditions.checkNotNull(getWorkflowData);

            final LambdaFunctionsData lambdaFunctionsData = new LambdaFunctionsData();
            lambdaFunctionsData.setListCoreWorkersData(listCoreWorkersData);
            lambdaFunctionsData.setListWorkflowsData(listWorkflowsData);
            lambdaFunctionsData.setStartWorkflowData(startWorkflowData);
            lambdaFunctionsData.setGetWorkflowData(getWorkflowData);
            return lambdaFunctionsData;
        }

        public Builder withListCoreWorkersFunctionData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.listCoreWorkersData = lambdaData;
            return this;
        }

        public Builder withListWorkflowsData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.listWorkflowsData = lambdaData;
            return this;
        }

        public Builder withStartWorkflowData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.startWorkflowData = lambdaData;
            return this;
        }

        public Builder withGetWorkflowData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.getWorkflowData = lambdaData;
            return this;
        }
    }

    private void setListCoreWorkersData(final LambdaData listCoreWorkersData) {
        this.listCoreWorkersData = listCoreWorkersData;
    }

    private void setListWorkflowsData(LambdaData listWorkflowsData) {
        this.listWorkflowsData = listWorkflowsData;
    }

    private void setStartWorkflowData(LambdaData startWorkflowData) {
        this.startWorkflowData = startWorkflowData;
    }

    private void setGetWorkflowData(LambdaData getWorkflowData) {
        this.getWorkflowData = getWorkflowData;
    }

    @Override
    public String toString() {
        return "LambdaFunctionsData{" +
                "listCoreWorkersData=" + listCoreWorkersData +
                ", listWorkflowsData=" + listWorkflowsData +
                ", startWorkflowData=" + startWorkflowData +
                ", getWorkflowData=" + getWorkflowData +
                '}';
    }
}
