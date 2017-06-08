import CredentialNames from 'constants/CredentialNames';
import FBCredentials from 'modules/entities/FBCredentials';
import AWSCredentials from 'modules/entities/AWSCredentials';

let LocalStorageHelper = {
  saveFBCredentialsToLocalStorage: function(credentials) {
    if (!(credentials instanceof FBCredentials)) {
      throw new Error('The credentials supplied must be instanceof FBCredentials!');
    }

    localStorage.setItem(CredentialNames.FB_USER_ID, credentials.userId);
    localStorage.setItem(CredentialNames.FB_ACCESS_TOKEN, credentials.accessToken);
    localStorage.setItem(CredentialNames.FB_USER_NAME, credentials.userName);
  },

  loadFBCredentialsFromLocalStorage: function() {
    return new FBCredentials(
      localStorage.getItem(CredentialNames.FB_USER_ID),
      localStorage.getItem(CredentialNames.FB_ACCESS_TOKEN),
      localStorage.getItem(CredentialNames.FB_USER_NAME));
  },

  deleteFBCredentialsFromLocalStorage: function() {
    localStorage.removeItem(CredentialNames.FB_USER_ID);
    localStorage.removeItem(CredentialNames.FB_ACCESS_TOKEN);
    localStorage.removeItem(CredentialNames.FB_USER_NAME);

    return this.loadFBCredentialsFromLocalStorage();
  },

  saveAWSCredentialsToLocalStorage: function(credentials) {
    if (!(credentials instanceof AWSCredentials)) {
      throw new Error('The credentials supplied must be instanceof AWSCredentials!');
    }

    localStorage.setItem(CredentialNames.AWS_ACCESS_KEY, credentials.accessKey);
    localStorage.setItem(CredentialNames.AWS_SECRET_KEY, credentials.secretKey);
    localStorage.setItem(CredentialNames.AWS_SESSION_TOKEN, credentials.sessionToken);
    localStorage.setItem(CredentialNames.AWS_REGION, credentials.region);
    localStorage.setItem(CredentialNames.AWS_API_GATEWAY_KEY, credentials.apiKey);
    localStorage.setItem(CredentialNames.AWS_CREDENTIALS_EXPIRATION, credentials.expiration);
  },

  loadAWSCredentialsFromLocalStorage: function() {
    return new AWSCredentials({
      accessKey: localStorage.getItem(CredentialNames.AWS_ACCESS_KEY),
      secretKey: localStorage.getItem(CredentialNames.AWS_SECRET_KEY),
      sessionToken: localStorage.getItem(CredentialNames.AWS_SESSION_TOKEN),
      region: localStorage.getItem(CredentialNames.AWS_REGION),
      apiKey: localStorage.getItem(CredentialNames.AWS_API_GATEWAY_KEY),
      expiration: localStorage.getItem(CredentialNames.AWS_CREDENTIALS_EXPIRATION)
    });
  },

  deleteAWSCredentialsFromLocalStorage: function() {
    localStorage.removeItem(CredentialNames.AWS_ACCESS_KEY);
    localStorage.removeItem(CredentialNames.AWS_SECRET_KEY);
    localStorage.removeItem(CredentialNames.AWS_SESSION_TOKEN);
    localStorage.removeItem(CredentialNames.AWS_REGION);
    localStorage.removeItem(CredentialNames.AWS_API_GATEWAY_KEY);
    localStorage.removeItem(CredentialNames.AWS_CREDENTIALS_EXPIRATION);

    return this.loadFBCredentialsFromLocalStorage();
  },
};

export default LocalStorageHelper;
