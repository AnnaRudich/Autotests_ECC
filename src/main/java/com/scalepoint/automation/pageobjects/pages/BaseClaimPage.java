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
import com.scalepoint.automation.utils.threadlocal.CurrentUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
        mailserviceStub.findSentEmails(CurrentUser.getClaimId());
//        mailserviceStub.forCase(CurrentUser.getClaimId(), mailList);
//        String test = sentEmails.get(0).getBodyAsString();
//        Mail mail = null;
//        try {
//            mail = new ObjectMapper().readValue(test, Mail.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<Mail> mails = sentEmails.stream()
//                .map(loggedRequest -> readMail(loggedRequest))
////                .filter(m -> m.getCaseId().equals())
//                .collect(Collectors.toList());
//
//        String claimId = CurrentUser.getClaimId();
//
//        logger.info("Mail get Case ID: " + mail.getClaimNumber());
//
//        List<MailListItem> mailListItems = mails.stream()
//                .map(m -> MailListItem.builder()
//                        .date(LocalDateTime.now().toString())
//                        .eventType(m.getEventType())
//                        .status(3)
//                        .subject(m.getSubject())
//                        .token(UUID.randomUUID().toString())
//                        .type(m.getMailType())
//                        .build())
//                .collect(Collectors.toList());
//
//        Wait.waitMillis(2000);
//        mailserviceStub.forCase(userToken, mailListItems);
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
