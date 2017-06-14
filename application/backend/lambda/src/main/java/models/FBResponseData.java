package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FBResponseData {
    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("application")
    private String application;

    @JsonProperty("expires_at")
    private long expiresAt;

    @JsonProperty("is_valid")
    private boolean isValid;

    @JsonProperty("scopes")
    private List<String> scopes;

    @JsonProperty("user_id")
    private String userId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
