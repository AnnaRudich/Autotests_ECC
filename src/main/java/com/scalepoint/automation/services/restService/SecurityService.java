package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.utils.data.entity.credentials.User;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.Optional;
import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SecurityService extends LoginProcessService {

    public SecurityService login(User user) {

        super.login(user);
        return this;
    }

    public SecurityService requestOutputManagementStatus() {

        getRequest("/rest/om-admin/status");

        return this;
    }

    public SecurityService requestInsuranceCompanies() {

        getRequest("/rest/ExternalClaimOperation/getInsuranceCompanies.json");

        return this;
    }

    public SecurityService requestOutstandingAmounts() {

        getRequest("/admin/outstanding-amounts/list.json");

        return this;
    }

    public SecurityService requestListOfInsuranceCompanies() {

        getRequest("/rest/admin/reductionRules/companies");

        return this;
    }

    public SecurityService requestVouchersData() {

        getRequest("/rest/voucherData/collect.json");

        return this;
    }

    public void getRequest(String uri){

        RequestSpecification requestSpecification = given()
                .baseUri(getEccUrl())
                .redirects().follow(false);

        String eccSessionId = data.getEccSessionId();

        if(Optional.ofNullable(eccSessionId).isPresent()) {

            requestSpecification
                    .sessionId(data.getEccSessionId());
        }

        response = requestSpecification
                .get(uri)
                .then()
                .extract().response();
    }

    public SecurityService doAssert(Consumer<Asserts> assertsFunc) {

        assertsFunc.accept(new Asserts());
        return SecurityService.this;
    }

    public class Asserts {

        public Asserts assertAuthorized() {

            assertThat(response.getStatusCode())
                    .as("Request is unauthorized")
                    .isEqualTo(HttpStatus.SC_OK);
            return this;
        }

        public Asserts assertUnauthorized() {

            assertThat(response.getStatusCode())
                    .as("Request is should be authorized")
                    .isEqualTo(HttpStatus.SC_UNAUTHORIZED);
            return this;
        }
    }
}
