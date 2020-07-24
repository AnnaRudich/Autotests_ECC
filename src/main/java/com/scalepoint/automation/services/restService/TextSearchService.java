package com.scalepoint.automation.services.restService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.request.ServiceArgument;
import com.scalepoint.automation.utils.data.response.TextSearchResult.ProductResult;
import com.scalepoint.automation.utils.data.response.TextSearchResult.TextSearchResult;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.ArrayList;

import static com.scalepoint.automation.services.restService.common.BasePath.SECURED_IMAGE;
import static com.scalepoint.automation.services.restService.common.BasePath.TEXT_SEARCH;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class TextSearchService extends BaseService {

    private Response response;

    private TextSearchResult textSearchResult;

    public Response getResponse() {
        return this.response;
    }

    public TextSearchService searchText(String text) throws JsonProcessingException {

        ServiceArgument serviceArgument = ServiceArgument.builder()
                .selectedCategory(0)
                .startcat(1)
                .brand(0)
                .model(0)
                .price("")
                .difference("")
                .searchText(text)
                .start(1)
                .count(10)
                .sort("relevance")
                .first("")
                .selectedAttributes(new ArrayList<>())
                .refreshAllComponents(true)
                .matchId(-1)
                .matchWithSuggestion(false)
                .selectedModelId(-1)
                .didYouMeanMatch(false)
                .manuallySelectedCategory(false)
                .build();

        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .formParam("service_argument", new ObjectMapper().writeValueAsString(serviceArgument))
                .pathParam("userId", data.getUserId())
                .post(TEXT_SEARCH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        textSearchResult = response.as(TextSearchResult.class);

        textSearchResult
                .getProductResultList()
                .getProductResults()
                .stream()
                .forEach(productResult -> securedImage(String.valueOf(productResult.getProductId())));

        return this;
    }

    public TextSearchResult getTextSearchResult(){

        return textSearchResult;
    }

    private TextSearchService securedImage(String id){

        given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .queryParam("id", id)
                .get(SECURED_IMAGE)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public SettlementItemDialogService sid(ProductResult productResult){

        return new SettlementItemDialogService().sid(productResult);
    }
}
