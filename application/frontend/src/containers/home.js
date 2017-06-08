import {connect} from 'react-redux';
import async from 'async';
import AWS from 'aws-sdk';
import Home from 'components/Home';
import FBLoginHelper from 'modules/FBLoginHelper';
import AWSCredentials from 'modules/entities/AWSCredentials';
import LocalStorageHelper from 'modules/LocalStorageHelper';
import {initializeApiGatewayClient, addUserDataToState} from 'redux/actions/auth';
import {showAuthSpinner, hideAuthSpinner} from 'redux/actions/spinner';
import AWSConfig from 'config/aws-config.json';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';

function mapStateToProps(state, ownProps) {
  return {showSpinner: state.spinner.showAuthSpinner};
}

function mapDispatchToProps(dispatch, ownProps) {
  let routerHistory = ownProps.history;

  return {
    lifecycleMethods: {
      componentDidMount: () => {
        Utility.initializeApigClientFromLocalStorage(dispatch, routerHistory);
      }
    },

    handleAuthViaFacebook: () => {
      dispatch(showAuthSpinner());
      console.log("Authenticating with facebook...");
      async.waterfall([

        function(callback) {
          console.log("Trying to log in with facebook...");
          FBLoginHelper.loginWithFacebook(function(err, fbCredentials) {
            if (err) {
              console.log(`Error while logging in with facebook: ${err}`);
              LocalStorageHelper.deleteFBCredentialsFromLocalStorage();
              return callback(err);
            } else {
              console.log(`Login with facebook success! FBCredentials: ${fbCredentials.toString()}`);
              LocalStorageHelper.saveFBCredentialsToLocalStorage(fbCredentials);
              return callback(null, fbCredentials);
            }
          });
        },

        function(fbCredentials, callback) {
          console.log("Checking to see if credentials from localstorage can be used...");
          let awsCredentialsFromLocalStorage = LocalStorageHelper.loadAWSCredentialsFromLocalStorage();

          if (awsCredentialsFromLocalStorage.hasAllFieldsSet() && awsCredentialsFromLocalStorage.willNotExpireInTheNextFiveMinutes()) {
            console.log("Found AWS credentials in localStorage that don't expire in the next ~5 mintues.");
            return callback(null, awsCredentialsFromLocalStorage, fbCredentials);
          }

          console.log("Credentials from localStorage cannot be used because they will expire shortly or are already expired");
          var stsAssumeRoleArgs = {
            RoleArn: AWSConfig['facebookWebIdentityBasicRoleArn'],
            RoleSessionName: fbCredentials.userId,
            WebIdentityToken: fbCredentials.accessToken,
            DurationSeconds: 3600,
            ProviderId: 'graph.facebook.com'
          };
          var sts = new AWS.STS();
          sts.assumeRoleWithWebIdentity(stsAssumeRoleArgs, function(err, data) {
            if (err) {
              return callback(err);
            } else {
              console.log("Received data from STS assume-role: ");
              console.log(data);
              let awsCredentials = new AWSCredentials({
                accessKey: data.Credentials.AccessKeyId,
                secretKey: data.Credentials.SecretAccessKey,
                sessionToken: data.Credentials.SessionToken,
                region: AWSConfig['awsClientRegion'],
                apiKey: AWSConfig['apiKey'],
                expiration: data.Credentials.Expiration
              });

              LocalStorageHelper.saveAWSCredentialsToLocalStorage(awsCredentials);

              return callback(null, awsCredentials, fbCredentials);
            }
          });
        }

      ], function(err, awsCredentials, fbCredentials) {
        dispatch(hideAuthSpinner());
        if (err) {
          console.log(err);
        } else {
          dispatch(initializeApiGatewayClient(awsCredentials));
          dispatch(addUserDataToState(fbCredentials.userId, fbCredentials.userName));

          console.log("Redirecting to " + Routes.DASHBOARD + "...");
          routerHistory.push(Routes.DASHBOARD);
        }
      });
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
