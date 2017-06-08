
const authInitialState = {
  apigClient: undefined,
  apigClientCredentialsExpiration: undefined,
  userId: undefined,
  userName: undefined,
  loggedIn: false
};

export function auth(state = authInitialState, action) {

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
        apigClientCredentialsExpiration: action.awsCredentials.expiration,
        loggedIn: true
      });

      return newState;
    }

    case 'ADD_USER_DATA_TO_STATE': {
      console.log("Registering user data within state...");
      let newState = Object.assign({}, state, {
        userId: action.userId,
        userName: action.userName
      });
      return newState;
    }

    case 'DELETE_AUTH_DATA_FROM_STATE': {
      console.log("Deleting auth data froms state...");
      return Object.assign({}, state, authInitialState);
    }

    default: {
      return state;
    }
  }
};
