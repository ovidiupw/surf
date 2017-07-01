const workflowInitialState = {
  definition: undefined,
  executions: [],
  visitedPages: []
};

export function workflow(state = workflowInitialState, action) {

  switch (action.type) {

    case 'SAVE_WORKFLOW_TO_STATE':
      {
        console.log("Saving workflow to state...");

        return Object.assign({}, state, {
          definition: action.workflow
        });
      }

    case 'SAVE_WORKFLOW_EXECUTIONS_TO_STATE':
      {
        console.log("Saving workflow executions to state...");

        return Object.assign({}, state, {
          executions: action.executions
        });
      }

    case 'SAVE_VISITED_PAGES_TO_STATE':
      {
        console.log("Saving execution visited pages to state...");

        return Object.assign({}, state, {
          visitedPages: action.visitedPages
        });
      }

    default:
      {
        return state;
      }
  }
};
