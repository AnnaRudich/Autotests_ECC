package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;

public class FilesServiceTest extends BaseTest {

    private static final String IPHONE = "iPhone";

    @Test(dataProvider = "topdanmarkDataProvider", description = "FNOL", groups = {"ecc"})
    public void attachmentImportedFromFNOLTest(@UserCompany(TOPDANMARK) User user, Claim claim){

        ClaimRequest itemizationRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();

        String token = createFNOLClaimAndGetClaimToken(itemizationRequest, createClaimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(IPHONE)
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .selectAge("2")
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(createClaimRequest,
                        Change.Property.ATTACHMENT_IMPORTED_FROM_FNOL,1);
    }
}
