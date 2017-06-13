package surf.deployers.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import models.CrawlMetadata;
import models.Workflow;
import models.WorkflowExecution;
import models.WorkflowExecutionTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import surf.deployers.Deployer;
import surf.deployers.DeployerConfiguration;
import surf.deployment.Context;
import surf.exceptions.OperationFailedException;

import javax.annotation.Nonnull;
import java.util.List;

public class DynamoDeployer implements Deployer {

    private static final Logger LOG = LoggerFactory.getLogger(DynamoDeployer.class);

    private static final String DEPLOYER_NAME = "DynamoDBDeployer";

    private final DeployerConfiguration deployerConfiguration;

    @Inject
    public DynamoDeployer(@Nonnull final DeployerConfiguration deployerConfiguration) {
        Preconditions.checkNotNull(deployerConfiguration);
        this.deployerConfiguration = deployerConfiguration;
    }

    @Override
    public String getName() {
        return DEPLOYER_NAME;
    }

    @Override
    public Context deploy(@Nonnull final Context context) throws OperationFailedException {
        Preconditions.checkNotNull(context);
        final AmazonDynamoDB dynamoClient = initializeDynamoClient();

        final TableDescription workflowsTable = createWorkflowsTable(dynamoClient);
        final TableDescription workflowExecutionsTable = createWorkflowExecutionsTable(dynamoClient);
        final TableDescription workflowExecutionTasksTable = createWorkflowExecutionTasksTable(dynamoClient);
        final TableDescription crawlMetadataTable = createCrawlMetadataTable(dynamoClient);

        context.setWorkflowsDynamoDBTable(workflowsTable);
        context.setWorkflowExecutionsDynamoDBTable(workflowExecutionsTable);
        context.setWorkflowExecutionTasksDynamoDBTable(workflowExecutionTasksTable);
        context.setCrawlMetadataDynamoDBTable(crawlMetadataTable);

        return context;
    }

    private TableDescription createWorkflowsTable(@Nonnull final AmazonDynamoDB dynamoClient) {
        LOG.info("Trying to create DynamoDB table with name={}, readCapacityUnits={}, writeCapacityUnits={}, " +
                        "ownerGSIReadCapacityUnits={}, ownerGSIWriteCapacityUnits={}",
                Workflow.getTableName(),
                deployerConfiguration.getDynamoDBWorkflowsTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowsTableWriteCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits());

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Workflow.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(
                deployerConfiguration.getDynamoDBWorkflowsTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowsTableWriteCapacityUnits()
        ));

        final List<GlobalSecondaryIndex> globalSecondaryIndexes = createTableRequest.getGlobalSecondaryIndexes();
        for (final GlobalSecondaryIndex gsi : globalSecondaryIndexes) {
            switch (gsi.getIndexName()) {
                case Workflow.DDB_OWNER_GSI: {
                    gsi.setProvisionedThroughput(new ProvisionedThroughput(
                            deployerConfiguration.getDynamoDBWorkflowsTableOwnerGSIReadCapacityUnits(),
                            deployerConfiguration.getDynamoDBWorkflowsTableOwnerGSIWriteCapacityUnits()
                    ));
                    break;
                }
                default: {
                    throw new OperationFailedException(new UnsupportedOperationException("DDB GSI name not covered!"));
                }
            }
        }

        return createOrDescribeTable(dynamoClient, createTableRequest);
    }

    private TableDescription createWorkflowExecutionsTable(AmazonDynamoDB dynamoClient) {
        LOG.info("Trying to create DynamoDB table with name={}, readCapacityUnits={}, writeCapacityUnits={}",
                WorkflowExecution.getTableName(),
                deployerConfiguration.getDynamoDBWorkflowExecutionsTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowExecutionsTableWriteCapacityUnits());

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(WorkflowExecution.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(
                deployerConfiguration.getDynamoDBWorkflowExecutionsTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowExecutionsTableWriteCapacityUnits()
        ));

        return createOrDescribeTable(dynamoClient, createTableRequest);
    }

    private TableDescription createWorkflowExecutionTasksTable(AmazonDynamoDB dynamoClient) {
        LOG.info("Trying to create DynamoDB table with name={}, readCapacityUnits={}, writeCapacityUnits={}",
                WorkflowExecutionTask.getTableName(),
                deployerConfiguration.getDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits());

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(WorkflowExecutionTask.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(
                deployerConfiguration.getDynamoDBWorkflowExecutionTasksTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBWorkflowExecutionTasksTableWriteCapacityUnits()
        ));

        return createOrDescribeTable(dynamoClient, createTableRequest);
    }

    private TableDescription createCrawlMetadataTable(@Nonnull final AmazonDynamoDB dynamoClient) {
        LOG.info("Trying to create DynamoDB table with name={}, readCapacityUnits={}, writeCapacityUnits={}",
                CrawlMetadata.getTableName(),
                deployerConfiguration.getDynamoDBCrawlMetadataTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBCrawlMetadataTableWriteCapacityUnits());

        final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoClient);
        final CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(CrawlMetadata.class);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(
                deployerConfiguration.getDynamoDBCrawlMetadataTableReadCapacityUnits(),
                deployerConfiguration.getDynamoDBCrawlMetadataTableWriteCapacityUnits()
        ));

        return createOrDescribeTable(dynamoClient, createTableRequest);
    }

    private TableDescription createOrDescribeTable(@Nonnull final AmazonDynamoDB dynamoClient,
                                                   @Nonnull final CreateTableRequest createTableRequest) {
        try {
            LOG.info("Submitting the create table request to DynamoDB...");
            final CreateTableResult table = dynamoClient.createTable(createTableRequest);
            LOG.info("DynamoDB table with name={} was successfully created!", table.getTableDescription().getTableName());
            return table.getTableDescription();
        } catch (ResourceInUseException e) {
            LOG.error("!!! Create failed! The DynamoDB table with name={} cannot be created because it already exists." +
                            " If the deployer should recreate the table, please delete it first and re-execute" +
                            " the deployer.  !!!",
                    createTableRequest.getTableName());
            LOG.info("Continuing the deployment by trying to describe the existing table.");
            return describeTable(dynamoClient, createTableRequest.getTableName());

        } catch (LimitExceededException
                | InternalServerErrorException e) {
            LOG.error("Create failed!", e);
            throw new OperationFailedException(e);
        }
    }

    private TableDescription describeTable(
            @Nonnull final AmazonDynamoDB dynamoClient,
            @Nonnull final String tableName) {
        LOG.info("Trying to describe the table with name={}", tableName);

        try {
            return dynamoClient.describeTable(tableName).getTable();
        } catch (ResourceNotFoundException
                | InternalServerErrorException e) {
            LOG.error("Error while trying to describe existing DynamoDB table!", e);
            throw new OperationFailedException(e);
        }
    }

    private AmazonDynamoDB initializeDynamoClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withClientConfiguration(deployerConfiguration.getClientConfiguration())
                .withRegion(deployerConfiguration.getAwsClientRegion())
                .build();
    }
}
