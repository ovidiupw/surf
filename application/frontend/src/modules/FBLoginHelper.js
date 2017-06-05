import FBCredentials from 'modules/entities/FBCredentials';
import async from 'async';

let FBLoginHelper = (function() {

  let _loginWithFacebook = function(waterfallCallback) {
    async.waterfall([
      function(callback) {
        return _getCurrentUserCredentials(function(err, fbCredentials) {
          if (err) {
            return callback(err);
          } else {
            return callback(null, fbCredentials);
          }
        });
      },

      function(fbCredentials, callback) {
        if (fbCredentials != null) {
          return callback(null, fbCredentials);
        } else {
          return _doLogin(function(err, fbCredentials) {
            if (err) {
              return callback(err);
            } else {
              return callback(null, fbCredentials);
            }
          });
        }
      }

    ], function(err, fbCredentials) {
      waterfallCallback(err, fbCredentials);
    });

  };

  let _getCurrentUserCredentials = function(callback) {
    FB.getLoginStatus(function(response) {
      if (response.status === 'connected') {
        console.log("The user is already authenticated with facebook!");
        FB.api('/me', {
          fields: 'name'
        }, function(nameRequestResponse) {
          return callback(null, new FBCredentials(
            response.authResponse.userID,
            response.authResponse.accessToken,
            nameRequestResponse.name));
        });
      } else if (response.status === 'not_authorized') {
        return callback(new Error("The user did not authorize the app to access his/her data!"));
      } else {
        return callback(null, null);
      }
    });
  };

  let _doLogin = function(callback) {
    const fbPermissionsScope = {
      scope: ''
    };

    const fbResponseCallback = function(response) {
      if (response.authResponse) {
        console.log("Successfully logged in with facebook!");
        FB.api('/me', {
          fields: 'name'
        }, function(nameRequestResponse) {
          callback(null, new FBCredentials(response.authResponse.userID, response.authResponse.accessToken, nameRequestResponse.name));
          return;
        });
      } else {
        callback(new Error('User cancelled login or did not fully authorize!'));
        return;
      }
    };

    FB.login(fbResponseCallback, fbPermissionsScope);
  };

  return ({loginWithFacebook: _loginWithFacebook});

})();

export default FBLoginHelper;
