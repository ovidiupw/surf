class AWSCredentials {
  constructor(credentialsMap) {
    this.accessKey = credentialsMap.accessKey;
    this.secretKey = credentialsMap.secretKey;
    this.sessionToken = credentialsMap.sessionToken;
    this.region = credentialsMap.region;
    this.apiKey = credentialsMap.apiKey;
    this.expiration = credentialsMap.expiration;
  }

  hasAllFieldsSet() {
    let allFieldsSet =  this.accessKey != null
    && this.secretKey != null
    && this.sessionToken != null
    && this.region != null
    && this.apiKey != null
    && this.expiration != null;

    let expirationIsADate = true;
    try {
      new Date(this.expiration);
    } catch (ignored) {
      expirationIsADate = false;
    }

    return allFieldsSet && expirationIsADate;
  }

  areNotExpired() {
    return new Date(this.expiration).getTime() - new Date().getTime() > 0;
  }

  willNotExpireInTheNextFiveMinutes() {
    return new Date(this.expiration).getTime() - new Date().getTime() > 1000 * 60 * 5;
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
