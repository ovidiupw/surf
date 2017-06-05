import CredentialNames from 'constants/CredentialNames';
import FBCredentials from 'modules/entities/FBCredentials';

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
  }
};

export default LocalStorageHelper;
