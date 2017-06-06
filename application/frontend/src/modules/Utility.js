let Utility = {
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

  requireBootstrapResources: function() {
    require('bootstrap/dist/css/bootstrap.css');
  }
};

export default Utility;
