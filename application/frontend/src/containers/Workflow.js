import {connect} from 'react-redux';
import Workflow from 'components/Workflow';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';
import {
  workflowsIdGet,
  workflowsExecutionsPost,
  workflowsExecutionsGet
} from 'redux/actions/Api';

function mapStateToProps(state, ownProps) {
  return {
    apigClient: state.auth.apigClient,
    workflow: state.workflow.definition,
    workflowExecutions: state.workflow.executions,
    workflowId: ownProps.match.url.split("/").pop(),
    loadSpinnerVisible: state.spinner.workflowCategorySpinnerActive,
    startSpinnerVisible: state.spinner.workflowCategoryStartSpinnerActive,
    executionsSpinnerVisible: state.spinner.workflowExecutionsCategorySpinnerActive
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;
  return {
    lifecycleMethods: {
      componentDidMount: (apigClient) => {
        console.log("Workflow component did mount!");
        if (apigClient == null) {
          Utility.initializeApigClientFromLocalStorage(
            dispatch, routerHistory, ownProps.match.url);
        }
      }
    },

    gotoHome: () => {
      routerHistory.push(Routes.HOME);
    },

    getWorkflow: (apigClient, workflowId) => {
      dispatch(workflowsIdGet(apigClient, workflowId));
    },

    startWorkflow: (apigClient, workflowId) => {
      dispatch(workflowsExecutionsPost(apigClient, workflowId, function() {
        routerHistory.push(Routes.WORKFLOW_EXECUTIONS.replace(":id", workflowId));
      }));
    },

    getWorkflowExecutions: (apigClient, workflowId) => {
      dispatch(workflowsExecutionsGet(apigClient, workflowId));
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Workflow);
