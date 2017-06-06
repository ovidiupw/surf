import {connect} from 'react-redux';
import async from 'async';
import AWS from 'aws-sdk';
import Home from 'components/home';
import FBLoginHelper from 'modules/FBLoginHelper';
import AWSCredentials from 'modules/entities/AWSCredentials';
import LocalStorageHelper from 'modules/LocalStorageHelper';
import {initializeApiGatewayClient} from 'redux/actions/auth';
import AWSConfig from 'config/aws-config.json';

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

      async.waterfall([

        function(callback) {
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
              callback(err);
            }
            else {
              console.log(data);
              let awsCredentials = new AWSCredentials({
                accessKey: data.Credentials.AccessKeyId,
                secretKey: data.Credentials.SecretAccessKey,
                sessionToken: data.Credentials.SessionToken,
                region: AWSConfig['awsClientRegion'],
                apiKey: AWSConfig['apiKey'],
                expiration: data.Credentials.Expiration
              });
              callback(null, awsCredentials);
            }
          });
        }

      ], function(err, awsCredentials) {
        if (err) {
          console.log(err);
        } else {
          dispatch(initializeApiGatewayClient(awsCredentials));
        }
      });
    }
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
