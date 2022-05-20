package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.MailViewDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.types.SortType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class MailsPage extends BaseClaimPage {

    @FindBy(id = "subject_column")
    private SelenideElement subjectColumnHeader;
    @FindBy(id = "type_column")
    private SelenideElement typeColumnHeader;
    @FindBy(id = "date_column")
    private SelenideElement dateColumnHeader;
    @FindBy(xpath = "//table[1]//td[1]")
    private SelenideElement latestMailSubject;

    public MailsPage sortByDate(SortType sortType) {

        dateColumnHeader.click();
        int totalAttempts = 2;
        int currentAttempt = 0;
        while (currentAttempt < totalAttempts) {

            dateColumnHeader.click();
            Boolean isDisplayed = Wait.forCondition(webDriver -> sortType.isExtJsValueEqualTo(dateColumnHeader.getAttribute("aria-sort")));

            if (isDisplayed) {

                break;
            }
            currentAttempt++;
        }
        return this;
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/matching_engine/customer_mails.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        latestMailSubject.should(Condition.visible);
    }

    public Mails parseMails() {

        ElementsCollection elements = $$(By.cssSelector(".x-grid-item tbody"));
        List<Mail> mailRows = new ArrayList<>();

        for (SelenideElement element : elements) {

            String text = element.find(By.xpath(".//td[contains(@data-columnid,'subject_column')]")).getText();
            String type = element.find(By.xpath(".//td[contains(@data-columnid,'type_column')]")).getText();
            String dateValue = element.find(By.xpath(".//td[contains(@data-columnid,'date_column')]")).getText();
            LocalDateTime sentDate = LocalDateTime.parse(dateValue, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            SelenideElement viewMailButton = element.find(By.xpath(".//a[contains(@class,'viewMailButtonCls ')]//span[contains(@class, 'x-btn-inner')]"));
            mailRows.add(new Mail(text, type, sentDate, viewMailButton));
        }
        return new Mails(mailRows);
    }

    public MailViewDialog viewMail(MailType mailType) {

        return parseMails().findMailByType(mailType).viewMail();
    }

    public MailViewDialog viewMail(MailType mailType, String mailSubject) {

        MailViewDialog mailViewDialog = null;

        int counter = 1;
        do {

            try {

                mailViewDialog = parseMails().findMailsByTypeAndSubject(mailType, mailSubject).viewMail();
                counter = 0;
            } catch (Exception e) {

                logger.warn("View mail exception: {}", e);
                refresh();
                at(MailsPage.class);
            }
        }while (counter-- > 0);

        return mailViewDialog;
    }

    public Mail getLatestMail(MailType mailType) {


        return parseMails().findMailByType(mailType);
    }

    public static class Mails {

        public static final Logger log = LogManager.getLogger(Mails.class);

        private List<Mail> mails = new ArrayList<>();

        public Mails(List<Mail> mails) {

            this.mails = mails;
            mails.sort((o1, o2) -> o2.getSentDate().compareTo(o1.getSentDate()));
        }

        public List<Mail> getMails() {

            return mails;
        }

        public Mail findMailByType(MailType mailType) {

            for (Mail mail : mails) {

                if (mailType.equals(mail.getMailType())) {
                    return mail;
                }
            }

            log.error("Can't find appropriate MailType");
            log.error("--------- Existing --------------");
            for (Mail mail : mails) {

                log.info(mail.getSubject() + " --> " + mail.getMailType());
            }

            log.error("-------------------------------");
            return null;
        }

        public Mail findMailsByTypeAndSubject(MailType mailType, String mailSubject) {

            return mails
                    .stream()
                    .filter(mail -> mailType.equals(mail.getMailType()))
                    .filter(mail -> mailSubject.equals(mail.getSubject()))
                    .findFirst()
                    .get();
        }
    }

    public static class Mail {

        private String subject;
        private LocalDateTime sentDate;
        private WebElement viewMailButton;
        private MailType mailType;

        public Mail(String subject, String type, LocalDateTime sentDate, WebElement viewMailButton) {

            this.subject = subject;
            this.mailType = MailType.findByText(type);
            this.sentDate = sentDate;
            this.viewMailButton = viewMailButton;
        }

        public String getSubject() {

            return subject;
        }

        public LocalDateTime getSentDate() {

            return sentDate;
        }

        public MailViewDialog viewMail() {

            Wait.waitForAjaxCompletedAndJsRecalculation();
            $(viewMailButton)
                    .hover()
                    .click();
            return BaseDialog.at(MailViewDialog.class);
        }

        public MailType getMailType() {

            return mailType;
        }
    }

    public enum MailType {

        SETTLEMENT_NOTIFICATION_TO_IC("Opgørelsesnotifikation (skadeafslutning)"),
        SETTLEMENT_NOTIFICATION_TO_SP("Opgørelsesnotifikation (refusion)"),
        SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL("Opgørelsesnotifikation (ekstern)"),
        CUSTOMER_WELCOME("Kundemail (opgørelse)"),
        ORDER_CONFIRMATION("Ordrebekræftelse"),
        REPLACEMENT_WITH_MAIL("Genlevering"),
        INVOICE_TO_IC("Ordre bekræftigelse (kopi til forsikringsselskab)"),
        PAYOUT_NOTIFICATION_TO_IC("Udbetalingsbekræftelse (til selskab)"),
        PAYOUT_NOTIFICATION_TO_CH("Udbetalingsbekræftelse (til skadebehandler)"),
        SELFSERVICE_CUSTOMER_WELCOME("Kundemail (adgang til selvbetjening)"),
        SELFSERVICE_CUSTOMER_NOTIFICATION("Kundemail (selvbetjeningsbekræftelse)"),
        SELFSERVICE_IC_NOTIFICATION("Selskabsmail (selvbetjeningsbekræftelse)"),
        PROCURA_APPROVAL_REQUEST("Anmodning om godkendelse af anvisningsret"),
        PROCURA_DECISION_NOTIFICATION("Afgørelse vedrørende anvisningsret"),
        REPAIR_AND_VALUATION("Repair And Valuation"),
        REMINDER_MAIL("Påmindelse vedr. lukning af adgang"),
        BLOCKED_ACCOUNT("Lukning af adgang"),
        ITEMIZATION_CUSTOMER_MAIL("Kundemail (fnol)"),
        ITEMIZATION_CONFIRMATION_IC_MAIL("Selskabsmail (fnol)"),
        LOSS_ADJUSTER_SHEET("Loss adjuster sheet mail");

        private String typeText;

        MailType(String typeText) {

            this.typeText = typeText;
        }

        public static MailType findByText(String typeText) {

            for (MailType mailType : MailType.values()) {

                if (mailType.typeText.equals(typeText)) {

                    return mailType;
                }
            }
            return null;
        }
    }

    public MailsPage doAssert(Consumer<MailsPage.Asserts> assertFunc) {

        assertFunc.accept(new MailsPage.Asserts());
        return MailsPage.this;
    }

    public class Asserts {

        public void isMailExist(MailType mailType) {

            assertThat(parseMails().findMailByType(mailType))
                    .as("the following mail should be present: " + mailType, parseMails().findMailByType(mailType))
                    .isNotNull();
        }

        public void noMailsOnThePage() {

            assertThat(parseMails().getMails().isEmpty())
                    .as("there should be no mails on Mails page")
                    .isTrue();
        }

        public void isMailExist(MailType mailType, String subject) {

            isMailExist(mailType);
            String latestMailSubject = getLatestMail(mailType).getSubject();
            assertThat(latestMailSubject.equals(subject)).as("expected mail subject: " + subject + "but was: " + latestMailSubject).isTrue();
        }

        public void noOtherMailsOnThePage(List<MailType> expectedMails){

            List<MailType> mails = parseMails().getMails().stream().map(mail -> mail.getMailType()).collect(Collectors.toList());
            assertThat(mails.containsAll(expectedMails))
                    .as(String.format("Following mails should be sent: %s, but were: %s", expectedMails, mails))
                    .isTrue();
            mails.removeAll(expectedMails);
            assertThat(mails.isEmpty())
                    .as("Following mails should not be sent %s", mails)
                    .isTrue();
        }
    }
}

