package models.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import models.exceptions.InternalServerException;
import utils.RandomGenerator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class StateMachineExecutionDefinition {

    public static final String TASK_INPUTS = "stateInputs";

    private final StateMachineDefinition stateMachineDefinition;
    private final String id;

    public StateMachineExecutionDefinition(@Nonnull final StateMachineDefinition stateMachineDefinition) {
        Preconditions.checkNotNull(stateMachineDefinition);

        this.id = RandomGenerator.randomUUIDWithTimestamp();
        this.stateMachineDefinition = stateMachineDefinition;
    }


    public String buildExecutionInputJson() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(buildExecutionInput());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerException(e.getMessage());
        }
    }

    private Input buildExecutionInput() {
        final List<CrawlWebPageStateInput> crawlWebPageStateInputs = new ArrayList<>();

        for (final WorkflowTask workflowTask : stateMachineDefinition.getTasks()) {
            final CrawlWebPageStateInput input = new CrawlWebPageStateInput();
            input.setWorkflowExecutionId(workflowTask.getWorkflowExecutionId());
            input.setWorkflowTaskId(workflowTask.getId());
            input.setUrl(workflowTask.getUrl());
            input.setDepthLevel(workflowTask.getDepth());
            input.setMaxWebPageSizeBytes(workflowTask.getMaxWebPageSizeBytes());
            input.setUrlMatcher(workflowTask.getUrlMatcher());
            input.setSelectionPolicy(workflowTask.getSelectionPolicy());

            crawlWebPageStateInputs.add(input);
        }

        final StateMachineExecutionDefinition.Input input = new StateMachineExecutionDefinition.Input();
        input.setStateInputs(crawlWebPageStateInputs);
        return input;
    }

    public String getId() {
        return id;
    }

    public static class Input {
        @JsonProperty(TASK_INPUTS)
        private List<CrawlWebPageStateInput> stateInputs;

        public List<CrawlWebPageStateInput> getStateInputs() {
            return stateInputs;
        }

        public void setStateInputs(List<CrawlWebPageStateInput> stateInputs) {
            this.stateInputs = stateInputs;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "stateInputs=" + stateInputs +
                    '}';
        }
    }
}
