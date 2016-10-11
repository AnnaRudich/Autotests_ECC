package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.utils.Http.*;

public class ClaimApi extends EccServerApi {

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

    public void createClaim(Claim claim) {
        createClaim(claim, 0);
    }
    private void createClaim(Claim claim, int attempt) {
        log.info("Create client: " + claim.getClaimNumber());

        List<NameValuePair> clientParams = ParamsBuilder.create().
                add("policytype", claim.getPolicyTypeFF()).
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

            if (location != null && !location.getValue().endsWith("/")) {
                String leaveCaseRedirectUrl = location.getValue() + Page.getUrl(SettlementPage.class);
                log.info("Leaving the case: " + leaveCaseRedirectUrl);
                String query = new URL(leaveCaseRedirectUrl).getQuery();
                log.info("Extracted query: " + query);
                String saveCustomerJsp = URL_SAVE_CUSTOMER_PAGE + query+"&policytype="+claim.getPolicyType();
                log.info("To saveCustomer.jsp: " + saveCustomerJsp);

                String content = get(saveCustomerJsp, executor).returnContent().asString();
                String saveCustomerUrl = Configuration.getEccUrl() + extractUrl(content);
                log.info("To SaveCustomer: " + saveCustomerUrl);

                HttpResponse saveCustomerResponse = get(saveCustomerUrl, executor).returnResponse();
                ensure200Code(saveCustomerResponse.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            log.error("Can't create claim", e);
            if (attempt < ATTEMPTS_LIMIT) {
                createClaim(claim, ++attempt);
            } else {
                throw new ServerApiException(e);
            }
        }
    }

    private String extractUrl(String html) {
        Pattern p = Pattern.compile("href=\"/webapp/ScalePoint/dk(.*?)\"");
        Matcher m = p.matcher(html);
        String url = null;
        if (m.find()) {
            url = m.group(1);
        }
        return url;
    }
}
