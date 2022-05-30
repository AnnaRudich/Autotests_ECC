package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FilesServiceSelfServiceSystemTest extends BaseUITest {

    private static final String ATTACHMENT_IMPORTED_FROM_SELF_SERVICE_SYSTEM_DATA_PROVIDER = "attachmentImportedFromSelfServiceSystemDataProvider";
    private static final String ATTACHMENT_IMPORTED_FROM_SELF_FNOL_DATA_PROVIDER = "attachmentImportedFromFNOLDataProvider";

    private static final String IPHONE = "iPhone";
    private static final String AGE = "2";

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = ATTACHMENT_IMPORTED_FROM_SELF_SERVICE_SYSTEM_DATA_PROVIDER,
            description = "Verifies attachment imported form Self Service")
    public void attachmentImportedFromSelfServiceSystemTest(@UserAttributes(company = TOPDANMARK) User user,
                                                            Claim claim, ClaimRequest claimRequest, String description,
                                                            Double newPrice, Double customerDemandPrice, String age){

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(description)
                .addNewPrice(newPrice)
                .addCustomerDemandPrice(customerDemandPrice)
                .selectAge(age)
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_IMPORTED_FROM_SELFSERVICE,1);
    }

    @Test(enabled = false, groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK},
            dataProvider = ATTACHMENT_IMPORTED_FROM_SELF_FNOL_DATA_PROVIDER,
            description = "Verifies attachment imported from FNOL")
    public void attachmentImportedFromFNOLSystemTest(@UserAttributes(company = TOPDANMARK) User user,
                                                     Claim claim, ClaimRequest itemizationRequest, ClaimRequest createClaimRequest,
                                                     String description, Double newPrice, Double customerDemandPrice,
                                                     String age){

        String token = loginFlow.createFNOLClaimAndGetClaimToken(itemizationRequest, createClaimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(description)
                .addNewPrice(newPrice)
                .addCustomerDemandPrice(customerDemandPrice)
                .selectAge(age)
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(createClaimRequest,
                        Change.Property.ATTACHMENT_IMPORTED_FROM_FNOL,1);
    }

    @DataProvider(name = ATTACHMENT_IMPORTED_FROM_SELF_SERVICE_SYSTEM_DATA_PROVIDER)
    public static Object[][] attachmentImportedFromSelfServiceSystemDataProvider(Method method) {

        List parameters = removeObjectByClass(TestDataActions.getTestDataParameters(method), ClaimRequest.class);

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        parameters.add(claimRequest);
        parameters.add(IPHONE);
        parameters.add(Constants.PRICE_500);
        parameters.add(Constants.PRICE_50);
        parameters.add(AGE);

        return new Object[][]{

                parameters.toArray()
        };
    }

    @DataProvider(name = ATTACHMENT_IMPORTED_FROM_SELF_FNOL_DATA_PROVIDER)
    public static Object[][] attachmentImportedFromFNOLDataProvider(Method method) {

        List parameters = removeObjectByClass(TestDataActions.getTestDataParameters(method), ClaimRequest.class);

        ClaimRequest itemizationRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();

        parameters.add(itemizationRequest);
        parameters.add(createClaimRequest);
        parameters.add(IPHONE);
        parameters.add(Constants.PRICE_500);
        parameters.add(Constants.PRICE_50);
        parameters.add(AGE);

        return new Object[][]{

                parameters.toArray()
        };
    }
}
