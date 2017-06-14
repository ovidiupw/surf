package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FBSuccessResponse {
    @JsonProperty("data")
    private FBResponseData data;

    public FBResponseData getData() {
        return data;
    }

    public void setData(FBResponseData data) {
        this.data = data;
    }
}