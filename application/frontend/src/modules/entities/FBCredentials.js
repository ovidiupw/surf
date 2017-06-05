import CredentialNames from 'constants/CredentialNames';

class FBCredentials {
  constructor(userId, accessToken, userName) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.userName = userName;
  }

  toString() {
    return `{userId: ${this.userId}, accessToken: ${this.accessToken}, userName: ${this.userName}}`;
  }
}

export default FBCredentials;
