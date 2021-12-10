package com.scalepoint.automation.services.restService;

import com.fasterxml.jackson.annotation.JsonValue;
import com.scalepoint.automation.services.restService.common.BaseService;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.scalepoint.automation.services.restService.common.BasePath.HEALTCH_CHECK;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckService extends BaseService {

    private Response response;
    private Set<HealthCheck.HealthCheckName> excludedHealthChecks;

    public HealthCheckService(String excludedHealthChecks) {

        this.excludedHealthChecks = Arrays.stream(excludedHealthChecks.split(","))
                .filter(name -> !name.isEmpty())
                .map(name -> HealthCheck.HealthCheckName.findByName(name))
                .collect(Collectors.toSet());
    }

    public Response getResponse() {
        return this.response;
    }

    public List<HealthCheck.Result> getResults() {

        return response
                .as(HealthCheck.class)
                .getResults();
    }

    public HealthCheckService healthCheckStatus() {

        this.response = given().baseUri(getEccUrl())
                .basePath(HEALTCH_CHECK)
                .when()
                .get()
                .then()
                .log().all()
                .extract().response();

        return this;
    }

    public HealthCheckService doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return HealthCheckService.this;
    }

    public class Asserts {

        public Asserts assertHealthChecks() {

            getResults().stream()
                    .filter(result -> !excludedHealthChecks.contains(result.name))
                    .forEach(result -> assertThat(result.getStatus())
                            .as(String.format("HealthCheck %s status is %s", result.getName(), result.getStatus()))
                            .isNotEqualTo("FAIL"));

            return this;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthCheck{

        private List<Result> results;
        private String status;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Result{

            private HealthCheckName name;
            private String status;
        }

        public enum HealthCheckName {

            BLOCKED_THREADS("Blocked Threads Healthcheck"),
            DATABASE_CONNECTIONS("Database connections status"),
            CLAIM_SEARCH("ClaimSearch"),
            SOLR("Solr"),
            SOLR_INDEX("Solr index status"),
            LAST_SOLR_REDINDEX("Last Solr Reindex"),
            SOLR_XPRICE("Solr XPrice errors counter"),
            OUTPUT_MANAGEMENT("OutputManagement responses"),
            OUTPUT_MANAGEMENT_TEMPLATES("OutputManagement templates availability"),
            MAILSERVICE_REQUESTS("Failed Mailservice Requests"),
            ECC_FILES("ECC Files Database"),
            ECC_DB("ECC Database"),
            ECC_ADMIN("EccAdmin"),
            MONGO_DB("MongoDB"),
            RNV("Repair and Valuation module"),
            STATELESS("Stateless"),
            PAYEX("PayEx"),
            MAILSERVICE("MailService"),
            SMTP("SMTP host"),
            PDF("PDF webservice"),
            SELF_SERVICE("SelfService 2.0"),
            AUDIT("Audit Health Status"),
            EVENT_API("Event API Health Status"),
            CWA("CWA Event Sink"),
            SPID("ScalepointID Health Status"),
            VOUCHER_PREDICTION("VoucherPrediction"),
            CATEGORIZATION("Categorization service"),
            POSTAL_CODE("Postal Code Service");

            private String description;

            HealthCheckName(String description) {

                this.description = description;
            }

            @JsonValue
            public String getName(){

                return description;
            }

            public static HealthCheckName findByName(String name){

                return Arrays.stream(HealthCheckName.values())
                        .filter(value -> value.name().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(name));
            }
        }
    }


}





