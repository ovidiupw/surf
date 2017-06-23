import {connect} from 'react-redux';
import WorkflowExecutions from 'components/WorkflowExecutions';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';

function mapStateToProps(state, ownProps) {
  return {apigClient: state.auth.apigClient};
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
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(WorkflowExecutions);
