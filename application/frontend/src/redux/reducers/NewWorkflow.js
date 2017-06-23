import * as AWSStepFunctions from 'constants/AWSStepFunctions';

const initialState = {
  definition: {
    workflow: {
      name: "",
      metadata: {
        rootAddress: "",
        urlMatcher: "",
        maxPagesPerDepthLevel: "",
        maxWebPageSizeBytes: "",
        maxConcurrentCrawlers: "",
        maxRecursionDepth: "",
        crawlerTimeoutSeconds: "",
        crawlerRetryPolicy: {
          exponentialBackoffRetriers: []
        },
        selectionPolicy: {
          textSelectors: [],
          cssSelectors: []
        }
      }
    }
  },
  crawlerRetriersInputFormVisible: false,
  textSelectorsInputFormVisible: false,
  cssSelectorsInputFormVisible: false,
  retryAllErrors: {
    trueRadioButtonChecked: true,
    falseRadioButtonChecked: false
  },
  formErrorsToRetry: [],
  formRetryInterval: undefined,
  formRetryBackoffRate: undefined,
  formRetryMaximumAttempts: undefined,
  currentSelectedFormError: null
};

export function newWorkflow(state = initialState, action) {
  switch (action.type) {
    case 'UPDATE_NEW_WORKFLOW_DEFINTION':
      {
        return Object.assign({}, state, {definition: action.newWorkflowDefinition});
      }

    case 'UPDATE_FORM_ERRORS_TO_RETRY':
      {
        return Object.assign({}, state, {formErrorsToRetry: action.formErrors});
      }

    case 'UPDATE_CURRENT_SELECTED_FORM_ERROR':
      {
        return Object.assign({}, state, {currentSelectedFormError: action.error});
      }

    case 'SHOW_CRAWLER_RETRIERS_INPUT_FORM':
      {
        return Object.assign({}, state, {crawlerRetriersInputFormVisible: true});
      }

    case 'HIDE_CRAWLER_RETRIERS_INPUT_FORM':
      {
        return Object.assign({}, state, {crawlerRetriersInputFormVisible: false});
      }

    case 'TOGGLE_RETRY_ALL_ERRORS_TRUE_RADIO_BUTTON':
      {
        let retryAllErrors = Object.assign({}, state.retryAllErrors, {
          trueRadioButtonChecked: !state.retryAllErrors.trueRadioButtonChecked
        });

        return Object.assign({}, state, {retryAllErrors: retryAllErrors});
      }

    case 'TOGGLE_RETRY_ALL_ERRORS_FALSE_RADIO_BUTTON':
      {
        let retryAllErrors = Object.assign({}, state.retryAllErrors, {
          falseRadioButtonChecked: !state.retryAllErrors.falseRadioButtonChecked
        });

        return Object.assign({}, state, {retryAllErrors: retryAllErrors});
      }

    case 'UPDATE_RETRY_FORM_RETRY_INTERVAL':
      {
        return Object.assign({}, state, {formRetryInterval: action.formRetryInterval});
      }

    case 'UPDATE_RETRY_FORM_BACKOFF_RATE':
      {
        return Object.assign({}, state, {formRetryBackoffRate: action.formRetryBackoffRate});
      }

    case 'UPDATE_RETRY_FORM_MAXIMUM_ATTEMPTS':
      {
        return Object.assign({}, state, {formRetryMaximumAttempts: action.formRetryMaximumAttempts});
      }

    case 'SHOW_CRAWLER_TEXT_SELECTORS_INPUT_FORM':
      {
        return Object.assign({}, state, {textSelectorsInputFormVisible: true});
      }

    case 'HIDE_CRAWLER_TEXT_SELECTORS_INPUT_FORM':
      {
        return Object.assign({}, state, {textSelectorsInputFormVisible: false});
      }

    case 'SHOW_CRAWLER_CSS_SELECTORS_INPUT_FORM':
      {
        return Object.assign({}, state, {cssSelectorsInputFormVisible: true});
      }

    case 'HIDE_CRAWLER_CSS_SELECTORS_INPUT_FORM':
      {
        return Object.assign({}, state, {cssSelectorsInputFormVisible: false});
      }

    default:
      {
        return state;
      }
  };
}
