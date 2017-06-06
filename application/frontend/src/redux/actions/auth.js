export function initializeApiGatewayClient(awsCredentials) {
  return {
    type: 'INITIALIZE_API_GATEWAY_CLIENT',
    awsCredentials
  };
};
