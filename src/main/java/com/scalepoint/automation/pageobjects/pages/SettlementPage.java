package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.*;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvTaskWizardPage1;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.scalepoint.automation.utils.Wait.waitForElementDisplaying;
import static com.scalepoint.automation.utils.Wait.waitForStableElements;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class SettlementPage extends BaseClaimPage {

    @FindBy(css = "#settlementGrid-body table:first-child")
    private Table firstClaim;
    @FindBy(css = ".x-grid-cell-descriptionColumn")
    private List<WebElement> claimDescription;
    @FindBy(css = ".x-grid-cell-claimLineIDColumn")
    private List<WebElement> claimLineID;
    @FindBy(id = "settlementSummaryTotalsPanel-body")
    private BottomMenu settlementConclusion;
    @FindBy(xpath = "//td[contains(@class,'voucherImageColumn')]//img")
    private WebElement iconToolTip;
    @FindBy(xpath = "//a[contains(@id, 'button-')][1]")
    private WebElement ok;
    @FindBy(id = "_OK_button")
    private Button _import;

    @FindBy(xpath = "//span[contains(@style, 'selectAllIcon.png')]")
    private WebElement selectAllClaims;

    private String sendNotToRepairLineIconByDescriptionXpath = "//span[contains(text(), '$1')]/ancestor::tr/td[contains(@class, 'repairValuationColumn')]//img[contains(@src, 'view.png')]";
    private String lockForRepairLineIconByDescriptionXpath = "//span[contains(text(), '$1')]/ancestor::tr/td[contains(@class, 'repairValuationColumn')]//img[contains(@src, 'wrench.png')]";
    private String byDescriptionItemsXpath = "//td[contains(@class,'descriptionColumn')][contains(.,'$1')]//span";

    private BottomMenu bottomMenu = new BottomMenu();

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
        Table table = new Table(driver.findElement
                (By.xpath(".//*[@id='settlementGrid-body']//table//span[contains(text(), '" + description + "')]/ancestor::table")));
        return new ClaimLine(table);
    }

    public SettlementPage selectAllLines() {
        selectAllClaims.click();
        return this;
    }

    public SettlementPage requestSelfService(Claim claim, String password) {
        claimOperationsMenu.requestSelfService().
                fill(claim, password).
                send();
        return this;
    }

    public SettlementPage addGenericItemToClaim(GenericItem genericItem) {
        claimOperationsMenu.addGenericItem().
                chooseItem(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
        return this;
    }

    public ClaimOperationsMenu getClaimOperationsMenu() {
        return claimOperationsMenu;
    }

    public SettlementPage assertGenericItemIsNotPresent(GenericItem genericItem) {
        claimOperationsMenu.addGenericItem().assertGenericItemIsNotPresent(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
        return this;
    }

    public ToolBarMenu getToolBarMenu() {
        return toolBarMenu;
    }

    public BottomMenu getBottomMenu() {
        return bottomMenu;
    }

    public TextSearchPage toTextSearchPage() {
        functionalMenu.findInCatalogue();
        return Page.at(TextSearchPage.class);
    }

    public TextSearchPage toTextSearchPage(String text) {
        return toTextSearchPage().searchByProductName(text);
    }

    public SettlementDialog addManually() {
        return functionalMenu.addManually();
    }

    public SettlementPage addManually(String description, String category, String subcategory, int newPrice) {
        return addManually()
                .fillBaseData(description, category, subcategory, newPrice)
                .ok();
    }

    public void cancelClaim() {
        bottomMenu.cancel();
    }

    public MyPage saveClaim() {
        bottomMenu.saveClaim();
        return at(MyPage.class);
    }

    public CompleteClaimPage toCompleteClaimPage() {
        bottomMenu.completeClaim();
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
            logger.info("No alert is present");
        }
        return at(CompleteClaimPage.class);
    }

    public boolean isItemPresent(String _item) {
        List<WebElement> claims = claimDescription;
        return claims.stream().anyMatch(claim -> claim.getText().equals(_item));
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

    public SettlementPage assertSettlementPagePresent(String message) {
        Assert.assertTrue(isSettlementPagePresent(), message);
        return this;
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

    /*------------------------------ ASSERTS ---------------------------------------*/
    /*------------------------------ ------- ---------------------------------------*/
    public SettlementPage assertItemIsPresent(String claimLineDescription) {
        Assert.assertTrue(isItemPresent(claimLineDescription),
                errorMessage("The claim item [%s] is not found", claimLineDescription));
        return this;
    }

    public class ClaimLine {

        private boolean voucherPresent;
        private boolean discretionaryPresent;
        private String tooltip = "";
        private String description;
        private String category;
        private int quantity;
        private int age;
        private double purchasePrice;
        private int depreciation;
        private double replacementAmount;
        private String actualColor;
        private String computedColor;
        private boolean lineSentToRepair;
        private boolean lineSentToValuation;

        private WebElement descriptionElement;

        public ClaimLine(Table claimLine) {
            this.voucherPresent = claimLine.findElements(By.xpath(".//*[@data-columnid='voucherImageColumn']//img[contains(@src, 'voucherIcon.png')]")).size() > 0;
            this.discretionaryPresent = claimLine.findElements(By.xpath(".//*[@data-columnid='voucherImageColumn']//img[contains(@src, 'discretionary_icon.png')]")).size() > 0;
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
            replacementAmount = OperationalUtils.getDoubleValue(claimLine.findElement(By.xpath(".//*[@data-columnid='replacementAmountColumn']")).getText());

            this.lineSentToRepair = claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'wrench.png')]")).size() > 0;
            this.lineSentToValuation = claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'view.png')]")).size() > 0;
        }

        public SettlementPage selectLine() {
            descriptionElement.click();
            return SettlementPage.this;
        }

        public SettlementDialog editLine() {
            doubleClick(descriptionElement);
            driver.manage().timeouts().setScriptTimeout(150, TimeUnit.SECONDS);
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

        public boolean isVoucherPresent() {
            return voucherPresent;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getAge() {
            return age;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public int getDepreciation() {
            return depreciation;
        }

        public double getReplacementAmount() {
            return replacementAmount;
        }

        public String getActualColor() {
            return actualColor;
        }

        public String getComputedColor() {
            return computedColor;
        }

        public boolean isDiscretionaryPresent() {
            return discretionaryPresent;
        }

        public boolean hasColor(String color) {
            return color.equals(getActualColor());
        }

        public boolean hasComputedColor(String color) {
            return color.equals(getComputedColor());
        }

        public boolean isTooltipPresent(String expectedText) {
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
    }
}
