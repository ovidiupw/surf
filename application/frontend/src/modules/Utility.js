import LocalStorageHelper from 'modules/LocalStorageHelper';
import {initializeApiGatewayClient, addUserDataToState} from 'redux/actions/Auth';
import Routes from 'constants/Routes';
import React from 'react';
import * as AWSStepFunctions from 'constants/AWSStepFunctions';

let Utility = {

  getStepFunctionsErrorsForSelect() {
    console.log(AWSStepFunctions);
    return ([
      { label: 'All errors',    value: AWSStepFunctions.STATES_ALL },
      { label: 'Timeouts',  value: AWSStepFunctions.STATES_TIMEOUT },
      { label: 'Failed Tasks', value: AWSStepFunctions.STATES_TASK_FAILED },
      { label: 'Permissions errors',    value: AWSStepFunctions.STATES_PERMISSIONS}
    ]);
  },

  getPrettyErrorComponent(errorResponse, errorCode) {
    return (
      <div>
        <h4>Error message</h4>
        <p>{errorResponse.errorMessage}</p>
        <h4>Error type</h4>
        <p>{errorResponse.errorType}</p>
        <h4>Error code</h4>
        <p>{errorCode}</p>
        <h4>Error stack trace</h4>
        <p>{errorResponse.stackTrace.toString()}</p>
      </div>
    );
  },

  initFacebook: function() {
    FB.init({appId: '1964336457119734', xfbml: true, version: 'v2.9'});
    FB.AppEvents.logPageView();
    (function(d, s, id) {
      var js,
        fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) {
        return;
      }
      js = d.createElement(s);
      js.id = id;
      js.src = "//connect.facebook.net/en_US/sdk.js";
      fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
  },

  requireApiGatewayResources: function() {
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/axios/dist/axios.standalone.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/CryptoJS/rollups/hmac-sha256.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/CryptoJS/rollups/sha256.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/CryptoJS/components/hmac.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/CryptoJS/components/enc-base64.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/url-template/url-template.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/apiGatewayCore/sigV4Client.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/apiGatewayCore/apiGatewayClient.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/apiGatewayCore/simpleHttpClient.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/lib/apiGatewayCore/utils.js');
    require('!!script-loader!sdks/api-gateway-js-sdk/apiGateway-js-sdk/apigClient.js');
  },

  requireStyles: function() {
    require('bootstrap/dist/css/bootstrap.css');
    require('bootstrap/dist/css/bootstrap-theme.css');
    require('elemental/less/elemental.less');
    require('react-table/react-table.css');
    require('styles/docco.css');
  },

  requireGlobalErrorHandler: function() {
    require('global_scripts/GlobalErrorsHandler.js');
  },

  initializeApigClientFromLocalStorage: function(dispatch, routerHistory, redirectLocation) {
    console.log("Entering componentDidMount()...");

    console.log("ApigClient was null. Trying to recreate it using localStorage credentials...");
    console.log("Trying to load AWS credentials from local storage...");
    let awsCredentials = LocalStorageHelper.loadAWSCredentialsFromLocalStorage();
    let fbCredentials = LocalStorageHelper.loadFBCredentialsFromLocalStorage();

    if (awsCredentials.hasAllFieldsSet() && awsCredentials.willNotExpireInTheNextFiveMinutes() && fbCredentials.haveUserIdAndUserName()) {
      console.log("Found AWS credentials in localStorage that don't expire in the next ~5 mintues.");
      dispatch(initializeApiGatewayClient(awsCredentials));
      dispatch(addUserDataToState(fbCredentials.userId, fbCredentials.userName));

      console.log("Redirecting to " + redirectLocation);
      routerHistory.push(redirectLocation);
    }
  },

  clearLocalStorageAuthData: function() {
    LocalStorageHelper.deleteAWSCredentialsFromLocalStorage();
    LocalStorageHelper.deleteFBCredentialsFromLocalStorage();
  }
};

export default Utility;
