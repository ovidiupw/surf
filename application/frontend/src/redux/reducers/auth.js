export function auth(state = {
  apigClient: undefined,
  apigClientCredentialsExpiration: undefined
}, action) {
  switch (action.type) {
    case 'INITIALIZE_API_GATEWAY_CLIENT': {
      console.log("Initializing the API Gateway client...");

      var apigClient = apigClientFactory.newClient({
        accessKey: action.awsCredentials.accessKey,
        secretKey: action.awsCredentials.secretKey,
        sessionToken: action.awsCredentials.sessionToken,
        region: action.awsCredentials.region,
        apiKey: action.awsCredentials.apiKey
      });

      let newState = Object.assign({}, state, {
        apigClient: apigClient,
        apigClientCredentialsExpiration: action.awsCredentials.expiration
      });

      return newState;
    }
    default: {
      return state;
    }
  }
};
