package com.scalepoint.automation.tests.communicationDesigner;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.to;

public class CommunicationDesignerBaseTests extends BaseTest {

    protected static final String CUSTOMER_WELCOME_REJECTION = "[CustomerWelcomeRejectionMail]";
    protected static final String CUSTOMER_WELCOME = "[CustomerWelcome]";
    protected static final String CUSTOMER_WELCOME_WITH_OUTSTANDING = "[CustomerWelcomeWithOutstanding]";
    protected static final String ORDER_CONFIRMATION = "[OrderConfirmation]";
    protected static final String REPLACEMENT_MAIL = "[ReplacementMail]";
    protected static final String AUTOMATIC_CUSTOMER_WELCOME = "[AutomaticCustomerWelcome]";
    protected static final String ITEMIZATION_SUBMIT_LOSS_ITEMS = "[ItemizationSubmitLossItems]";
    protected static final String ITEMIZATION_SAVE_LOSS_ITEMS = "[ItemizationSaveLossItems]";
    protected static final String SELFSERVICE_CUSTOMER_WELCOME = "[SelfServiceCustomerWelcome]";
    protected static final String ONE_ATTACHMENT = "testpdf.pdf";
    protected static final String TWO_ATTACHMENTS = "testpdf.pdf,testpdf.pdf";


    @BeforeMethod
    public void setCommunicationDesignerSection(Object[] objects) throws IOException {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        CommunicationDesigner communicationDesigner = getObjectByClass(parameters, CommunicationDesigner.class).get(0);

        communicationDesignerMock.addStub(user.getCompanyName().toLowerCase());

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);
    }

    protected MailsPage replacement(Claim claim, ClaimItem claimItem, String regNumber, String accountNumber){

        return Page.at(SettlementPage.class)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .completeClaimUsingCashPayoutToBankAccount(regNumber, accountNumber)
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertClaimCompleted)
                .openRecentClaim()
                .toMailsPage();
    }

    protected void sendSelfServiceCustomerWelcomeEmail(Claim claim, String companyCode, String password){

        MailsPage mailsPage = Page.at(SettlementPage.class)
                .requestSelfService(claim, password)
                .toMailsPage();

        mailsPage
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .doAssert(mailViewDialog -> {
                    mailViewDialog.isTextVisible(SELFSERVICE_CUSTOMER_WELCOME);
                });

        schemaValidation(companyCode.toLowerCase(), claim.getClaimNumber());
    }

    protected void sendItemizationSubmitAndSaveLossItemsEmails(User user, Claim claim, ClaimItem claimItem, String password, String month, Double newPrice){

        String claimLineDescription = claimItem.getSetDialogTextMatch();

        Page.at(SettlementPage.class)
                .requestSelfServiceWithEnabledAutoClose(claim, password)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(password)
                .addDescriptionWithOutSuggestions(claimLineDescription)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(month)
                .addNewPrice(newPrice)
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem()
                .saveResponse()
                .continueRegistration()
                .login(password)
                .sendResponseToEcc();

        to(MyPage.class).openActiveRecentClaim()
                .toMailsPage()
                .doAssert(mail -> {
                    mail.isMailExist(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL);
                    mail.isMailExist(MailsPage.MailType.ITEMIZATION_CONFIRMATION_IC_MAIL);
                })
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL, ITEMIZATION_SUBMIT_LOSS_ITEMS)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ITEMIZATION_SUBMIT_LOSS_ITEMS)
                )
                .cancel()
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL, ITEMIZATION_SAVE_LOSS_ITEMS)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ITEMIZATION_SAVE_LOSS_ITEMS)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    protected void sendSplitReplacementEmails(User user, Claim claim, ClaimItem claimItem, String regNumber, String accountNumber){

        replacement(claim, claimItem, regNumber, accountNumber)
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME)
                )
                .cancel()
                .viewMail(MailsPage.MailType.REPLACEMENT_WITH_MAIL, ORDER_CONFIRMATION)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ORDER_CONFIRMATION));

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    protected void schemaValidation(String companyName, String clamNumber){

        communicationDesignerMock.getStub(companyName)
                .doValidation(schemaValidation ->
                        schemaValidation.validateTemplateGenerateSchema(clamNumber));
    }
}
