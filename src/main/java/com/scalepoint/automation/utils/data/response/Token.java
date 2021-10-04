package com.scalepoint.automation.utils.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.http.Header;
import lombok.Data;

/**
 * Created by bza on 5/25/2017.
 */
@Data
public class Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("scope")
    private String scope;

    public Header getAuthorizationHeader() {
        return new Header("Authorization", tokenType + " " + accessToken);
    }

}
