const initialState = {
  showAuthSpinner: false,
  workflowsCategorySpinnerActive: false,
  workflowExecutionsCategorySpinnerActive: false,
  newWorkflowCategorySpinnerActive: false
};

export function spinner(state = initialState, action) {
  switch (action.type) {
    case 'SHOW_AUTH_SPINNER': {
      return Object.assign({}, state, {
        showAuthSpinner: true
      });
    }

    case 'HIDE_AUTH_SPINNER': {
      return Object.assign({}, state, {
        showAuthSpinner: false
      });
    }

    case 'SHOW_WORKFLOWS_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        workflowsCategorySpinnerActive: true
      });
    }

    case 'HIDE_WORKFLOWS_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        workflowsCategorySpinnerActive: false
      });
    }

    case 'SHOW_WORKFLOW_EXECUTIONS_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        workflowExecutionsCategorySpinnerActive: true
      });
    }

    case 'HIDE_WORKFLOW_EXECUTIONS_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        workflowExecutionsCategorySpinnerActive: false
      });
    }

    case 'SHOW_NEW_WORKFLOW_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        newWorkflowCategorySpinnerActive: true
      });
    }

    case 'HIDE_NEW_WORKFLOW_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        newWorkflowCategorySpinnerActive: false
      });
    }

    default: {
      return state;
    }
  };
}
