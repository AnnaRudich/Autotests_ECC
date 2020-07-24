package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.eccIntegration.PriceTypes;
import com.scalepoint.automation.utils.data.entity.eccIntegration.ValuationTypes;
import com.scalepoint.automation.utils.data.request.*;
import com.scalepoint.automation.utils.data.response.TextSearchResult.ProductResult;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.services.restService.SettlementItemDialogService.VariableFromJSP.PSEUDO_CATEGORY_ID;
import static com.scalepoint.automation.services.restService.SettlementItemDialogService.VariableFromJSP.TAX_RATE;
import static com.scalepoint.automation.services.restService.common.BasePath.SID_ITEM;
import static com.scalepoint.automation.services.restService.common.BasePath.SID_STATISTICS;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class SettlementItemDialogService extends BaseService {

    private static final String ZONE_OFFSET = "+02:00";

    private Response response;
    private Double taxRate;
    private Integer pseudoCategoryId;
    private ProductResult productResult;

    public Response getResponse() {
        return this.response;
    }

    public SettlementItemDialogService sid(ProductResult productResult){

        this.productResult = productResult;

        sidStatistics();

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("_dc", String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET))));
        queryParams.put("caseid", String.valueOf(data.getUserId()));
        queryParams.put("itemid", "");
        queryParams.put("pseudocatID", "");
        queryParams.put("published", "");
        queryParams.put("productid", String.valueOf(productResult.getId()));
        queryParams.put("replacedproductid", "");
        queryParams.put("claimDescription", "");
        queryParams.put("selectedCategory", "");
        queryParams.put("qualityweight", "");
        queryParams.put("companySpecific", "");

        this.response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .queryParams(queryParams)
                .post(SID_ITEM)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        pseudoCategoryId = Integer.valueOf(getVariableFromJSP(PSEUDO_CATEGORY_ID));

        return this;
    }

    private SettlementItemDialogService sidStatistics() {

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("_dc", String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET))));
        queryParams.put("caseid", String.valueOf(data.getUserId()));
        queryParams.put("itemid", "");
        queryParams.put("pseudocatID", "");
        queryParams.put("published", "");
        queryParams.put("productid", String.valueOf(productResult.getId()));
        queryParams.put("replacedproductid", "");
        queryParams.put("claimDescription", "");
        queryParams.put("selectedCategory", "");
        queryParams.put("companySpecific", "");

        this.response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .queryParams(queryParams)
                .post(SID_STATISTICS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        taxRate = Double.valueOf(getVariableFromJSP(TAX_RATE));

        return this;
    }

    public ClaimSettlementItemsService addLines(){

        return new ClaimSettlementItemsService()
                .addLines(testItem());
    }

    private String getVariableFromJSP(VariableFromJSP variable){

        String body = response.getBody().print();

        Matcher matcher = Pattern.compile(variable.getRegex(), Pattern.MULTILINE).matcher(body);
        matcher.find();

        return matcher.group(2);
    }

    enum VariableFromJSP{

        PSEUDO_CATEGORY_ID("(var pseudoCategoryId = )(\\d+)(;)"),
        TAX_RATE("(var taxRate = )(.+)(;)");

        private String regex;

        VariableFromJSP(String regex) {

            this.regex = regex;
        }

        public String getRegex(){

            return regex;
        }
    }

    private InsertSettlementItem testItem() {

        InsertSettlementItem insertSettlementItem = TestData.getPerformanceInsertSettlementItem();
        insertSettlementItem.setCaseId(data.getUserId().toString());

        SettlementItem settlementItem = insertSettlementItem.getSettlementItem();

        Claim claim = settlementItem.getClaim();
        claim.setDescription(productResult.getProductDesc());
        claim.setCategory(pseudoCategoryId.toString());
        claim.setClaimToken(data.getClaimToken());
        claim.setOriginalDescription(productResult.getProductDesc());

        Valuation[] valuations = settlementItem.getValuations().getValuation();

        Valuation catalogPrice = getValuationByType(valuations, ValuationTypes.CATALOG_PRICE);

        Arrays.stream(catalogPrice.getPrice()).forEach(price -> {
            switch (PriceTypes.findPrice(price.getName())) {
                case PRICE:
                    setPrice(price, productResult.getInvoicePrice());
                    break;
            }
        });

        Arrays.stream(catalogPrice.getProductMatch().getPrice()).forEach(price -> {
            switch (PriceTypes.findPrice(price.getName())) {
                case RECOMMENDED_REATAIL_PRICE:
                    setPrice(price, productResult.getMarketPrice());
                    break;
                case FREIGHT_PRICE:
                    setPrice(price, productResult.getFreightPrice());
                    break;
            }
        });

        Valuation marketPrice = getValuationByType(valuations, ValuationTypes.MARKET_PRICE);

        Arrays.stream(marketPrice.getPrice()).forEach(price -> {
            switch (PriceTypes.findPrice(price.getName())) {
                case PRICE:
                    setPrice(price, productResult.getMarketPrice());
                    break;
            }
        });

        return insertSettlementItem;
    }

    private Price setPrice(Price price, String value){

        price.setNetAmount(String.valueOf(Double.valueOf(value) / (1 + Double.valueOf(taxRate))));
        price.setAmount(value);

        return price;
    }

    private Valuation getValuationByType(Valuation[] valuations, ValuationTypes valuationType){

        return Arrays.stream(valuations)
                .filter(valuation -> valuation.getValuationType().equals(valuationType))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
