package utils.aws.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.workflow.*;
import utils.Logger;
import utils.SurfObjectMother;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class DynamoDBOperationsHelper {
    private final Logger LOG;
    private final DynamoDBMapper dynamoDBMapper;

    public DynamoDBOperationsHelper(@Nonnull final AmazonDynamoDB dynamoClient, @Nonnull final Logger logger) {
        Preconditions.checkNotNull(dynamoClient, "The DynamoDB client was null!");
        dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        this.LOG = logger;
    }

    public QueryResultPage<Workflow> getWorkflowsByOwner(
            @Nonnull final String userArn,
            @Nullable final String startingWorkflowId,
            @Nullable Long createdBefore,
            @Nonnull final Integer resultsPerPage) {

        Preconditions.checkNotNull(
                userArn,
                "The 'userArn' cannot be null when trying to get workflows by owner!"
        );
        Preconditions.checkNotNull(
                resultsPerPage,
                "The 'resultsPerPage' cannot be null when trying to get workflows by owner!"
        );
        if (startingWorkflowId != null) {
            Preconditions.checkNotNull(
                    createdBefore,
                    "'startingWorkflowId' and 'createdBefore' must either be both not null or both null!"
            );
        }
        if (createdBefore != null) {
            Preconditions.checkNotNull(
                    startingWorkflowId,
                    "'startingWorkflowId' and 'createdBefore' must either be both not null or both null!"
            );
        }

        Map<String, AttributeValue> startKey = null;
        if (createdBefore != null && startingWorkflowId != null) {
            startKey = new HashMap<>(4);

            startKey.put(Workflow.DDB_ID, new AttributeValue().withS(startingWorkflowId));
            startKey.put(Workflow.DDB_OWNER_ID, new AttributeValue().withS(SurfObjectMother.getOwnerId(userArn)));
            startKey.put(Workflow.DDB_CREATION_DATE_MILLIS, new AttributeValue().withN(String.valueOf(createdBefore)));
        } else {
            createdBefore = System.currentTimeMillis();
        }

        final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LE)
                .withAttributeValueList(new AttributeValue().withN(String.valueOf(createdBefore)));

        final Workflow workflowModel = new Workflow();
        workflowModel.setId(startingWorkflowId);
        workflowModel.setOwnerId(SurfObjectMother.getOwnerId(userArn));

        final DynamoDBQueryExpression<Workflow> query = new DynamoDBQueryExpression<Workflow>()
                .withHashKeyValues(workflowModel)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withExclusiveStartKey(startKey)
                .withRangeKeyCondition(Workflow.DDB_CREATION_DATE_MILLIS, rangeKeyCondition)
                .withLimit(resultsPerPage);

        LOG.info("Trying to get workflows for ownerId='%s', using condition='%s' paginated with limit='%s' and startKey='%s'",
                workflowModel.getOwnerId(),
                rangeKeyCondition,
                resultsPerPage,
                startKey
        );
        final QueryResultPage<Workflow> workflowQueryResultPage = dynamoDBMapper.queryPage(Workflow.class, query);
        LOG.info("Successfully retrieved workflows from database!");
        return workflowQueryResultPage;
    }

    public Workflow saveWorkflow(@Nonnull final Workflow workflow) {
        Preconditions.checkNotNull(workflow, "The 'workflow' must be non-null when trying to save it to database!");

        LOG.info("Trying to save workflow='%s' to database....", workflow);
        dynamoDBMapper.save(workflow);
        LOG.info("Successfully saved workflow to database!");

        return getWorkflow(workflow.getId());
    }

    public Workflow getWorkflow(@Nonnull final String workflowId) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowId),
                "Cannot get workflow for which 'workflowId' is null or empty!"
        );

        final Workflow workflowModel = new Workflow();
        workflowModel.setId(workflowId);

        LOG.info("Trying to get Workflow with id='%s'", workflowModel.getId());
        final Workflow workflow = dynamoDBMapper.load(workflowModel);

        if (workflow == null) {
            LOG.warn("Found no workflow with id='%s' in database!", workflowModel.getId());
        } else {
            LOG.info("Successfully retrieved Workflow from database: '%s'", workflow);
        }
        return workflow;
    }

    public WorkflowExecution saveWorkflowExecution(@Nonnull final WorkflowExecution workflowExecution) {
        Preconditions.checkNotNull(workflowExecution, "The 'workflowExecution' must be non-null when trying to save it to database!");

        LOG.info("Trying to save workflowExecution='%s' to database....", workflowExecution);
        dynamoDBMapper.save(workflowExecution);
        LOG.info("Successfully saved workflowExecution to database!");

        return getWorkflowExecution(workflowExecution.getId());
    }

    public WorkflowExecution getWorkflowExecution(@Nonnull final String workflowExecutionId) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot get workflow execution for which 'workflowExecutionId' is null or empty!");

        final WorkflowExecution workflowExecutionModel = new WorkflowExecution();
        workflowExecutionModel.setId(workflowExecutionId);

        LOG.info("Trying to get inserted workflowExecution with id='%s' from database...", workflowExecutionModel.getId());
        WorkflowExecution workflowExecution = dynamoDBMapper.load(workflowExecutionModel);

        if (workflowExecution == null) {
            LOG.warn("Found no workflowExecution with id='%s' in database!", workflowExecutionModel.getId());
        } else {
            LOG.info("Successfully retrieved workflowExecution from database!");
        }

        return workflowExecution;

    }

    public WorkflowTask saveWorkflowTask(@Nonnull final WorkflowTask workflowTask) {
        Preconditions.checkNotNull(workflowTask, "The 'workflowTask' must be non-null when trying to save it to database!");

        LOG.info("Trying to save workflowTask='%s' to database....", workflowTask);
        dynamoDBMapper.save(workflowTask);
        LOG.info("Successfully saved workflowTask to database!");

        return getWorkflowTask(workflowTask.getId());
    }

    public WorkflowTask getWorkflowTask(@Nonnull final String workflowTaskId) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowTaskId),
                "Cannot get workflow task for which 'workflowTaskId' is null or empty!");

        final WorkflowTask workflowTaskModel = new WorkflowTask();
        workflowTaskModel.setId(workflowTaskId);

        LOG.info("Trying to get inserted workflowTask with id='%s' from database...", workflowTaskModel.getId());
        final WorkflowTask workflowTask = dynamoDBMapper.load(workflowTaskModel);

        if (workflowTask == null) {
            LOG.warn("Found no workflowTask with id='%s' in database!", workflowTaskModel.getId());
        } else {
            LOG.info("Successfully retrieved workflowTask from database!");
        }
        return workflowTask;
    }

    public Map<Status, List<WorkflowTask>> getTasksForEachStatus(
            @Nonnull final String workflowExecutionId,
            final long depthLevel) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(workflowExecutionId),
                "Workflow execution id cannot be empty when getting tasks for each status!"
        );
        Preconditions.checkArgument(
                depthLevel >= 0,
                "The depth level must be >= 0 when getting tasks for each status!"
        );

        LOG.info("Getting tasks for each Status for workflowExecutionId='%s' and depthLevel='%d'...",
                workflowExecutionId, depthLevel);

        final Map<Status, List<WorkflowTask>> tasksForStatus = new HashMap<>();
        for (final Status status : Status.values()) {
            tasksForStatus.put(status, getTasks(workflowExecutionId, status, (int) depthLevel));
        }
        return tasksForStatus;
    }

    public List<WorkflowTask> getTasks(
            @Nonnull final String workflowExecutionId,
            @Nonnull final Status taskStatus,
            final int taskDepthLevel) {
        return getTasks(workflowExecutionId, taskStatus, taskDepthLevel, Integer.MAX_VALUE);
    }

    public List<WorkflowTask> getTasks(
            @Nonnull final String workflowExecutionId,
            @Nonnull final Status taskStatus,
            final long taskDepthLevel,
            final long maxTasksToRetrieve) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot get tasks for which 'workflowExecutionId' is null or empty!");
        Preconditions.checkNotNull(
                taskStatus,
                "Cannot get tasks for which 'taskStatus' is null!"
        );
        Preconditions.checkArgument(
                taskDepthLevel >= 0,
                "A task's depth level must be >= 0!"
        );

        final WorkflowTask taskModel = new WorkflowTask();
        taskModel.setWorkflowExecutionId(workflowExecutionId);
        taskModel.setStatus(taskStatus);
        taskModel.setDepth(taskDepthLevel);

        final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(taskModel.getStatusAndDepth()));

        LOG.info("Getting workflow tasks based on taskModel='%s'", taskModel);
        LOG.info("Getting workflow tasks based on condition='%s'", rangeKeyCondition);
        LOG.info("Will use queryLimit='%s'", maxTasksToRetrieve);

        final DynamoDBQueryExpression<WorkflowTask> query = new DynamoDBQueryExpression<WorkflowTask>()
                .withHashKeyValues(taskModel)
                .withConsistentRead(false)
                .withRangeKeyCondition(WorkflowTask.DDB_STATUS_AND_DEPTH, rangeKeyCondition)
                .withLimit((int) maxTasksToRetrieve);

        final PaginatedQueryList<WorkflowTask> workflowTasks = dynamoDBMapper.query(WorkflowTask.class, query);

        for (final WorkflowTask workflowTask : workflowTasks) {
            LOG.info("Found '%s' task='%s'", taskStatus.getName(), workflowTask);
        }

        return workflowTasks;
    }

    public void addErrorToWorkflowExecution(
            @Nonnull final WorkflowExecution workflowExecution,
            @Nonnull final StepFunctionsError stepFunctionsError) {

        LOG.info("Trying to add StepFunctionsError to workflowExecution='%s', error='%s'",
                workflowExecution,
                stepFunctionsError
        );

        CrawlWebPageError.Cause cause = null;
        if (stepFunctionsError.getCause() != null) {
            cause = new CrawlWebPageError.Cause();
            cause.setStackTrace(stepFunctionsError.getCause().getStackTrace());
            cause.setErrorType(stepFunctionsError.getCause().getErrorType());
            cause.setErrorMessage(stepFunctionsError.getCause().getErrorMessage());
        }

        CrawlWebPageError crawlWebPageError = new CrawlWebPageError();
        crawlWebPageError.setCause(cause);
        crawlWebPageError.setError(stepFunctionsError.getError());

        addErrorToWorkflowExecution(workflowExecution, crawlWebPageError);
    }

    public void addErrorToWorkflowExecution(
            @Nonnull final WorkflowExecution workflowExecution,
            @Nonnull final CrawlWebPageError crawlWebPageError) {
        Preconditions.checkNotNull(workflowExecution);
        Preconditions.checkNotNull(crawlWebPageError);

        if (crawlWebPageError.getCause() != null) {
            /* Don't place error stack trace in database for now */
            //TODO place error stacktrace in s3
            crawlWebPageError.getCause().setStackTrace(null);
        }

        LOG.info("Trying to add CrawlWebPageError to workflowExecution='%s', error='%s'",
                workflowExecution,
                crawlWebPageError
        );

        Set<WorkflowExecutionFailure> executionFailures = workflowExecution.getExecutionFailures();
        if (executionFailures == null) {
            executionFailures = new HashSet<>();
        }
        final WorkflowExecutionFailure failure = SurfObjectMother.getWorkflowExecutionFailure(crawlWebPageError);
        executionFailures.add(failure);
        workflowExecution.setExecutionFailures(executionFailures);

        LOG.info("Workflow execution failures including current failure: '%s'", executionFailures);
        LOG.info("Adding workflow execution failure='%s' to workflow execution in database...", failure);
        saveWorkflowExecution(workflowExecution);
    }

    public VisitedPage saveVisitedPage(@Nonnull final VisitedPage visitedPage) {
        Preconditions.checkNotNull(
                visitedPage,
                "The 'visitedPage' must be non-null when trying to save it to database!"
        );

        LOG.info("Trying to save visitedPage='%s' to database...", visitedPage);
        dynamoDBMapper.save(visitedPage);
        LOG.info("Successfully saved visitedPage to database!");
        return visitedPage;
    }

    public VisitedPage getVisitedPage(@Nonnull final String workflowExecutionId,
                                      @Nonnull final String url) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot get page visit data for which 'workflowExecutionId' is null or empty!");
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(url),
                "Cannot get page visit data for which 'url' is null or empty!");

        final VisitedPage visitedPageModel = new VisitedPage();
        visitedPageModel.setWorkflowExecutionId(workflowExecutionId);
        visitedPageModel.setUrl(url);

        LOG.info("Trying to get visitedPage form database using model='%s'...", visitedPageModel);
        final VisitedPage visitedPage = dynamoDBMapper.load(visitedPageModel);


        if (visitedPage == null) {
            LOG.warn(
                    "Found no visitedPage with workflowExecutionId='%s' and url='%s' in the database!",
                    visitedPageModel.getWorkflowExecutionId(),
                    visitedPageModel.getUrl()
            );
        } else {
            LOG.info("Successfully retrieved visitedPage from database!");
        }

        return visitedPage;
    }

    public PageToBeVisited savePageToBeVisited(@Nonnull final PageToBeVisited pageToBeVisited) {
        Preconditions.checkNotNull(
                pageToBeVisited,
                "The 'pageToBeVisited' must be non-null when trying to save it to database!"
        );

        LOG.info("Trying to save pageToBeVisited='%s' to database...", pageToBeVisited);
        dynamoDBMapper.save(pageToBeVisited);
        LOG.info("Successfully saved pageToBeVisited to database!");
        return pageToBeVisited;
    }

    public PageToBeVisited getPageToBeVisited(
            @Nonnull final String workflowExecutionId,
            @Nonnull final String url) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot get page visit data for which 'workflowExecutionId' is null or empty!");
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(url),
                "Cannot get page visit data for which 'url' is null or empty!");

        final PageToBeVisited pageToBeVisitedModel = new PageToBeVisited();
        pageToBeVisitedModel.setWorkflowExecutionId(workflowExecutionId);
        pageToBeVisitedModel.setUrl(url);

        LOG.info("Trying to get pageToBeVisited form database using model='%s'...", pageToBeVisitedModel);
        final PageToBeVisited pageToBeVisited = dynamoDBMapper.load(pageToBeVisitedModel);

        if (pageToBeVisited == null) {
            LOG.warn(
                    "Found no pageToBeVisited with workflowExecutionId='%s' and url='%s' in the database!",
                    pageToBeVisitedModel.getWorkflowExecutionId(),
                    pageToBeVisitedModel.getUrl()
            );
        } else {
            LOG.info("Successfully retrieved pageToBeVisited from database!");
        }

        return pageToBeVisited;
    }

    public List<VisitedPage> listVisitedPages(@Nonnull final String workflowExecutionId, final long depthLevel) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot list visited pages for which 'workflowExecutionId' is null or empty!");

        final VisitedPage visitedPageModel = new VisitedPage();
        visitedPageModel.setWorkflowExecutionId(workflowExecutionId);
        visitedPageModel.setPageVisitDepth(depthLevel);

        LOG.info("Trying to get visited pages ath depth='%s' form database using model='%s'...",
                depthLevel, visitedPageModel);
        final DynamoDBQueryExpression<VisitedPage> query = new DynamoDBQueryExpression<VisitedPage>()
                .withHashKeyValues(visitedPageModel)
                .withRangeKeyCondition(
                        VisitedPage.DDB_PAGE_VISIT_DEPTH,
                        new Condition()
                                .withAttributeValueList(
                                        new AttributeValue().withN(String.valueOf(visitedPageModel.getPageVisitDepth())))
                                .withComparisonOperator(ComparisonOperator.EQ))
                .withIndexName(VisitedPage.DDB_PAGE_VISIT_DEPTH_LSI)
                .withConsistentRead(true);


        final PaginatedQueryList<VisitedPage> visitedPageList = dynamoDBMapper.query(VisitedPage.class, query);

        if (visitedPageList == null || visitedPageList.size() == 0) {
            LOG.warn(
                    "Found no pageVisitData with workflowExecutionId='%s' in the database!",
                    visitedPageModel.getWorkflowExecutionId()
            );
        } else {
            LOG.info("Successfully retrieved visitedPageList from database!");
            for (final VisitedPage visitedPage : visitedPageList) {
               LOG.info("Visited page: '%s'", visitedPage);
            }
        }

        return visitedPageList;
    }

    public List<PageToBeVisited> listPagesToBeVisited(@Nonnull final String workflowExecutionId) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(workflowExecutionId),
                "Cannot list pages to be visited for which 'workflowExecutionId' is null or empty!");

        final PageToBeVisited pageToBeVisitedModel = new PageToBeVisited();
        pageToBeVisitedModel.setWorkflowExecutionId(workflowExecutionId);

        LOG.info("Trying to get pages to be visited form database using model='%s'...", pageToBeVisitedModel);
        final DynamoDBQueryExpression<PageToBeVisited> query = new DynamoDBQueryExpression<PageToBeVisited>()
                .withHashKeyValues(pageToBeVisitedModel);

        List<PageToBeVisited> pagesToBeVisited = dynamoDBMapper.query(PageToBeVisited.class, query);

        if (pagesToBeVisited == null) {
            pagesToBeVisited = Collections.emptyList();
        }

        return pagesToBeVisited;
    }

    public void deleteWorkflowTask(@Nonnull final WorkflowTask workflowTask) {
        Preconditions.checkNotNull(workflowTask);

        LOG.info("Trying to delete task='%s'...", workflowTask);
        dynamoDBMapper.delete(workflowTask);
        LOG.info("Successfully deleted task!");
    }

    public void deletePageToBeVisited(@Nonnull final PageToBeVisited pageToBeVisited) {
        Preconditions.checkNotNull(pageToBeVisited);

        LOG.info("Trying to delete pageToBeVisited='%s'...", pageToBeVisited);
        dynamoDBMapper.delete(pageToBeVisited);
        LOG.info("Successfully deleted pageToBeVisited!");
    }

    public void deletePagesToBeVisited(@Nonnull final List<PageToBeVisited> pagesToBeVisited) {
        Preconditions.checkNotNull(pagesToBeVisited);

        LOG.info("Trying to delete '%s' pagesToBeVisited='%s'...", pagesToBeVisited.size(), pagesToBeVisited);
        final List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchDelete(pagesToBeVisited);
        for (DynamoDBMapper.FailedBatch failedBatch : failedBatches) {
            LOG.error("Failed batch: '%s'", failedBatch.getException().getMessage());
        }
        LOG.info("Successfully deleted pagesToBeVisited!");
    }

    public CrawlMetadata saveCrawlMetadata(@Nonnull final CrawlMetadata crawlMetadata) {
        Preconditions.checkNotNull(
                crawlMetadata,
                "The 'crawlMetadata' must be non-null when trying to save it to database!"
        );

        LOG.info("Trying to save crawlMetadata='%s' to database...", crawlMetadata);
        dynamoDBMapper.save(crawlMetadata);
        LOG.info("Successfully saved crawlMetadata to database!");
        return crawlMetadata;
    }
}
