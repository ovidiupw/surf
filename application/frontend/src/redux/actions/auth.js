export function initializeApiGatewayClient(awsCredentials) {
  return {
    type: 'INITIALIZE_API_GATEWAY_CLIENT',
    awsCredentials
  };
};

export function addUserDataToState(userId, userName) {
  return {
    type: 'ADD_USER_DATA_TO_STATE',
    userId,
    userName
  };
}

export function deleteAuthDataFromState() {
  return {
    type: 'DELETE_AUTH_DATA_FROM_STATE'
  };
}
