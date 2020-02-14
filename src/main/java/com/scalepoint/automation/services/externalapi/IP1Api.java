package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.testng.Assert;

import java.net.URISyntaxException;
import java.util.Objects;

public class IP1Api {

    public static SettlementPage doGetIntegration(User user, Claim claim, boolean withLogin) {
        String url = buildUrl(claim);
        if (withLogin) {
            SettlementPage settlementPage = Page.getUrlAndExpectPage(url, LoginPage.class).login(user.getLogin(), user.getPassword(), SettlementPage.class);
            SolrApi.waitForClaimAppearedInIndexByClaimNumber(claim);
            return settlementPage;
        }
        SettlementPage settlementPage = Page.getUrlAndExpectPage(url, SettlementPage.class);
        SolrApi.waitForClaimAppearedInIndexByClaimNumber(claim);
        return settlementPage;
    }

    public static void assertGetIntegrationHasError(User user, Claim claim, boolean withLogin, String expectedError) {
        String url = buildUrl(claim);

        if (withLogin) {
            Page.getUrlAndExpectPage(url, LoginPage.class).loginWithoutExpectedPage(user.getLogin(), user.getPassword());
        } else {
            Objects.requireNonNull(Browser.driver()).get(url);
        }
        Assert.assertTrue(Objects.requireNonNull(Browser.driver()).getPageSource().contains(expectedError));
    }

    private static String buildUrl(Claim claim) {
        String url;
        try {
            URIBuilder b = new URIBuilder(Configuration.getEccUrl() + "Integration/CreateAndOpen");
            b.addParameter("firstName", claim.getFirstName());
            b.addParameter("lastName", claim.getLastName());
            b.addParameter("addressLine1", claim.getAddress());
            b.addParameter("zipCode", claim.getZipCode());
            b.addParameter("city", claim.getCity());
            b.addParameter("phone1", claim.getPhoneNumber());
            b.addParameter("deductible", "0");
            b.addParameter("claimsNumber", claim.getClaimNumber());
            b.addParameter("policy", claim.getPolicyTypeFF());
            if (StringUtils.isNotBlank(claim.getDamageDate())) {
                b.addParameter("damageDate", claim.getDamageDate());
            }
            b.addParameter("updateAction", "U");
            url = b.build().toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e.getMessage() + ": " + claim.toString());
        }
        return url;
    }
}
