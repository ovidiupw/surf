import {connect} from 'react-redux';
import App from 'components/App';
import Utility from 'modules/Utility';
import {hideErrorModal} from 'redux/actions/Modals';

function mapStateToProps(state, ownProps) {
  return {
    loggedIn: state.auth.loggedIn,
    errorModal: state.errorModal
  };
}

function mapDispatchToProps(dispatch, ownProps) {

  return {
    hideErrorModal: () => {
      dispatch(hideErrorModal());
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
