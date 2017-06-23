import LocalStorageHelper from 'modules/LocalStorageHelper';
import Utility from 'modules/Utility';

import {
  saveWorkflows
} from 'redux/actions/Workflows';
import {
  showWorkflowsCategorySpinner,
  hideWorkflowsCategorySpinner,
  showNewWorkflowCategorySpinner,
  hideNewWorkflowCategorySpinner
} from 'redux/actions/Spinner';
import {
  showErrorModal
} from 'redux/actions/Modals';

export function workersGet(apigClient, message) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      message: message,

      AuthDetails: {
        userId: fbCredentials.userId,
        accessToken: fbCredentials.accessToken
      }
    };

    const body = {};

    const additionalParams = {
      headers: {},
      queryParams: {}
    };

    apigClient.workersGet(params, body, additionalParams).then(function(result) {
      console.log(result);
    }).catch(function(result) {
      console.log(result);
    });
  };
}

export function workflowsGet(apigClient, request) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      resultsPerPage: encodeURIComponent(request.resultsPerPage),
      createdBefore: encodeURIComponent(request.createdBefore),
      startingWorkflowId: encodeURIComponent(request.startingWorkflowId),
      AuthDetails: {
        userId: fbCredentials.userId,
        accessToken: fbCredentials.accessToken
      }
    };

    const body = {};

    const additionalParams = {
      headers: {},
      queryParams: {
      }
    };

    console.log(params);
    console.log(additionalParams);

    dispatch(showWorkflowsCategorySpinner());
    apigClient.workflowsGet(params, body, additionalParams).then(function(result) {
      console.log("Successfully listed workflows!");
      console.log(result);

      dispatch(saveWorkflows(result.data.workflows));
    }).catch(function(result) {
      console.log("Error while trying to list workflows!");
      console.log(result);

      try {
        dispatch(showErrorModal(
          "Error while trying to list workflows!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to list workflows!",
          "Unexpected error while trying to list workflows!"
        ));
      }
    }).then(function() {
      dispatch(hideWorkflowsCategorySpinner());
    });
  };
}

export function workflowsPost(apigClient, workflowDefinition) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      AuthDetails: {
        userId: fbCredentials.userId,
        accessToken: fbCredentials.accessToken
      }
    };

    const body = JSON.stringify(workflowDefinition.workflow);

    const additionalParams = {
      headers: {},
      queryParams: {}
    };

    console.log(params);
    console.log(body);
    console.log(additionalParams);

    dispatch(showNewWorkflowCategorySpinner());
    apigClient.workflowsPost(params, body, additionalParams).then(function(result) {
      console.log("Successfully created workflow!");
      console.log(result);
    }).catch(function(result) {
      console.log("Error while trying to create workflow!");
      console.log(result);

      try {
        dispatch(showErrorModal(
          "Error while trying create workflow!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to create workflow!",
          "Unexpected error while trying to create workflow!"
        ));
      }
    }).then(function() {
      dispatch(hideNewWorkflowCategorySpinner());
    });
  };
}
