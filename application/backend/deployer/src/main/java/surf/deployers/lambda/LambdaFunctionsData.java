package surf.deployers.lambda;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class LambdaFunctionsData {

    private LambdaData listCoreWorkersData;
    private LambdaData listWorkflowsData;
    private LambdaData createWorkflowData;
    private LambdaData startWorkflowData;
    private LambdaData getWorkflowData;
    private LambdaData initializeCrawlSessionData;
    private LambdaData crawlWebPageData;
    private LambdaData finalizeCrawlSessionData;
    private LambdaData listWorkflowExecutionsData;
    private LambdaData apiAuthorizerData;

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

    public LambdaData getCreateWorkflowData() {
        return createWorkflowData;
    }

    public LambdaData getListWorkflowExecutionsData() {
        return listWorkflowExecutionsData;
    }

    public LambdaData getApiAuthorizerData() {
        return apiAuthorizerData;
    }

    public static class Builder {

        private LambdaData listCoreWorkersData;
        private LambdaData listWorkflowsData;
        private LambdaData createWorkflowData;
        private LambdaData startWorkflowData;
        private LambdaData getWorkflowData;
        private LambdaData initializeCrawlSessionData;
        private LambdaData crawlWebPageData;
        private LambdaData finalizeCrawlSessionData;
        private LambdaData listWorkflowExecutionsData;
        private LambdaData apiAuthorizerData;

        public LambdaFunctionsData build() {
            Preconditions.checkNotNull(listCoreWorkersData);
            Preconditions.checkNotNull(listWorkflowsData);
            Preconditions.checkNotNull(createWorkflowData);
            Preconditions.checkNotNull(startWorkflowData);
            Preconditions.checkNotNull(getWorkflowData);
            Preconditions.checkNotNull(initializeCrawlSessionData);
            Preconditions.checkNotNull(crawlWebPageData);
            Preconditions.checkNotNull(finalizeCrawlSessionData);
            Preconditions.checkNotNull(listWorkflowExecutionsData);
            Preconditions.checkNotNull(apiAuthorizerData);

            final LambdaFunctionsData lambdaFunctionsData = new LambdaFunctionsData();
            lambdaFunctionsData.setListCoreWorkersData(listCoreWorkersData);
            lambdaFunctionsData.setListWorkflowsData(listWorkflowsData);
            lambdaFunctionsData.setCreateWorkflowData(createWorkflowData);
            lambdaFunctionsData.setStartWorkflowData(startWorkflowData);
            lambdaFunctionsData.setGetWorkflowData(getWorkflowData);
            lambdaFunctionsData.setInitializeCrawlSessionData(initializeCrawlSessionData);
            lambdaFunctionsData.setCrawlWebPageData(crawlWebPageData);
            lambdaFunctionsData.setFinalizeCrawlSessionData(finalizeCrawlSessionData);
            lambdaFunctionsData.setListWorkflowExecutionsData(listWorkflowExecutionsData);
            lambdaFunctionsData.setApiAuthorizerData(apiAuthorizerData);
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

        public Builder withCreateWorkflowData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.createWorkflowData = lambdaData;
            return this;
        }

        public Builder withListWorkflowExecutionsData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.listWorkflowExecutionsData = lambdaData;
            return this;
        }

        public Builder withApiAuthorizerData(@Nonnull final LambdaData lambdaData) {
            Preconditions.checkNotNull(lambdaData);
            this.apiAuthorizerData = lambdaData;
            return this;
        }
    }

    private void setListCoreWorkersData(final LambdaData listCoreWorkersData) {
        this.listCoreWorkersData = listCoreWorkersData;
    }

    private void setListWorkflowsData(@Nonnull final LambdaData listWorkflowsData) {
        this.listWorkflowsData = listWorkflowsData;
    }

    private void setCreateWorkflowData(@Nonnull final LambdaData createWorkflowData) {
        this.createWorkflowData = createWorkflowData;
    }

    private void setStartWorkflowData(@Nonnull final LambdaData startWorkflowData) {
        this.startWorkflowData = startWorkflowData;
    }

    private void setGetWorkflowData(@Nonnull final LambdaData getWorkflowData) {
        this.getWorkflowData = getWorkflowData;
    }

    private void setInitializeCrawlSessionData(@Nonnull final LambdaData initializeCrawlSessionData) {
        this.initializeCrawlSessionData = initializeCrawlSessionData;
    }

    private void setCrawlWebPageData(@Nonnull final LambdaData crawlWebPageData) {
        this.crawlWebPageData = crawlWebPageData;
    }

    private void setFinalizeCrawlSessionData(@Nonnull final LambdaData finalizeCrawlSessionData) {
        this.finalizeCrawlSessionData = finalizeCrawlSessionData;
    }


    private void setListWorkflowExecutionsData(@Nonnull final LambdaData lambdaData) {
        this.listWorkflowExecutionsData = lambdaData;
    }


    private void setApiAuthorizerData(@Nonnull final LambdaData apiAuthorizerData) {
        this.apiAuthorizerData = apiAuthorizerData;
    }

    @Override
    public String toString() {
        return "LambdaFunctionsData{" +
                "listCoreWorkersData=" + listCoreWorkersData +
                ", listWorkflowsData=" + listWorkflowsData +
                ", createWorkflowData=" + createWorkflowData +
                ", startWorkflowData=" + startWorkflowData +
                ", getWorkflowData=" + getWorkflowData +
                ", initializeCrawlSessionData=" + initializeCrawlSessionData +
                ", crawlWebPageData=" + crawlWebPageData +
                ", finalizeCrawlSessionData=" + finalizeCrawlSessionData +
                ", listWorkflowExecutionsData=" + listWorkflowExecutionsData +
                ", apiAuthorizerData=" + apiAuthorizerData +
                '}';
    }
}
