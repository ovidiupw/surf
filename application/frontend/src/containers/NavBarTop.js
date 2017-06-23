import {connect} from 'react-redux';
import NavBarTop from 'components/NavBarTop';
import Utility from 'modules/Utility';
import {deleteAuthDataFromState} from 'redux/actions/Auth';

function mapStateToProps(state, ownProps) {
  return {
    userName: state.auth.userName,
    loggedIn: state.auth.loggedIn
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;

  return {
    lifecycleMethods: {
      componentDidMount: () => {
        Utility.initializeApigClientFromLocalStorage(dispatch, routerHistory);
      }
    },

    handleLogout: function() {
      Utility.clearLocalStorageAuthData();
      FB.logout();
      dispatch(deleteAuthDataFromState());
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(NavBarTop);
