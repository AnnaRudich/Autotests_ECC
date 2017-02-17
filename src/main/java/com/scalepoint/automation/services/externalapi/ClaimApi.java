package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.CurrentUser;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.utils.Http.*;

@SuppressWarnings("ConstantConditions")
public class ClaimApi extends AuthenticationApi {

    private static final int ATTEMPTS_LIMIT = 1;
    private static String DATE_FORMAT = "yyyy-MM-dd";

    public static final String URL_CREATE_CUSTOMER = Configuration.getEccUrl() + "CreateUser";
    public static final String URL_SAVE_CUSTOMER_PAGE = Configuration.getEccUrl() + "webshop/jsp/matching_engine/save_customer.jsp?";

    public ClaimApi(User user) {
        super(user);
    }

    public ClaimApi(Executor executor) {
        super(executor);
    }

    public void createClaim(Claim claim, String policyType) {
        createClaim(claim, 0, policyType);
    }

    public void createClaim(Claim claim) {
        createClaim(claim, 0, null);
    }

    private void createClaim(Claim claim, int attempt, String policyType) {
        log.info("Create client: " + claim.getClaimNumber());

        List<NameValuePair> clientParams = ParamsBuilder.create().
                add("policytype", policyType).
                add("damageDate", new SimpleDateFormat(DATE_FORMAT).format(new Date())).
                add("last_name", claim.getLastName()).
                add("first_name", claim.getFirstName()).
                add("policy_number", claim.getPolicyNumber()).
                add("claim_number", claim.getClaimNumber()).
                add("url", "").get();

        try {
            HttpResponse createUserResponse = post(URL_CREATE_CUSTOMER, clientParams, executor).returnResponse();
            ensure302Code(createUserResponse.getStatusLine().getStatusCode());

            Header location = createUserResponse.getHeaders("Location").length > 0 ?
                    createUserResponse.getHeaders("Location")[0] :
                    null;

            log.info("CreateUser redirected to: " + location);
            log.info("Base ECC URL is:          " + Configuration.getEccUrl());

            String claimId = location.getValue().replaceAll(".*/([0-9]+)/.*", "$1");
            CurrentUser.setClaimId(claimId);

            Browser.driver().get(location.getValue() + "settlement.jsp");
        } catch (IOException e) {
            log.error("Can't create claim", e);
            if (attempt < ATTEMPTS_LIMIT) {
                createClaim(claim, ++attempt, policyType);
            } else {
                throw new ServerApiException(e);
            }
        }
    }

    private String extractUrl(String html) {
        String eccContext = Configuration.getEccContext();
        String locale = Configuration.getLocale().toString().toLowerCase();

        String regex = String.format("href=\"/%s/%s(.*?)\"", eccContext, locale);

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(html);
        String url = null;
        if (m.find()) {
            url = m.group(1);
        }
        return url;
    }


}
