import LocalStorageHelper from 'modules/LocalStorageHelper';
import Utility from 'modules/Utility';

import {
  saveWorkflows
} from 'redux/actions/Workflows';
import {
  saveWorkflow,
  saveWorkflowExecutions,
  saveVisitedPages
} from 'redux/actions/Workflow';
import {
  showWorkflowsCategorySpinner,
  hideWorkflowsCategorySpinner,
  showNewWorkflowCategorySpinner,
  hideNewWorkflowCategorySpinner,
  showWorkflowCategorySpinner,
  hideWorkflowCategorySpinner,
  showWorkflowCategoryStartSpinner,
  hideWorkflowCategoryStartSpinner,
  showWorkflowExecutionsCategorySpinner,
  hideWorkflowExecutionsCategorySpinner,
  showVisitedPagesSpinner,
  hideVisitedPagesSpinner,
  showS3LinkModalSpinner,
  hideS3LinkModalSpinner
} from 'redux/actions/Spinner';
import {
  showErrorModal,
  hideS3LinkModal,
  showS3LinkModal,
  updateS3LinkModalLinks
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
          "Unexpected error while trying to list workflows! Please log out and log in again!"
        ));
      }
    }).then(function() {
      dispatch(hideWorkflowsCategorySpinner());
    });
  };
}

export function workflowsIdGet(apigClient, workflowId) {
  return dispatch => {
    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      AuthDetails: {
        userId: fbCredentials.userId,
        accessToken: fbCredentials.accessToken
      },
      id: workflowId
    };

    const body = {};

    const additionalParams = {
      headers: {},
      queryParams: {}
    };

    console.log(params);
    console.log(body);
    console.log(additionalParams);

    dispatch(showWorkflowCategorySpinner());
    apigClient.workflowsIdGet(params, body, additionalParams).then(function(result) {
      console.log("Successfully retrieved workflow!");
      console.log(result.data.workflow);
      dispatch(saveWorkflow(result.data.workflow));
    }).catch(function(result) {
      console.log("Error while trying to retrieve workflow!");
      console.log(result);
      try {
        dispatch(showErrorModal(
          "Error while trying retrieve workflow!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to retrieve workflow!",
          "Unexpected error while trying to retrieve workflow! Please log out and log in again!"
        ));
      }
    }).then(function() {
      dispatch(hideWorkflowCategorySpinner());
    });
  };
}

export function workflowsPost(apigClient, workflowDefinition, redirectCallback) {
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
      console.log(result.data.workflow);
      redirectCallback();
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
          "Unexpected error while trying to create workflow! Please log out and log in again!"
        ));
      }
    }).then(function() {
      dispatch(hideNewWorkflowCategorySpinner());
    });
  };
}

export function workflowsExecutionsPost(apigClient, workflowId, redirectCallback) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      AuthDetails: {
        userId: fbCredentials.userId,
        accessToken: fbCredentials.accessToken
      }
    };

    const body = {
      workflowId: workflowId
    };

    const additionalParams = {
      headers: {},
      queryParams: {}
    };

    console.log(params);
    console.log(body);
    console.log(additionalParams);

    dispatch(showWorkflowCategoryStartSpinner());
    apigClient.workflowsExecutionsPost(params, body, additionalParams).then(function(result) {
      console.log("Successfully started workflow!");
      console.log(result.data.workflowExecution);
      redirectCallback();
    }).catch(function(result) {
      console.log("Error while trying to start workflow!");
      console.log(result);
      try {
        dispatch(showErrorModal(
          "Error while trying start workflow!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to start workflow!",
          "Unexpected error while trying to start workflow! Please log out and log in again!"
        ));
      }
    }).then(function() {
      dispatch(hideWorkflowCategoryStartSpinner());
    });
  };
}

export function workflowsExecutionsGet(apigClient, workflowId) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      workflowId: workflowId,

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

    console.log(params);
    console.log(body);
    console.log(additionalParams);

    dispatch(showWorkflowExecutionsCategorySpinner());
    apigClient.workflowsExecutionsGet(params, body, additionalParams).then(function(result) {
      console.log("Successfully retrieved workflow executions!");
      console.log(result.data.workflowExecutions);
      dispatch(saveWorkflowExecutions(result.data.workflowExecutions));
    }).catch(function(result) {
      console.log("Error while trying to retrieve workflow executions!");
      console.log(result);
      try {
        dispatch(showErrorModal(
          "Error while trying retrieve workflow executions!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to retrieve workflow executions!",
          "Unexpected error while trying to retrieve the workflow executions! Please log out and log in again!"
        ));
      }
    }).then(function() {
      dispatch(hideWorkflowExecutionsCategorySpinner());
    });
  };
}

export function workflowsExecutionsIdGet(apigClient, workflowExecutionId) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      id: workflowExecutionId,

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

    console.log(params);
    console.log(body);
    console.log(additionalParams);

    dispatch(showVisitedPagesSpinner());
    apigClient.workflowsExecutionsIdGet(params, body, additionalParams).then(function(result) {
      console.log("Successfully retrieved execution details!");
      console.log(result.data.visitedPages);
      dispatch(saveVisitedPages(result.data.visitedPages));
    }).catch(function(result) {
      console.log("Error while trying to retrieve execution details!");
      console.log(result);
      try {
        dispatch(showErrorModal(
          "Error while trying retrieve execution details!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to retrieve execution details!",
          "Unexpected error while trying to retrieve the workflow execution's details! Please log out and log in again!"
        ));
      }
    }).then(function() {
      dispatch(hideVisitedPagesSpinner());
    });
  };
}

export function workflowsExecutionsIdDataGet(apigClient, workflowExecutionId, url) {
  return dispatch => {

    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    const params = {
      id: workflowExecutionId,
      pageUrl: url,

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

    console.log(params);
    console.log(body);
    console.log(additionalParams);

    dispatch(showS3LinkModal());
    dispatch(showS3LinkModalSpinner());
    apigClient.workflowsExecutionsIdDataGet(params, body, additionalParams).then(function(result) {
      console.log("Successfully generated S3 links!");
      console.log(result.data.s3CSSUrl);
      console.log(result.data.s3TextUrl);
      dispatch(updateS3LinkModalLinks(result.data.s3CSSUrl, result.data.s3TextUrl));
      dispatch(hideS3LinkModalSpinner());
    }).catch(function(result) {
      console.log("Error while trying to generate S3 link!");
      console.log(result);

      dispatch(hideS3LinkModal());
      dispatch(hideS3LinkModalSpinner());
      try {
        dispatch(showErrorModal(
          "Error while trying to generate S3 link!",
          Utility.getPrettyErrorComponent(result.data, result.status)
        ));
      } catch (Error) {
        dispatch(showErrorModal(
          "Error while trying to generate S3 link!",
          "Unexpected error while trying to generate S3 link! Please log out and log in again!"
        ));
      }
    });
  };
}
