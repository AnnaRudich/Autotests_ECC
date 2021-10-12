package com.scalepoint.automation.tests.communicationDesigner;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.communicationDesignerEmailTemplates.AutomaticCustomerWelcomeEmailTemplate;
import com.scalepoint.automation.utils.data.entity.communicationDesignerEmailTemplates.CommunicationDesignerEmailTemplates;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.to;

public class CommunicationDesignerFnolTests extends CommunicationDesignerBaseTests {

    private static final String AUTOMATIC_CUSTOMER_WELCOME_DATA_PROVIDER = "automaticCustomerWelcomeDataProvider";

    @BeforeMethod
    public void toSettlementPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        ClaimRequest createClaimRequest = getLisOfObjectByClass(parameters, ClaimRequest.class).get(0);
        ClaimRequest itemizationClaimRequest = getLisOfObjectByClass(parameters, ClaimRequest.class).get(0);

        String token = createFNOLClaimAndGetClaimToken(itemizationClaimRequest, createClaimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token);
    }

    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = AUTOMATIC_CUSTOMER_WELCOME_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcome")
    public void automaticCustomerWelcomeTest(User user, ClaimItem claimItem,
                                             CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                             ClaimRequest createClaimRequest, ClaimRequest itemizationClaimRequest,
                                             String password, String month,
                                             Double newPrice, CommunicationDesigner communicationDesigner) {

        String claimLineDescription = claimItem.getSetDialogTextMatch();
        String automaticCustomerWelcomeTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(AutomaticCustomerWelcomeEmailTemplate.class)
                .getTitle();

        Page.at(SettlementPage.class)
                .cancelPolicy()
                .requestSelfServiceWithEnabledNewPassword(createClaimRequest, password)
                .toMailsPage(mailserviceStub, databaseApi)
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(password)
                .addDescriptionWithOutSuggestions(claimLineDescription)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(month)
                .addNewPrice(newPrice)
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem()
                .sendResponseToEcc();

        to(MyPage.class).openRecentClaim()
                .toMailsPage(mailserviceStub, databaseApi)
                .doAssert(mail ->
                        mail.noOtherMailsOnThePage(
                                Arrays.asList(
                                        MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL,
                                        MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME,
                                        MailsPage.MailType.CUSTOMER_WELCOME))
                )
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, automaticCustomerWelcomeTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(automaticCustomerWelcomeTitle)
                );
    }

    @DataProvider(name = AUTOMATIC_CUSTOMER_WELCOME_DATA_PROVIDER)
    public static Object[][] automaticCustomerWelcomeDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setAutomaticCustomerWelcome(false, null);

        List parameters = removeObjectByClass(TestDataActions.getTestDataParameters(method), ClaimRequest.class);

        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();
        ClaimRequest itemizationClaimRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        String password = DEFAULT_PASSWORD;
        String month = DEFAULT_MONTH;
        Double newPrice = 3000.00;
        String companyName = user.getCompanyName();

        parameters.addAll(Arrays.asList(setAllowAutoClose(createClaimRequest, companyName),
                setAllowAutoClose(itemizationClaimRequest, companyName), password, month, newPrice, communicationDesigner));

        return new Object[][]{

                parameters.toArray()
        };
    }

    private static ClaimRequest setAllowAutoClose(ClaimRequest claimRequest, String companyName){

        claimRequest.setCompany(companyName);
        claimRequest.setTenant(companyName);
        claimRequest.setAllowAutoClose(true);

        return claimRequest;
    }
}
