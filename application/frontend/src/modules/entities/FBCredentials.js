class FBCredentials {
  constructor(userId, accessToken, userName) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.userName = userName;
  }

  haveUserIdAndUserName() {
    return this.userId != null && this.userName != null;
  }

  toString() {
    return `{userId: ${this.userId}, accessToken: ${this.accessToken}, userName: ${this.userName}}`;
  }
}

export default FBCredentials;
