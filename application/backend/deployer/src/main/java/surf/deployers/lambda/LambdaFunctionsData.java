package surf.deployers.lambda;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaFunctionsData {

    private LambdaData listCoreWorkersData;
    private LambdaData listWorkflowsData;
    private LambdaData startWorkflowData;
    private LambdaData getWorkflowData;
    private LambdaData initializeCrawlSessionData;
    private LambdaData crawlWebPageData;
    private LambdaData finalizeCrawlSessionData;

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

    public LambdaData getInitializeCrawlSessionData() {
        return initializeCrawlSessionData;
    }

    public LambdaData getCrawlWebPageData() {
        return crawlWebPageData;
    }

    public LambdaData getFinalizeCrawlSessionData() {
        return finalizeCrawlSessionData;
    }

    public static class Builder {

        private LambdaData listCoreWorkersData;
        private LambdaData listWorkflowsData;
        private LambdaData startWorkflowData;
        private LambdaData getWorkflowData;
        private LambdaData initializeCrawlSessionData;
        private LambdaData crawlWebPageData;
        private LambdaData finalizeCrawlSessionData;

        public LambdaFunctionsData build() {
            Preconditions.checkNotNull(listCoreWorkersData);
            Preconditions.checkNotNull(listWorkflowsData);
            Preconditions.checkNotNull(startWorkflowData);
            Preconditions.checkNotNull(getWorkflowData);
            Preconditions.checkNotNull(initializeCrawlSessionData);
            Preconditions.checkNotNull(crawlWebPageData);
            Preconditions.checkNotNull(finalizeCrawlSessionData);

            final LambdaFunctionsData lambdaFunctionsData = new LambdaFunctionsData();
            lambdaFunctionsData.setListCoreWorkersData(listCoreWorkersData);
            lambdaFunctionsData.setListWorkflowsData(listWorkflowsData);
            lambdaFunctionsData.setStartWorkflowData(startWorkflowData);
            lambdaFunctionsData.setGetWorkflowData(getWorkflowData);
            lambdaFunctionsData.setInitializeCrawlSessionData(initializeCrawlSessionData);
            lambdaFunctionsData.setCrawlWebPageData(crawlWebPageData);
            lambdaFunctionsData.setFinalizeCrawlSessionData(finalizeCrawlSessionData);
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

        public Builder withInitializeCrawlSessionData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.initializeCrawlSessionData = lambdaData;
            return this;
        }

        public Builder withCrawlWebPageData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.crawlWebPageData = lambdaData;
            return this;
        }

        public Builder withFinalizeCrawlSessionData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.finalizeCrawlSessionData = lambdaData;
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

    private void setInitializeCrawlSessionData(LambdaData initializeCrawlSessionData) {
        this.initializeCrawlSessionData = initializeCrawlSessionData;
    }

    private void setCrawlWebPageData(LambdaData crawlWebPageData) {
        this.crawlWebPageData = crawlWebPageData;
    }

    private void setFinalizeCrawlSessionData(LambdaData finalizeCrawlSessionData) {
        this.finalizeCrawlSessionData = finalizeCrawlSessionData;
    }

    @Override
    public String toString() {
        return "LambdaFunctionsData{" +
                "listCoreWorkersData=" + listCoreWorkersData +
                ", listWorkflowsData=" + listWorkflowsData +
                ", startWorkflowData=" + startWorkflowData +
                ", getWorkflowData=" + getWorkflowData +
                ", initializeCrawlSessionData=" + initializeCrawlSessionData +
                ", crawlWebPageData=" + crawlWebPageData +
                ", finalizeCrawlSessionData=" + finalizeCrawlSessionData +
                '}';
    }
}
