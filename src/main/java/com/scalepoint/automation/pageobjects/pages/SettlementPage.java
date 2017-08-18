package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.*;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvTaskWizardPage1;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;
import java.util.function.Consumer;

import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@ClaimSpecificPage
@EccPage
public class SettlementPage extends BaseClaimPage {

    @FindBy(css = "#settlementGrid-body table:first-child")
    private Table firstClaim;
    @FindBy(css = ".x-grid-cell-descriptionColumn")
    private List<WebElement> claimDescription;
    @FindBy(css = ".x-grid-cell-claimLineIDColumn")
    private List<WebElement> claimLineID;
    @FindBy(id = "settlementSummaryTotalsPanel-body")
    private SettlementSummary settlementConclusion;
    @FindBy(xpath = "//td[contains(@class,'voucherImageColumn')]//img")
    private WebElement iconToolTip;
    @FindBy(xpath = "//a[contains(@id, 'button-')][1]")
    private WebElement ok;
    @FindBy(id = "_OK_button")
    private Button _import;
    @FindBy(id = "draft-status-inputEl")
    private WebElement auditStatus;
    @FindBy(id = "auditInfoPanel")
    private WebElement auditInfoPanel;

    @FindBy(xpath = "//span[contains(@style, 'selectAllIcon.png')]")
    private WebElement selectAllClaims;

    private String sendNotToRepairLineIconByDescriptionXpath = "//span[contains(text(), '$1')]/ancestor::tr/td[contains(@class, 'repairValuationColumn')]//img[contains(@src, 'view.png')]";
    private String lockForRepairLineIconByDescriptionXpath = "//span[contains(text(), '$1')]/ancestor::tr/td[contains(@class, 'repairValuationColumn')]//img[contains(@src, 'wrench.png')]";
    private String byDescriptionItemsXpath = "//td[contains(@class,'descriptionColumn')][contains(.,'$1')]//span";

    private SettlementSummary settlementSummary = new SettlementSummary();

    private FunctionalMenu functionalMenu = new FunctionalMenu();

    private ToolBarMenu toolBarMenu = new ToolBarMenu();

    private ClaimOperationsMenu claimOperationsMenu = new ClaimOperationsMenu();

    private MainMenu mainMenu = new MainMenu();

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/settlement.jsp";
    }

    @Override
    public SettlementPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(ok);
        return this;
    }

    public ClaimLine findClaimLine(String description) {
        By claimLineXpath = By.xpath(".//*[@id='settlementGrid-body']//table//span[contains(text(), '" + description + "')]/ancestor::table");
        Wait.waitForDisplayed(claimLineXpath);
        Table table = new Table(driver.findElement(claimLineXpath));
        return new ClaimLine(table);
    }

    public ClaimLine parseFirstClaimLine() {
        By claimLineXpath = By.xpath("(.//*[@id='settlementGrid-body']//table//tr[1]/ancestor::table)[1]");
        Wait.waitForDisplayed(claimLineXpath);
        Table table = new Table(driver.findElement(claimLineXpath));
        return new ClaimLine(table);
    }

    public SettlementDialog editFirstClaimLine() {
        return parseFirstClaimLine().editLine();
    }

    public SettlementPage selectAllLines() {
        selectAllClaims.click();
        return this;
    }

    public SettlementPage requestSelfService(Claim claim, String password) {
        return claimOperationsMenu.requestSelfService().
                fill(claim, password).
                send();
    }

    public SettlementPage requestSelfService(String password) {
        return claimOperationsMenu.requestSelfService().
                fill(password).
                send();
    }

    public SettlementPage requestSelfServiceWithEnabledAutoClose(Claim claim, String password) {
        return claimOperationsMenu.requestSelfService()
                .fill(claim, password)
                .enableAutoClose()
                .send();
    }

    public SettlementPage addGenericItemToClaim(GenericItem genericItem) {
        return claimOperationsMenu.addGenericItem().
                chooseItem(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
    }

    public ImportDialog openImportSelfServiceDialog() {
        return claimOperationsMenu.openImportDialog();
    }

    public SettlementPage ensureAuditInfoPanelVisible() {
        waitForVisible(auditInfoPanel);
        return this;
    }

    public SettlementPage checkStatusFromAudit(String status){
        ExpectedConditions.textToBePresentInElement(auditStatus, status);
        return this;
    }

    public ClaimOperationsMenu getClaimOperationsMenu() {
        return claimOperationsMenu;
    }

    public ToolBarMenu getToolBarMenu() {
        return toolBarMenu;
    }

    public SettlementSummary getSettlementSummary() {
        return settlementSummary;
    }

    public TextSearchPage toTextSearchPage() {
        functionalMenu.findInCatalogue();
        return Page.at(TextSearchPage.class);
    }

    public TextSearchPage toTextSearchPage(String text) {
        return toTextSearchPage().searchByProductName(text);
    }

    public SettlementDialog openSid() {
        return functionalMenu.addManually();
    }

    public SettlementPage openSid(String description, String category, String subcategory, Double newPrice) {
        return openSid()
                .fill(description, category, subcategory, newPrice)
                .closeSidWithOk();
    }

    public SettlementDialog openSidAndFill(Consumer<SettlementDialog.FormFiller> fillfunc) {
        return openSid().setDescription(Constants.TEXT_LINE).fill(fillfunc);
    }

    public void cancelClaim() {
        settlementSummary.cancel();
    }

    public SettlementPage enableAuditForIc(String icName) {
        to(InsCompaniesPage.class)
        .editCompany(icName)
        .enableAuditOptionAndSave();
        return to(SettlementPage.class);
    }

    public MyPage saveClaim() {
        settlementSummary.saveClaim();
        return at(MyPage.class);
    }

    public MyPage completeClaimWithoutMail() {
        settlementSummary.completeClaimWithoutMail();
        return at(MyPage.class);
    }

    public CompleteClaimPage toCompleteClaimPage() {
        settlementSummary.completeClaim();
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
            logger.info("No alert is present");
        }
        return at(CompleteClaimPage.class);
    }

    public boolean isItemPresent(String item) {
        List<WebElement> claims = claimDescription;
        return claims.stream().anyMatch(claim -> claim.getText().equals(item));
    }

    public boolean isSettlementPagePresent() {
        try {
            return driver.findElement(By.cssSelector("#settlementGrid-body div[id*='tableview']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public RnvTaskWizardPage1 sendToRnV() {
        toolBarMenu.sendToRepairAndValuation();
        return at(RnvTaskWizardPage1.class);
    }


    public Double getFaceTooltipValue() {
        String tooltipText = (iconToolTip.getAttribute("title")).split("\\(")[0];
        String value = tooltipText.replaceAll("[^\\.,0123456789]", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public SettlementPage importExcelFile(String filePath) {
        return functionalMenu.
                openImportExcelDialog().
                uploadExcel(filePath);
    }

    private String reviewedColor = "rgb(30, 144, 255)";
    private String excludedColor = "rgba(221, 170, 170, 1)";

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public SettlementPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return SettlementPage.this;
    }

    public class Asserts {
        public Asserts assertFaceValueTooltipIs(Double expectedPrice) {
            assertEqualsDouble(getFaceTooltipValue(), expectedPrice, "Tooltip face value %s should be assertEqualsDouble to not  depreciated new price %s");
            return this;
        }

        public Asserts assertSettlementPagePresent(String message) {
            Assert.assertTrue(isSettlementPagePresent(), message);
            return this;
        }

        public Asserts assertItemIsPresent(String claimLineDescription) {
            Assert.assertTrue(isItemPresent(claimLineDescription),
                    errorMessage("The claim item [%s] is not found", claimLineDescription));
            return this;
        }

        public Asserts assertItemNotPresent(String claimLineDescription) {
            Assert.assertFalse(isItemPresent(claimLineDescription),
                    errorMessage("The claim item [%s] must be absent: ", claimLineDescription));
            return this;
        }


        public Asserts assertGenericItemIsNotPresent(GenericItem genericItem) {
            boolean genericItemIsPresent = claimOperationsMenu.addGenericItem().isGenericItemPresent(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
            Assert.assertFalse(genericItemIsPresent);
            return this;
        }
    }


    public class ClaimLine {

        private Table claimLine;

        private String tooltip = "";
        private String description;
        private String category;
        private int quantity;
        private int age;
        private double purchasePrice;
        private int depreciation;
        private double replacementPrice;
        private double voucherPurchaseAmount;
        private String actualColor;
        private String computedColor;
        private WebElement descriptionElement;

        public ClaimLine(Table claimLine) {
            this.claimLine = claimLine;
            List<WebElement> elements = claimLine.findElements(By.xpath(".//*[@data-columnid='voucherImageColumn']//img[@title]"));
            if (elements.size() > 0) {
                this.tooltip = elements.get(0).getAttribute("title");
            }
            this.descriptionElement = claimLine.findElement(By.xpath(".//*[@data-columnid='descriptionColumn']//span"));
            this.description = descriptionElement.getText();
            this.actualColor = descriptionElement.getAttribute("style");
            this.computedColor = descriptionElement.getCssValue("color");
            this.category = claimLine.findElement(By.xpath(".//*[@data-columnid='categoryGroupColumn']")).getText();
            this.quantity = Integer.valueOf(claimLine.findElement(By.xpath(".//*[@data-columnid='quantityColumn']")).getText());

            String ageValue = claimLine.findElement(By.xpath(".//*[@data-columnid='settlementAgeColumn']")).getText();
            if (NumberUtils.isNumber(ageValue)) {
                this.age = Integer.valueOf(ageValue);
            }

            purchasePrice = OperationalUtils.getDoubleValue(claimLine.findElement(By.xpath(".//*[@data-columnid='totalPurchasePriceColumn']")).getText());
            String depreciationText = claimLine.findElement(By.xpath(".//*[@data-columnid='depreciationColumn']")).getText().replace("%", "");
            depreciation = NumberUtils.isNumber(depreciationText) ? Integer.valueOf(depreciationText) : -1;
            replacementPrice = OperationalUtils.getDoubleValue(claimLine.findElement(By.xpath(".//*[@data-columnid='replacementAmountColumn']")).getText());
            try {
                this.voucherPurchaseAmount = OperationalUtils.getDoubleValue(claimLine.findElement(By.xpath(".//*[@data-columnid='voucherPurchaseAmountValueColumn']")).getText());
            }catch (Exception e){
                logger.warn(e.getMessage());
            }

        }

        public SettlementPage selectLine() {
            descriptionElement.click();
            return SettlementPage.this;
        }

        public SettlementPage toSettlementPage() {
            return SettlementPage.this;
        }

        public SettlementDialog editLine() {
            doubleClick(descriptionElement);
            waitForAjaxCompleted();
            String js =
                    "var callback = arguments[arguments.length - 1];" +
                            "function groupsLoaded() {" +
                            "var groups = Ext.getCmp('group-combobox');" +
                            "if (!groups || (groups.getStore().count() <= 0)) {" +
                            "setTimeout(groupsLoaded, 1000);" +
                            "} else {" +
                            "callback();" +
                            "}" +
                            "}" +
                            "groupsLoaded();";
            ((JavascriptExecutor) driver).executeAsyncScript(js);
            return BaseDialog.at(SettlementDialog.class);
        }

        public String getTooltip() {
            return tooltip;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public int getAge() {
            return age;
        }

        public int getDepreciation() {
            return depreciation;
        }

        boolean isTooltipPresent(String expectedText) {
            return expectedText.equals(getTooltip());
        }

        public boolean isClaimLineSentToRepair() {
            return isElementPresent(By.xpath(lockForRepairLineIconByDescriptionXpath.replace("$1", "")));
        }

        public boolean isClaimLineSendNotToRepairAndIconDisplays() {
            return isElementPresent(By.xpath(sendNotToRepairLineIconByDescriptionXpath.replace("$1", "")));
        }

        public boolean isLineExcludedAndReviewed() {
            return descriptionElement.getAttribute("class").equals("divNotActive")
                    && descriptionElement.getCssValue("color").equals(excludedColor);
        }

        public boolean isLineIncludedAndNotReviewed() {
            return descriptionElement.getAttribute("class").equals("div")
                    && !descriptionElement.getAttribute("style").equals(reviewedColor);
        }

        public boolean isLineExcludedAndNotReviewed() {
            return descriptionElement.getAttribute("class").equals("divNotActive")
                    && !descriptionElement.getAttribute("style").equals(reviewedColor);
        }

        public ClaimLine doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return this;
        }

        public class Asserts {
            public Asserts assertDiscretionaryPresent() {
                boolean discretionaryPresent = claimLine.findElements(By.xpath(".//*[@data-columnid='voucherImageColumn']//img[contains(@src, 'discretionary_icon.png')]")).size() > 0;
                assertTrue(discretionaryPresent, "Discretionary reason icon should be displayed");
                return this;
            }

            public Asserts assertTooltipPresent(String tooltip) {
                assertTrue(isTooltipPresent(tooltip), "Discretionary Reason Tooltip should be displayed");
                return this;
            }

            public Asserts assertLineHasColor(String color) {
                assertEquals(actualColor, color, "Claim line must have color: " + color);
                return this;
            }

            public Asserts assertLineHasComputedColor(String color) {
                assertEquals(computedColor, color, "Claim line must have color: " + color);
                return this;
            }

            public Asserts assertLineIsSentToRepair() {
                boolean lineSentToRepair = claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'wrench.png')]")).size() > 0;
                Assert.assertTrue(lineSentToRepair);
                return this;
            }

            public Asserts assertLineSentToValuation() {
                boolean lineSentToValuation = claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'view.png')]")).size() > 0;
                Assert.assertTrue(lineSentToValuation);
                return this;
            }

            public Asserts assertPurchasePriceIs(double expectedPrice) {
                OperationalUtils.assertEqualsDoubleWithTolerance(purchasePrice, expectedPrice);
                return this;
            }

            public Asserts assertReplacementPriceIs(double expectedPrice) {
                OperationalUtils.assertEqualsDoubleWithTolerance(replacementPrice, expectedPrice);
                return this;
            }

            public Asserts assertVoucherPurchaseAmount(double expectedAmount) {
                OperationalUtils.assertEqualsDouble(voucherPurchaseAmount, expectedAmount, "Expected amount is: "+expectedAmount);
                return this;
            }

            public Asserts assertVoucherPresent() {
                boolean voucherPresent = claimLine.findElements(By.xpath(".//*[@data-columnid='voucherImageColumn']//img[contains(@src, 'voucherIcon.png')]")).size() > 0;
                assertTrue(voucherPresent, "Voucher icon should be displayed");
                return this;
            }

            public Asserts assertProductDetailsIconIsDisplayed(){
                boolean productInfoPresent = claimLine.findElement(By.xpath(".//*[@data-columnid='typeColumn']//img[contains(@src, 'info.png')]")).isDisplayed();
                return this;
            }
        }
    }
}
