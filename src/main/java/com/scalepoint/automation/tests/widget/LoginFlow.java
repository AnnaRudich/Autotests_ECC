package com.scalepoint.automation.tests.widget;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.EditPolicyTypeDialog;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.services.externalapi.AuthenticationApi;
import com.scalepoint.automation.services.externalapi.ClaimApi;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.Settlement;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;
import static com.scalepoint.automation.utils.data.entity.credentials.User.UserType.SCALEPOINT_ID;
import static io.restassured.RestAssured.given;


@Component
public class LoginFlow {

    @Autowired
    DatabaseApi databaseApi;

    public SettlementPage loginAndCreateClaim(User user, Claim claim, String policyType) {

        MyPage myPage = login(user);
        if(user.getType().equals(SCALEPOINT_ID))
        {

            ClaimApi.createClaim(claim, 1);
        }else {

            ClaimApi claimApi = new ClaimApi(user);
            claimApi.createClaim(claim, policyType);
        }

        return myPage.to(SettlementPage.class);
    }

    public SettlementPage loginAndCreateClaim(User user, Claim claim) {

        return loginAndCreateClaim(user, claim, null);
    }

    public EditPolicyTypeDialog loginAndCreateClaimToEditPolicyDialog(User user, Claim claim) {

        loginAndCreateClaim(user, claim, null);
        return BaseDialogSelenide.at(EditPolicyTypeDialog.class);
    }

    public String createCwaClaimAndGetClaimToken(ClaimRequest claimRequest) {

        Token token = new OauthTestAccountsApi().sendRequest().getToken();

        Response response = new CreateClaimService(token).addClaim(claimRequest).getResponse();

        CurrentUser.setClaimId(String.valueOf(databaseApi.getUserIdByClaimNumber(claimRequest.getCaseNumber())));

        return response.jsonPath().get("token");
    }

    public CreateClaimService createCwaClaim(ClaimRequest claimRequest) {

        Token token = new OauthTestAccountsApi().sendRequest().getToken();
        return new CreateClaimService(token).addClaim(claimRequest);
    }

    public String createFNOLClaimAndGetClaimToken(ClaimRequest itemizationRequest, ClaimRequest createClaimRequest){

        itemizationRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        String test = unifiedIntegrationService.createItemizationCaseFNOL(createClaimRequest.getCountry(), createClaimRequest.getTenant(), itemizationRequest);
        createClaimRequest.setItemizationCaseReference(test);
        createClaimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        return unifiedIntegrationService.createClaimFNOL(createClaimRequest, databaseApi);
    }

    public SettlementPage loginAndOpenUnifiedIntegrationClaimByToken(User user, String claimToken) {

        if(user.getType().equals(SCALEPOINT_ID)){

            Page.to(LoginPage.class)
                    .loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword(), MyPage.class);

        }else {

            login(user, null);
        }

        Browser.open(getEccUrl() + "Integration/Open?token=" + claimToken);

        return new SettlementPage();
    }

    public  <T extends Page> T loginAndOpenUnifiedIntegrationClaimByToken(User user, String claimToken, Class<T> returnPageClass) {

        if(user.getType().equals(SCALEPOINT_ID)){

            Page.to(LoginPage.class)
                    .loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword(), MyPage.class);
        }else {

            login(user, null);
        }

        Browser.open(getEccUrl() + "Integration/Open?token=" + claimToken);

        return Page.at(returnPageClass);
    }

    public MyPage login(User user) {

        MyPage myPage;

        if(user.getType().equals(SCALEPOINT_ID)){

            myPage = Page.to(LoginPage.class)
                    .loginViaScalepointId()
                    .login(user);
        } else{

            Page.to(LoginPage.class);
            myPage = AuthenticationApi.createServerApi().login(user, MyPage.class);
        }

        return myPage;
    }

    public  <T extends Page> T login(User user, Class<T> returnPageClass) {
        Page page;

        if(user.getType().equals(SCALEPOINT_ID)){

            page = Page.to(LoginPage.class)
                    .loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword(), returnPageClass);
        } else{

            Page.to(LoginPage.class);
            page = AuthenticationApi
                    .createServerApi()
                    .login(user, returnPageClass);
        }

        return (T) page;
    }

    public  <T extends Page> T login(User user, Class<T> returnPageClass, String parameters) {

        Page.to(LoginPage.class);
        return AuthenticationApi.createServerApi().login(user, returnPageClass, parameters);
    }

    public SuppliersPage loginToEccAdmin(User user) {
        return login(user)
                .getMainMenu()
                .toEccAdminPage();
    }

    public EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany, EditReasonsPage.ReasonType reasonType, boolean showDisabled) {

        return login(getSystemUser(), AdminPage.class)
                .to(EditReasonsPage.class)
                .applyFilters(insuranceCompany.getFtTrygHolding(), reasonType, showDisabled)
                .assertEditReasonsFormVisible();
    }

    public EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany, boolean showDisabled) {

        return openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false);
    }
}
