package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.utils.Wait;

public abstract class BaseClaimPage extends Page {

    private ClaimNavigationMenu claimNavigationMenu = new ClaimNavigationMenu();

    private MainMenu mainMenu = new MainMenu();

    public NotesPage toNotesPage() {
        return claimNavigationMenu.toNotesPage();
    }

    public MailsPage toMailsPage() {
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return claimNavigationMenu.toMailsPage();
    }

//    public Mail readMail(LoggedRequest loggedRequest){
//
//        Mail mail = null;
//        String lr = loggedRequest.getBodyAsString();
//        try {
//            mail = new ObjectMapper().readValue(lr, Mail.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return mail;
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

//  public MailsPage toMailsPage(MailserviceMock.MailserviceStub mailserviceStub, DatabaseApi databaseApi) {
//        List<LoggedRequest> test2 = mailserviceStub.test2();
//
//      String test = test2.get(0).getBodyAsString();
//        Mail mail = null;
//        try {
//            mail = new ObjectMapper().readValue(test, Mail.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<Mail> mails = mailserviceStub.test2().stream()
//                .map(loggedRequest -> readMail(loggedRequest))
//                .collect(Collectors.toList());
//
//        logger.info("Mail get Case ID: " + mail.getClaimNumber());
//
//        List<MailListItem> mailListItems = mails.stream()
//                .map(m -> MailListItem.builder()
//                        .date(LocalDateTime.now().toString())
//                        .eventType(m.getEventType())
//                        .status(3)
//                        .subject(m.getSubject())
//                        .token(m.getCaseId())
//                        .type(m.getMailType())
//                        .build())
//                .collect(Collectors.toList());
//
//        mailserviceStub.test3(mailListItems);
//
//        mails.stream()
//                .filter(m -> m.getMailType().equals("SELFSERVICE_CUSTOMER_WELCOME"))
//                .forEach(m -> mailserviceStub.test4(m, databaseApi));
//
//
//
//        await()
//                .pollInterval(5, TimeUnit.SECONDS)
//                .timeout(1, TimeUnit.MINUTES)
//                .until(() -> allMailsSent(),
//                        equalTo(true));
//
//        Wait.waitForAjaxCompletedAndJsRecalculation();
//        return claimNavigationMenu.toMailsPage();
//    }

//    private boolean allMailsSent(String jSessionID, Integer userId){
//
//        Data data = new Data();
//        data.setEccSessionId(jSessionID);
//        data.setUserId(userId);
//
//        try {
//            CustomerMailListItem[] test = BaseService
//                    .setData(data)
//                    .getCustomerMailList()
//                    .setUserId(CurrentUser.getClaimId())).getCustomerMailList();
//            boolean sent =  Arrays.stream(test)
//                    .allMatch(customerMailListItem -> customerMailListItem.getStatus() == 3);
//
//            return sent;
//
//        }catch (Exception e){
//
//            logger.warn(e);
//            return false;
//        }
//
//
//
//
//    }



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
