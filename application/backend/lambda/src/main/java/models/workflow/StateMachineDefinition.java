package models.workflow;

import com.amazonaws.services.stepfunctions.builder.StateMachine;
import com.amazonaws.services.stepfunctions.builder.states.Branch;
import com.amazonaws.services.stepfunctions.builder.states.Catcher;
import com.amazonaws.services.stepfunctions.builder.states.Retrier;
import com.amazonaws.services.stepfunctions.builder.states.State;
import com.google.common.base.Preconditions;
import models.config.LambdaConfigurationConstants;
import utils.RandomGenerator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.amazonaws.services.stepfunctions.builder.StepFunctionBuilder.*;

public class StateMachineDefinition {

    private static final String PARALLEL_MAIN_STATE_NAME = "ParallelMainState";
    private static final String FINALIZE_CRAWLING_SESSION_STATE_NAME = "FinalizeCrawlingTask";

    private static final int MAX_STEP_FUNCTIONS_ACTIVITY_WORKERS = 200;
    private static final String SURF_CRAWLING_TASK = "SurfCrawlingTask";

    private String id;
    private final Workflow workflow;
    private final WorkflowExecution workflowExecution;
    private final List<WorkflowTask> tasks;
    private final LambdaConfigurationConstants config;

    public StateMachineDefinition(
            @Nonnull final Workflow workflow,
            @Nonnull final WorkflowExecution workflowExecution,
            @Nonnull final List<WorkflowTask> tasks,
            @Nonnull final LambdaConfigurationConstants config) {
        Preconditions.checkNotNull(workflow);
        Preconditions.checkNotNull(workflowExecution);
        Preconditions.checkNotNull(tasks);
        Preconditions.checkArgument(tasks.size() > 0, "The number of tasks in a StateMachineDefinition must be >= 0!");
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(config.getCrawlWebPageLambdaArn(), "The crawl-webpage Lambda ARN is required!");
        Preconditions.checkNotNull(config.getFinalizeCrawlSessionLambdaArn(), "The finalize-crawl-session Lambda ARN is required!");

        this.id = RandomGenerator.randomUUIDWithTimestamp();
        this.workflow = workflow;
        this.workflowExecution = workflowExecution;
        this.tasks = tasks;
        this.config = config;
    }

    public StateMachine getStateMachine() {

        final StateMachine.Builder stateMachineBuilder = stateMachine()
                .comment("Surf web-crawler state machine")
                .startAt(PARALLEL_MAIN_STATE_NAME)
                .state(PARALLEL_MAIN_STATE_NAME, buildMainParallelState())
                .state(FINALIZE_CRAWLING_SESSION_STATE_NAME, buildFinalizeCrawlingSessionState());
        return stateMachineBuilder.build();
    }

    private State.Builder buildFinalizeCrawlingSessionState() {
        return taskState()
                .comment("Surf finalize crawling session task")
                .resource(config.getFinalizeCrawlSessionLambdaArn())
                .timeoutSeconds((int) workflow.getMetadata().getCrawlerTimeoutSeconds())
                .retrier(buildFinalizeCrawlingSessionRetrier())
                .transition(end())
                .outputPath("$");
    }

    private Retrier.Builder buildFinalizeCrawlingSessionRetrier() {
        return retrier()
                .maxAttempts(3)
                .intervalSeconds(5)
                .backoffRate(1.2)
                .retryOnAllErrors();
    }

    private State.Builder buildMainParallelState() {
        int numberOfBranches = (int) Math.min(
                Math.min(MAX_STEP_FUNCTIONS_ACTIVITY_WORKERS, workflow.getMetadata().getMaxConcurrentCrawlers()),
                tasks.size()
        );

        final Branch.Builder[] branchBuilders = new Branch.Builder[numberOfBranches];
        for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
            branchBuilders[branchNumber] = buildBranchBuilder(branchNumber);
        }

        return parallelState()
                .branches(branchBuilders)
                .transition(next(FINALIZE_CRAWLING_SESSION_STATE_NAME))
                .outputPath("$")
                .catcher(buildCrawlWebPageTaskCatcher());
    }

    private Branch.Builder buildBranchBuilder(int branchNumber) {
        final String branchName = String.format("%s-%d", SURF_CRAWLING_TASK, branchNumber);

        return branch()
                .comment("Surf crawling task branch")
                .startAt(branchName)
                .state(branchName, taskState()
                        .inputPath(String.format("$.%s[%d]", StateMachineExecutionDefinition.STATE_INPUTS, branchNumber))
                        .comment(String.format("Surf crawling task with index='%s'", branchNumber))
                        .resource(config.getCrawlWebPageLambdaArn())
                        .timeoutSeconds((int) workflow.getMetadata().getCrawlerTimeoutSeconds())
                        .retriers(buildRetriers())
                        .outputPath("$")
                        .transition(end()));
    }

    private Catcher.Builder buildCrawlWebPageTaskCatcher() {
        return catcher()
                .catchAll()
                .resultPath(String.format("$.%s", StateMachineExecutionDefinition.ERROR_INFO))
                .transition(next(FINALIZE_CRAWLING_SESSION_STATE_NAME));
    }

    private Retrier.Builder[] buildRetriers() {
        final List<ExponentialBackoffRetrier> workflowRetriers = new ArrayList<>();
        workflowRetriers.addAll(workflow.getMetadata().getCrawlerRetryPolicy().getExponentialBackoffRetriers());

        final int numberOfRetriers = workflowRetriers.size();
        final Retrier.Builder[] retrierBuilders = new Retrier.Builder[numberOfRetriers];

        for (int retrierIndex = 0; retrierIndex < numberOfRetriers; retrierIndex++) {
            final ExponentialBackoffRetrier workflowRetrier = workflowRetriers.get(retrierIndex);

            if (workflowRetrier.getErrors() == null
                    || workflowRetrier.getErrors().size() < 1
                    || workflowRetrier.getRetryAllErrors()) {
                retrierBuilders[retrierIndex] = retrier()
                        .backoffRate(workflowRetrier.getBackoffRate())
                        .errorEquals()
                        .retryOnAllErrors()
                        .intervalSeconds(workflowRetrier.getIntervalSeconds())
                        .maxAttempts(workflowRetrier.getMaxAttempts());
            } else if (workflowRetrier.getErrors() != null && workflowRetrier.getErrors().size() > 0){
                final String[] errorsToRetry = (String[]) workflowRetrier.getErrors().toArray();

                retrierBuilders[retrierIndex] = retrier()
                        .backoffRate(workflowRetrier.getBackoffRate())
                        .errorEquals()
                        .errorEquals(errorsToRetry)
                        .intervalSeconds(workflowRetrier.getIntervalSeconds())
                        .maxAttempts(workflowRetrier.getMaxAttempts());
            }
        }

        return retrierBuilders;
    }


    public String getId() {
        return id;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public WorkflowExecution getWorkflowExecution() {
        return workflowExecution;
    }

    public List<WorkflowTask> getTasks() {
        return tasks;
    }

    public LambdaConfigurationConstants getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "StateMachineDefinition{" +
                "id='" + id + '\'' +
                ", workflow=" + workflow +
                ", workflowExecution=" + workflowExecution +
                ", tasks=" + tasks +
                ", config=" + config +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateMachineDefinition that = (StateMachineDefinition) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(workflow, that.workflow) &&
                Objects.equals(workflowExecution, that.workflowExecution) &&
                Objects.equals(tasks, that.tasks) &&
                Objects.equals(config, that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workflow, workflowExecution, tasks, config);
    }
}
