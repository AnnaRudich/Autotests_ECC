package com.scalepoint.automation.pageobjects.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.stubs.MailserviceMock;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.request.Mail;
import com.scalepoint.automation.utils.data.request.MailListItem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseClaimPage extends Page {

    private ClaimNavigationMenu claimNavigationMenu = new ClaimNavigationMenu();

    private MainMenu mainMenu = new MainMenu();

    public NotesPage toNotesPage() {
        return claimNavigationMenu.toNotesPage();
    }

//    public MailsPage toMailsPage() {
//        Wait.waitForAjaxCompletedAndJsRecalculation();
//        return claimNavigationMenu.toMailsPage();
//    }

    public Mail readMail(LoggedRequest loggedRequest){

        Mail mail = null;
        String lr = loggedRequest.getBodyAsString();
        try {
            mail = new ObjectMapper().readValue(lr, Mail.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mail;
    }

//    public MailsPage toMailsPage(){
//
//        String jSessionID = Browser.cookies().get("JSESSIONID");
//        Integer userId = Integer.valueOf(CurrentUser.getClaimId());
//
//        await()
//                .pollInterval(5, TimeUnit.SECONDS)
//                .timeout(90, TimeUnit.SECONDS)
//                .until(() -> allMailsSent(jSessionID, userId),
//                        equalTo(true));
//
//        Wait.waitForAjaxCompletedAndJsRecalculation();
//        return claimNavigationMenu.toMailsPage();
//    }

    public MailsPage toMailsPage(MailserviceMock.MailserviceStub mailserviceStub) {
        List<LoggedRequest> sentEmails = mailserviceStub.findSentEmails();

        String test = sentEmails.get(0).getBodyAsString();
        Mail mail = null;
        try {
            mail = new ObjectMapper().readValue(test, Mail.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Mail> mails = sentEmails.stream()
                .map(loggedRequest -> readMail(loggedRequest))
                .collect(Collectors.toList());

        logger.info("Mail get Case ID: " + mail.getClaimNumber());

        List<MailListItem> mailListItems = mails.stream()
                .map(m -> MailListItem.builder()
                        .date(LocalDateTime.now().toString())
                        .eventType(m.getEventType())
                        .status(3)
                        .subject(m.getSubject())
                        .token(m.getCaseId())
                        .type(m.getMailType())
                        .build())
                .collect(Collectors.toList());

        mailserviceStub.forCase(mailListItems);
//        mails.stream()
//                .filter(m -> m.getMailType().equals("SELFSERVICE_CUSTOMER_WELCOME"))
//                .forEach(m -> mailserviceStub.test4(m, databaseApi));

        Wait.waitForAjaxCompletedAndJsRecalculation();
        return claimNavigationMenu.toMailsPage();
    }

    public MailsPage toEmptyMailsPage() {
        return claimNavigationMenu.toEmptyMailsPage();
    }

    public OrderDetailsPage toOrdersDetailsPage() {
        return claimNavigationMenu.toOrderDetailsPage();
    }

    public CustomerDetailsPage toCustomerDetails() {
        return claimNavigationMenu.toCustomerDetailsPage();
    }

    public AdminPage toAdminPage() {
        mainMenu.toAdminPage();
        return at(AdminPage.class);
    }

    public ClaimSearchPage toClaimSearchPage() {
        mainMenu.openClaimSearch();
        return at(ClaimSearchPage.class);
    }


    public ProjectsPage toRepairValuationProjectsPage() {
        return claimNavigationMenu.toRepairValuationProjectsPage();
    }

    public void toSuppliersPage() {
        mainMenu.toEccAdminPage();
    }

    public SettlementPage toSettlementPageUsingNavigationMenu() {
        claimNavigationMenu.toSettlementPage();
        return at(SettlementPage.class);
    }

    public SettlementPage toSettlementPage() {
        return claimNavigationMenu.toSettlementPage();
    }
}
