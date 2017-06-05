import {connect} from 'react-redux';
import Home from 'components/home';
import FBLoginHelper from 'modules/FBLoginHelper';
import LocalStorageHelper from 'modules/LocalStorageHelper';
//import {authenticateWithFacebook} from 'redux/actions/facebookAuth'; TODO

function mapStateToProps(state, ownProps) {
  return {
    lifecycleMethods: {
      componentDidMount: () => {
        console.log("Home component successfully mounted!");
      }
    }
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  return {
    handleAuthViaFacebook: (e) => {
      e.preventDefault();
      console.log("Trying to login with facebook...");

      FBLoginHelper.loginWithFacebook(function(err, fbCredentials) {
        if (err) {
          console.log(`Error while logging in with facebook: ${err}`);
          LocalStorageHelper.deleteFBCredentialsFromLocalStorage();
        } else {
          console.log(`Login with facebook success! FBCredentials: ${fbCredentials.toString()}`);
          LocalStorageHelper.saveFBCredentialsToLocalStorage(fbCredentials);
        }
      });
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
