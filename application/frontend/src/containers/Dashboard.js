import {connect} from 'react-redux';
import Dashboard from 'components/Dashboard';
import {workersGet} from 'redux/actions/Api';
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
        console.log("Dashboard component did mount!");
        if (apigClient == null) {
          Utility.initializeApigClientFromLocalStorage(dispatch, routerHistory, Routes.DASHBOARD);
        }
      }
    },

    gotoHome: () => {
      routerHistory.push(Routes.HOME);
    },

    workersGet: (apigClient, message) => {
      dispatch(workersGet(apigClient, message));
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
