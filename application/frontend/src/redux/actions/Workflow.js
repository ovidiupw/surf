export function saveWorkflow(workflow) {
  return {
    type: 'SAVE_WORKFLOW_TO_STATE',
    workflow
  };
}

export function saveWorkflowExecutions(executions) {
  return {
    type: 'SAVE_WORKFLOW_EXECUTIONS_TO_STATE',
    executions
  };
}

export function saveVisitedPages(visitedPages) {
  return {
    type: 'SAVE_VISITED_PAGES_TO_STATE',
    visitedPages
  };
}
