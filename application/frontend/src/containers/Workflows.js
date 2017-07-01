import {connect} from 'react-redux';
import Workflows from 'components/Workflows';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';
import {workflowsGet} from 'redux/actions/Api';
import {RESULTS_PER_PAGE} from 'constants/Configs';

function mapStateToProps(state, ownProps) {
  return {
    apigClient: state.auth.apigClient,
    workflows: state.workflows.list,
    lastWorkflowOnPage: state.workflows.lastWorkflowOnPage,
    workflowExecutionsCategorySpinnerActive: state.spinner.workflowExecutionsCategorySpinnerActive,
    workflowsCategorySpinnerActive: state.spinner.workflowsCategorySpinnerActive,
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;
  return {
    lifecycleMethods: {
      componentDidMount: (apigClient) => {
        console.log("Workflows component did mount!");
        if (apigClient == null) {
          Utility.initializeApigClientFromLocalStorage(
            dispatch, routerHistory, Routes.WORKFLOWS);
        }
      }
    },

    workflowsGet: (apigClient, lastWorkflowOnPage) => {
      let createdBefore = null;
      let startingWorkflowId = '';

      if (lastWorkflowOnPage != null) {
        createdBefore = lastWorkflowOnPage.creationDateMillis;
        startingWorkflowId = lastWorkflowOnPage.id;
      }

      let workflowsGetRequest = {
        resultsPerPage: RESULTS_PER_PAGE,
        createdBefore: createdBefore,
        startingWorkflowId: startingWorkflowId
      };
      dispatch(workflowsGet(apigClient, workflowsGetRequest));
    },

    gotoHome: () => {
      routerHistory.push(Routes.HOME);
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Workflows);
