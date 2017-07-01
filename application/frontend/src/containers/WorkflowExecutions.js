import {connect} from 'react-redux';
import WorkflowExecutions from 'components/WorkflowExecutions';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';
import {
  workflowsExecutionsIdGet,
  workflowsExecutionsIdDataGet
} from 'redux/actions/Api';

function mapStateToProps(state, ownProps) {
  console.log("HERHERUEHRUEH")
  console.log(ownProps.match.params.id)
  return {
    apigClient: state.auth.apigClient,
    visitedPagesSpinnerActive: state.spinner.visitedPagesSpinnerActive,
    workflowId: ownProps.match.params.workflow_id,
    workflowExecutionId: ownProps.match.params.id,
    visitedPages: state.workflow.visitedPages,
    workflow: state.workflow.definition
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;
  return {
    lifecycleMethods: {
      componentDidMount: (apigClient) => {
        console.log("WorkflowExecutions component did mount!");
        if (apigClient == null) {
          Utility.initializeApigClientFromLocalStorage(dispatch, routerHistory, Routes.WORKFLOW_EXECUTIONS);
        }
      }
    },

    gotoHome: () => {
      routerHistory.push(Routes.HOME);
    },

    getVisitedPages: (apigClient, workflowExecutionId) => {
      dispatch(workflowsExecutionsIdGet(apigClient, workflowExecutionId));
    },

    generateS3Link: (apigClient, url, workflowExecutionId) => {
      dispatch(workflowsExecutionsIdDataGet(apigClient, workflowExecutionId, url));
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(WorkflowExecutions);
