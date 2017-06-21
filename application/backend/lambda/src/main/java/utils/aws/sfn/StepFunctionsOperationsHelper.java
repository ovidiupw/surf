package utils.aws.sfn;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import models.config.LambdaConfigurationConstants;
import models.workflow.StateMachineDefinition;
import models.workflow.StateMachineExecutionDefinition;
import utils.Logger;

import javax.annotation.Nonnull;

public class StepFunctionsOperationsHelper {
    private final AWSStepFunctions stepFunctionsClient;
    private final Logger LOG;
    private final LambdaConfigurationConstants config;

    public StepFunctionsOperationsHelper(
            @Nonnull final AWSStepFunctions stepFunctionsClient,
            @Nonnull final Logger logger,
            @Nonnull final LambdaConfigurationConstants config) {
        Preconditions.checkNotNull(stepFunctionsClient, "The StepFunctions client was null!");
        Preconditions.checkNotNull(logger);
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(config.getStepFunctionsInvokeLambdaRoleArn());

        this.stepFunctionsClient = stepFunctionsClient;
        this.LOG = logger;
        this.config = config;
    }

    public CreateStateMachineResult createStateMachine(
            @Nonnull final StateMachineDefinition stateMachineDefinition) {
        Preconditions.checkNotNull(stateMachineDefinition,
                "The state machine definition cannot be null when trying to create a state machine!");

        LOG.info("Trying to create state machine with definition='%s' and name='%s'...",
                stateMachineDefinition.getStateMachine().toJson(), stateMachineDefinition.getId());

        final CreateStateMachineRequest createStateMachineRequest = new CreateStateMachineRequest()
                .withDefinition(stateMachineDefinition.getStateMachine())
                .withName(stateMachineDefinition.getId())
                .withRoleArn(config.getStepFunctionsInvokeLambdaRoleArn());

        final CreateStateMachineResult stateMachine = stepFunctionsClient.createStateMachine(createStateMachineRequest);

        LOG.info("Successfully created state machine with stateMachineArn='%s'", stateMachine.getStateMachineArn());

        return stateMachine;
    }

    public StartExecutionResult startStateMachine(
            @Nonnull final CreateStateMachineResult stateMachine,
            @Nonnull final StateMachineExecutionDefinition executionDefinition) {
        Preconditions.checkNotNull(stateMachine,
                "The state machine cannot be null when trying to start a new execution!");
        Preconditions.checkNotNull(executionDefinition,
                "The state machine execution definition cannot be null when trying to start a new execution!");

        final String stateMachineExecInput = executionDefinition.buildExecutionInputJson();
        LOG.info("Trying to start state machine execution with input='%s'", stateMachineExecInput);

        final StartExecutionRequest startStateMachineRequest = new StartExecutionRequest()
                .withStateMachineArn(stateMachine.getStateMachineArn())
                .withName(executionDefinition.getId())
                .withInput(stateMachineExecInput);
        final StartExecutionResult startExecutionResult = stepFunctionsClient.startExecution(startStateMachineRequest);

        LOG.info("Successfully started state machine execution with arn='%s'!", startExecutionResult.getExecutionArn());

        return startExecutionResult;
    }


    public void deleteStateMachine(@Nonnull final String stateMachineArn) {
        Preconditions.checkArgument(
                !Strings.isNullOrEmpty(stateMachineArn),
                "State machine ARN cannot be empty or null when trying to delete it!"
        );

        LOG.info("Trying to delete state machine with ARN='%s'", stateMachineArn);
        stepFunctionsClient.deleteStateMachine(new DeleteStateMachineRequest()
                .withStateMachineArn(stateMachineArn));
        LOG.info("Successfully deleted state machine with ARN='%s'", stateMachineArn);
    }
}
