import {connect} from 'react-redux';
import App from 'components/App';
import Utility from 'modules/Utility';
import {
  hideErrorModal,
  hideS3LinkModal
} from 'redux/actions/Modals';

function mapStateToProps(state, ownProps) {
  return {
    loggedIn: state.auth.loggedIn,
    errorModal: state.errorModal,
    s3LinkModal: state.s3LinkModal,
    s3LinkModalSpinnerActive: state.spinner.s3LinkModalSpinnerActive
  };
}

function mapDispatchToProps(dispatch, ownProps) {

  return {
    hideErrorModal: () => {
      dispatch(hideErrorModal());
    },
    hideS3LinkModal: () => {
      dispatch(hideS3LinkModal());
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
