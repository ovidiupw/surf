class AWSCredentials {
  constructor(credentialsMap) {
    this.accessKey = credentialsMap.accessKey;
    this.secretKey = credentialsMap.secretKey;
    this.sessionToken = credentialsMap.sessionToken;
    this.region = credentialsMap.region;
    this.apiKey = credentialsMap.apiKey;
    this.expiration = credentialsMap.expiration;
  }

  toString() {
    return `{accessKey: ${this.accessKey},
    secretKey: ${this.secretKey},
    sessionToken: ${this.sessionToken},
    region: ${this.region},
    apiKey: ${this.apiKey},
    expiration: ${this.expiration}}`;
  }
}

export default AWSCredentials;
