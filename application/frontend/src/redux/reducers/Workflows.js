const workflowsInitialState = {
  list: undefined,
  lastWorkflowOnPage: undefined
};

export function workflows(state = workflowsInitialState, action) {

  switch (action.type) {

    case 'SAVE_WORKFLOWS_TO_STATE':
      {
        console.log("Saving workflows to state...");

        return Object.assign({}, state, {
          list: action.workflows,
          lastWorkflowOnPage: action.workflows.length === 0
            ? null
            : action.workflows[action.workflows.length - 1]
        });
      }

    default:
      {
        return state;
      }
  }
};
