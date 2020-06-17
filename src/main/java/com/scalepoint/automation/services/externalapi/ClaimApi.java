package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.exceptions.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;

import java.util.List;

import static com.scalepoint.automation.utils.Http.*;

@SuppressWarnings("ConstantConditions")
public class ClaimApi extends AuthenticationApi {

    private static final int ATTEMPTS_LIMIT = 1;
    private static final String URL_CREATE_CUSTOMER = Configuration.getEccUrl() + "CreateUser";

    public ClaimApi(User user) {
        super(user);
    }

    public void createClaim(Claim claim, String policyType) {
        createClaim(claim, 0, policyType);
    }

    private void createClaim(Claim claim, int attempt, String policyType) {
        log.info("Create client: " + claim.getClaimNumber());

        List<NameValuePair> clientParams = ParamsBuilder.create().
                add("policytype", policyType).
                add("last_name", claim.getLastName()).
                add("first_name", claim.getFirstName()).
                add("policy_number", claim.getPolicyNumber()).
                add("claim_number", claim.getClaimNumber()).
                add("url", "").get();

        try {
            HttpResponse createUserResponse = post(URL_CREATE_CUSTOMER, clientParams, executor).returnResponse();
            ensure302Code(createUserResponse.getStatusLine().getStatusCode());

            Header headerLocation = createUserResponse.getHeaders("Location").length > 0 ?
                    createUserResponse.getHeaders("Location")[0] :
                    new BasicHeader("Location", "error=1");

            if (headerLocation.getValue().contains("error=1")) {
                throw new IllegalStateException("Response contains wrong location: " + headerLocation.getValue());
            }

            log.info("CreateUser redirected to: " + headerLocation);
            log.info("Base ECC URL is:          " + Configuration.getEccUrl());

            String claimId = headerLocation.getValue().replaceAll(".*/([0-9]+)/.*", "$1");
            CurrentUser.setClaimId(claimId);
            claim.setClaimId(claimId);

            SolrApi.waitForClaimAppearedInIndexById(claim);
        } catch (Throwable e) {
            log.error("Can't create claim", e);
            if (attempt < ATTEMPTS_LIMIT) {
                createClaim(claim, ++attempt, policyType);
            } else {
                throw new ServerApiException(e);
            }
        }
    }

}
