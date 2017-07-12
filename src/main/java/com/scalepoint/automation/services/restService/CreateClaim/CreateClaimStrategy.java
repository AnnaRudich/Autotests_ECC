package com.scalepoint.automation.services.restService.CreateClaim;

import io.restassured.response.ValidatableResponse;

import java.util.Optional;

/**
 * Created by bza on 7/11/2017.
 */
public interface CreateClaimStrategy {

    CreateClaimStrategy createClaim();
    CreateClaimStrategy saveData();
    ValidatableResponse getResponse();

    default ValidatableResponse getValidResponse(Optional<ValidatableResponse> optional){
        if(!optional.isPresent()) {
            throw new IllegalStateException("Claim response is not present");
        }
        return optional.get();
    }
}
