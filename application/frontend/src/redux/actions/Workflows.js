export function saveWorkflows(workflows) {
  return {
    type: 'SAVE_WORKFLOWS_TO_STATE',
    workflows
  };
}
