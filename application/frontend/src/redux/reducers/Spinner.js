const initialState = {
  showAuthSpinner: false,
  workflowsCategorySpinnerActive: false,
  workflowExecutionsCategorySpinnerActive: false,
  newWorkflowCategorySpinnerActive: false,
  workflowCategorySpinnerActive: false,
  workflowCategoryStartSpinnerActive: false,
  visitedPagesSpinnerActive: false,
  s3LinkModalSpinnerActive: false
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

    case 'SHOW_WORKFLOW_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        workflowCategorySpinnerActive: true
      });
    }

    case 'HIDE_WORKFLOW_CATEGORY_SPINNER': {
      return Object.assign({}, state, {
        workflowCategorySpinnerActive: false
      });
    }

    case 'SHOW_WORKFLOW_CATEGORY_START_SPINNER': {
      return Object.assign({}, state, {
        workflowCategoryStartSpinnerActive: true
      });
    }

    case 'HIDE_WORKFLOW_CATEGORY_START_SPINNER': {
      return Object.assign({}, state, {
        workflowCategoryStartSpinnerActive: false
      });
    }

    case 'SHOW_VISITED_PAGES_SPINNER': {
      return Object.assign({}, state, {
        visitedPagesSpinnerActive: true
      });
    }

    case 'HIDE_VISITED_PAGES_SPINNER': {
      return Object.assign({}, state, {
        visitedPagesSpinnerActive: false
      });
    }

    case 'SHOW_S3_LINK_MODAL_SPINNER': {
      return Object.assign({}, state, {
        s3LinkModalSpinnerActive: true
      });
    }

    case 'HIDE_S3_LINK_MODAL_SPINNER': {
      return Object.assign({}, state, {
        s3LinkModalSpinnerActive: false
      });
    }

    default: {
      return state;
    }
  };
}
