package models;

import java.util.Objects;

/**
 * Encapsulates all the details necessary for a {@link WorkflowExecution
 * workflow's execution} to be started.
 */
public class WorkflowMetadata {

    /**
     * The URL from which the crawling process is to be started.
     */
    private String rootAddress;

    /**
     * A regular expression used to select what links are followed throughout
     * the crawling process. Any link URL that is not matched by the regular
     * expression will not be crawled.
     */
    private String urlMatcher;

    /**
     * The maximum depth which can be reached by subsequently following unvisited
     * links throughout the crawling process. The recursion depth is defined as
     * the distance between the {@link #rootAddress} and the current address that
     * is being crawled.
     */
    private long maxRecursionDepth;

    /**
     * The maximum number of pages that will be crawled on a certain depth level.
     * For example, if the {@link #rootAddress}, which resides on the depth level 0
     * has N links to other web pages, then the N web-pages will all be crawled only
     * if <b>{@code maxPagesPerDepthLevel >= N}</b>.
     */
    private long maxPagesPerDepthLevel;

    /**
     * The maximum number of bytes that a web page may have in order to be crawled.
     */
    private long maxWebPageSizeBytes;

    /**
     * The number of parallel crawlers that may be executed for a certain depth level.
     * This is important in order to limit the amount of concurrent transactions to the
     * database and control the implicit costs of having the crawler running.
     * <p>
     * If this is set to a higher value, then the crawling process will finish faster
     * (assuming enough read/write capacity units are set on the database), but the
     * costs will be higher.
     */
    private long maxConcurrentCrawlers;

    /**
     * The maximum time, in seconds, that an instance of a web page crawler is allowed to run.
     */
    private long crawlerTimeoutSeconds;

    /**
     * Defines how the data in the crawled web-page is to be selected for extraction and
     * persisted. See {@link SelectionPolicy} for more details.
     */
    private SelectionPolicy selectionPolicy;

    /**
     * Defines the {@link RetryPolicy retry policy} used by the web page crawler executions.
     */
    private RetryPolicy crawlerRetryPolicy;

    /**
     * Defines the {@link RetryPolicy retry policy} used by the web crawler worker executions.
     */
    private RetryPolicy workerRetryPolicy;

    /**
     * Defines the {@link RetryPolicy retry policy} used by the web page crawl orchestrator executions.
     */
    private RetryPolicy orchestratorRetryPolicy;

    /**
     * Defines the {@link RetryPolicy retry policy} used by the web page crawl finalizer executions.
     */
    private RetryPolicy finalizerRetryPolicy;

    

    public String getRootAddress() {
        return rootAddress;
    }

    public void setRootAddress(String rootAddress) {
        this.rootAddress = rootAddress;
    }

    public String getUrlMatcher() {
        return urlMatcher;
    }

    public void setUrlMatcher(String urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    public long getMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public void setMaxRecursionDepth(long maxRecursionDepth) {
        this.maxRecursionDepth = maxRecursionDepth;
    }

    public long getMaxPagesPerDepthLevel() {
        return maxPagesPerDepthLevel;
    }

    public void setMaxPagesPerDepthLevel(long maxPagesPerDepthLevel) {
        this.maxPagesPerDepthLevel = maxPagesPerDepthLevel;
    }

    public long getMaxWebPageSizeBytes() {
        return maxWebPageSizeBytes;
    }

    public void setMaxWebPageSizeBytes(long maxWebPageSizeBytes) {
        this.maxWebPageSizeBytes = maxWebPageSizeBytes;
    }

    public long getMaxConcurrentCrawlers() {
        return maxConcurrentCrawlers;
    }

    public void setMaxConcurrentCrawlers(long maxConcurrentCrawlers) {
        this.maxConcurrentCrawlers = maxConcurrentCrawlers;
    }

    public long getCrawlerTimeoutSeconds() {
        return crawlerTimeoutSeconds;
    }

    public void setCrawlerTimeoutSeconds(long crawlerTimeoutSeconds) {
        this.crawlerTimeoutSeconds = crawlerTimeoutSeconds;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public RetryPolicy getCrawlerRetryPolicy() {
        return crawlerRetryPolicy;
    }

    public void setCrawlerRetryPolicy(RetryPolicy crawlerRetryPolicy) {
        this.crawlerRetryPolicy = crawlerRetryPolicy;
    }

    public RetryPolicy getWorkerRetryPolicy() {
        return workerRetryPolicy;
    }

    public void setWorkerRetryPolicy(RetryPolicy workerRetryPolicy) {
        this.workerRetryPolicy = workerRetryPolicy;
    }

    public RetryPolicy getFinalizerRetryPolicy() {
        return finalizerRetryPolicy;
    }

    public void setFinalizerRetryPolicy(RetryPolicy finalizerRetryPolicy) {
        this.finalizerRetryPolicy = finalizerRetryPolicy;
    }

    public RetryPolicy getOrchestratorRetryPolicy() {
        return orchestratorRetryPolicy;
    }

    public void setOrchestratorRetryPolicy(RetryPolicy orchestratorRetryPolicy) {
        this.orchestratorRetryPolicy = orchestratorRetryPolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowMetadata that = (WorkflowMetadata) o;
        return maxRecursionDepth == that.maxRecursionDepth &&
                maxPagesPerDepthLevel == that.maxPagesPerDepthLevel &&
                maxWebPageSizeBytes == that.maxWebPageSizeBytes &&
                maxConcurrentCrawlers == that.maxConcurrentCrawlers &&
                crawlerTimeoutSeconds == that.crawlerTimeoutSeconds &&
                Objects.equals(rootAddress, that.rootAddress) &&
                Objects.equals(urlMatcher, that.urlMatcher) &&
                Objects.equals(selectionPolicy, that.selectionPolicy) &&
                Objects.equals(crawlerRetryPolicy, that.crawlerRetryPolicy) &&
                Objects.equals(workerRetryPolicy, that.workerRetryPolicy) &&
                Objects.equals(orchestratorRetryPolicy, that.orchestratorRetryPolicy) &&
                Objects.equals(finalizerRetryPolicy, that.finalizerRetryPolicy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootAddress, urlMatcher, maxRecursionDepth, maxPagesPerDepthLevel, maxWebPageSizeBytes, maxConcurrentCrawlers, crawlerTimeoutSeconds, selectionPolicy, crawlerRetryPolicy, workerRetryPolicy, orchestratorRetryPolicy, finalizerRetryPolicy);
    }

    @Override
    public String toString() {
        return "WorkflowMetadata{" +
                "rootAddress='" + rootAddress + '\'' +
                ", urlMatcher='" + urlMatcher + '\'' +
                ", maxRecursionDepth=" + maxRecursionDepth +
                ", maxPagesPerDepthLevel=" + maxPagesPerDepthLevel +
                ", maxWebPageSizeBytes=" + maxWebPageSizeBytes +
                ", maxConcurrentCrawlers=" + maxConcurrentCrawlers +
                ", crawlerTimeoutSeconds=" + crawlerTimeoutSeconds +
                ", selectionPolicy=" + selectionPolicy +
                ", crawlerRetryPolicy=" + crawlerRetryPolicy +
                ", workerRetryPolicy=" + workerRetryPolicy +
                ", orchestratorRetryPolicy=" + orchestratorRetryPolicy +
                ", finalizerRetryPolicy=" + finalizerRetryPolicy +
                '}';
    }
}
