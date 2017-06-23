import {connect} from 'react-redux';
import NewWorkflow from 'components/NewWorkflow';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';
import {
  updateNewWorkflowDefinition,
  showCrawlerRetriersInputForm,
  hideCrawlerRetriersInputForm,
  toggleRetryAllErrorsTrueRadioButton,
  toggleRetryAllErrorsFalseRadioButton,
  updateFormErrorsToRetry,
  updateCurrentSelectedFormError,
  updateRetryFormRetryInterval,
  updateRetryFormBackoffRate,
  updateRetryFormMaximumAttempts,
  showCrawlerTextSelectorsInputForm,
  hideCrawlerTextSelectorsInputForm,
  showCrawlerCSSSelectorsInputForm,
  hideCrawlerCSSSelectorsInputForm,
} from 'redux/actions/NewWorkflow';
import {showErrorModal} from 'redux/actions/Modals';
import {workflowsPost} from 'redux/actions/Api';

function mapStateToProps(state, ownProps) {
  return {
    apigClient: state.auth.apigClient,
    newWorkflowCategorySpinnerActive: state.spinner.newWorkflowCategorySpinnerActive,
    newWorkflowDefinition: state.newWorkflow.definition,
    crawlerRetriersInputFormVisible: state.newWorkflow.crawlerRetriersInputFormVisible,
    textSelectorsInputFormVisible: state.newWorkflow.textSelectorsInputFormVisible,
    cssSelectorsInputFormVisible: state.newWorkflow.cssSelectorsInputFormVisible,
    retryAllErrorsTrueRadioButtonChecked: state.newWorkflow.retryAllErrors.trueRadioButtonChecked,
    retryAllErrorsFalseRadioButtonChecked: state.newWorkflow.retryAllErrors.falseRadioButtonChecked,
    formErrorsToRetry: state.newWorkflow.formErrorsToRetry,
    currentSelectedFormError: state.newWorkflow.currentSelectedFormError,
    formRetryInterval: state.newWorkflow.formRetryInterval,
    formRetryBackoffRate: state.newWorkflow.formRetryBackoffRate,
    formRetryMaximumAttempts: state.newWorkflow.formRetryMaximumAttempts
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;
  return {
    lifecycleMethods: {
      componentDidMount: (apigClient) => {
        console.log("New workflow component did mount!");
        if (apigClient == null) {
          Utility.initializeApigClientFromLocalStorage(
            dispatch, routerHistory, Routes.NEW_WORKFLOW);
        }
      }
    },

    showInputValidationError: (message) => {
      dispatch(showErrorModal(
        "Input validation error!",
        message
      ));
    },

    updateNewWorkflowDefinition: (definition) => {
      dispatch(updateNewWorkflowDefinition(definition));
    },

    updateFormErrorsToRetry: (formErrorsToRetry) => {
      dispatch(updateFormErrorsToRetry(formErrorsToRetry));
    },

    removeFormErrorToRetry: (formErrorsToRetry, key) => {
      delete formErrorsToRetry[key];
      dispatch(updateFormErrorsToRetry(formErrorsToRetry));
    },

    updateCurrentSelectedFormError: (error) => {
      dispatch(updateCurrentSelectedFormError(error));
    },

    showCrawlerRetriersInputForm: () => {
      dispatch(showCrawlerRetriersInputForm());
    },

    hideCrawlerRetriersInputForm: () => {
      dispatch(hideCrawlerRetriersInputForm());
    },

    toggleRetryAllErrorsTrueRadioButton: () => {
      dispatch(toggleRetryAllErrorsTrueRadioButton());
    },

    toggleRetryAllErrorsFalseRadioButton: () => {
      dispatch(toggleRetryAllErrorsFalseRadioButton());
    },

    removeExponentialBackoffRetrier: (workflowDefinition, key) => {
      let array = workflowDefinition.workflow.metadata.crawlerRetryPolicy.exponentialBackoffRetriers;
      delete array[key];
      array.splice(key, 1);
      dispatch(updateNewWorkflowDefinition(workflowDefinition));
    },

    updateRetryFormRetryInterval: (interval) => {
      dispatch(updateRetryFormRetryInterval(interval));
    },

    updateRetryFormBackoffRate: (backoffRate) => {
      dispatch(updateRetryFormBackoffRate(backoffRate));
    },

    updateRetryFormMaximumAttempts: (maxAttempts) => {
      dispatch(updateRetryFormMaximumAttempts(maxAttempts));
    },

    showCrawlerTextSelectorsInputForm: () => {
      dispatch(showCrawlerTextSelectorsInputForm());
    },

    hideCrawlerTextSelectorsInputForm: () => {
      dispatch(hideCrawlerTextSelectorsInputForm());
    },

    showCrawlerCSSSelectorsInputForm: () => {
      dispatch(showCrawlerCSSSelectorsInputForm());
    },

    hideCrawlerCSSSelectorsInputForm: () => {
      dispatch(hideCrawlerCSSSelectorsInputForm());
    },

    removeTextSelector: (workflowDefinition, key) => {
      let array = workflowDefinition.workflow.metadata.selectionPolicy.textSelectors;
      delete array[key];
      array.splice(key, 1);
      dispatch(updateNewWorkflowDefinition(workflowDefinition));
    },

    removeCSSSelector: (workflowDefinition, key) => {
      let array = workflowDefinition.workflow.metadata.selectionPolicy.cssSelectors;
      delete array[key];
      array.splice(key, 1);
      dispatch(updateNewWorkflowDefinition(workflowDefinition));
    },

    gotoHome: () => {
      routerHistory.push(Routes.HOME);
    },

    createWorkflow: (apigClient, workflowDefinition) => {
      dispatch(workflowsPost(apigClient, workflowDefinition));
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(NewWorkflow);
