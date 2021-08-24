package com.scalepoint.automation.tests.communicationDesigner;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.communicationDesignerEmailTemplates.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.BankAccount;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.to;

public class CommunicationDesignerBaseTests extends BaseTest {

    protected static String oneAttachment;
    protected static String twoAttachments;


    @BeforeMethod
    public void setCommunicationDesignerSection(Object[] objects) throws IOException {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        CommunicationDesigner communicationDesigner = getLisOfObjectByClass(parameters, CommunicationDesigner.class).get(0);
        CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates = getLisOfObjectByClass(parameters, CommunicationDesignerEmailTemplates.class).get(0);

        String title = communicationDesignerEmailTemplates.getEmailTemplateByClass(AttachmentEmailTemplate.class).getTemplateName();

        oneAttachment = title;
        twoAttachments = String.format("%s,%s", title, title);

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

    protected void sendSelfServiceCustomerWelcomeEmail(Claim claim, String companyCode, String password,
                                                       SelfServiceEmailTemplate selfServiceEmailTemplate){

        String title = selfServiceEmailTemplate
                .getTitle();

        MailsPage mailsPage = Page.at(SettlementPage.class)
                .requestSelfService(claim, password)
                .toMailsPage();

        mailsPage
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME, title)
                .doAssert(mailViewDialog -> mailViewDialog
                        .isTextVisible(title
                        ));

        schemaValidation(companyCode.toLowerCase(), claim.getClaimNumber());
    }

    protected void sendItemizationSubmitAndSaveLossItemsEmails(User user, Claim claim, ClaimItem claimItem,
                                                               CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                               String password, String month, Double newPrice){

        String claimLineDescription = claimItem.getSetDialogTextMatch();
        String itemizationSubmitLossItemsTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(ItemizationSubmitLossItemsEmailTemplate.class)
                .getTitle();
        String itemizationSaveLossItemsTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(ItemizationSaveLossItemsEmailTemplate.class)
                .getTitle();

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
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL, itemizationSubmitLossItemsTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(itemizationSubmitLossItemsTitle)
                )
                .cancel()
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL, itemizationSaveLossItemsTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(itemizationSaveLossItemsTitle)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    protected void sendSplitReplacementEmails(User user, Claim claim, ClaimItem claimItem, BankAccount bankAccount,
                                              CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates){

        String customerWelcomeTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(CustomerWelcomeEmailTemplate.class)
                .getTitle();

        String orderConfirmationTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(OrderConfirmationEmailTemplate.class)
                .getTitle();

        replacement(claim, claimItem, bankAccount.getRegNumber(), bankAccount.getRegNumber())
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, customerWelcomeTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(customerWelcomeTitle)
                )
                .cancel()
                .viewMail(MailsPage.MailType.REPLACEMENT_WITH_MAIL, orderConfirmationTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(orderConfirmationTitle));

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    protected void schemaValidation(String companyName, String clamNumber){

        communicationDesignerMock.getStub(companyName)
                .doValidation(schemaValidation ->
                        schemaValidation.validateTemplateGenerateSchema(clamNumber));
    }
}
