import {connect} from 'react-redux';
import Dashboard from 'components/Dashboard';
import Utility from 'modules/Utility';

function mapStateToProps(state, ownProps) {
  return {};
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;

  return {
    lifecycleMethods: {
      componentDidMount: () => {
        Utility.initializeApigClientFromLocalStorage(dispatch, routerHistory);
      }
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
