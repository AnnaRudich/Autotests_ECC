package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.ClaimOperationsMenu;
import com.scalepoint.automation.pageobjects.modules.FunctionalMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.modules.ToolBarMenu;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvTaskWizardPage1;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Constants.AGE_MONTH;
import static com.scalepoint.automation.utils.Constants.AGE_YEAR;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.invisible;
import static com.scalepoint.automation.utils.Wait.visible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@ClaimSpecificPage
@EccPage
public class SettlementPage extends BaseClaimPage {

    @FindBy(css = "#settlementGrid-body table:first-child")
    private Table firstClaim;
    @FindBy(css = ".x-grid-cell-descriptionColumn")
    private List<WebElement> claimLineDescription;
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


    private By groupButton = By.xpath("//span[text()='Opret gruppe']");
    private By deleteGroupButton = By.xpath("//span[text()='Opløs gruppe']");
    private By rejectButton = By.xpath("//span[contains(@style,'rejectIcon.png')]");


    private String sendNotToRepairLineIconByDescriptionXpath =
            "//span[contains(text(), '$1')]/ancestor::tr/td[contains(@class, 'repairValuationColumn')]//img[contains(@src, 'view.png')]";

    private String lockForRepairLineIconByDescriptionXpath =
            "//span[contains(text(), '$1')]/ancestor::tr/td[contains(@class, 'repairValuationColumn')]//img[contains(@src, 'wrench.png')]";

    private String byDescriptionItemsXpath =
            "//td[contains(@class,'descriptionColumn')][contains(.,'$1')]//span";


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
        By claimLineXpath = By.xpath(".//*[@id='settlementGrid-body']//table//span[contains(text(), '" + description + "')]/ancestor::table | .//*[@id='settlementTreeGrid-body']//table//span[contains(text(), '" + description + "')]/ancestor::table");
        Wait.waitForDisplayed(claimLineXpath);
        Table table = new Table(driver.findElement(claimLineXpath));
        return new ClaimLine(table);
    }

    public ClaimLine parseFirstClaimLine() {
        By claimLineXpath = By.xpath(".//*[@id='settlementGrid-body']//table//tr[1]/ancestor::table[1] | .//*[@id='settlementTreeGrid-body']//table//tr[1]/ancestor::table[1]");
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

    public SettlementPage addLines(ClaimItem claimItem, String... lineDescriptions) {
        for (String lineDescription : lineDescriptions) {
            openSidAndFill(sid -> sid
                    .withText(lineDescription)
                    .withNewPrice(PRICE_2400)
                    .withCategory(claimItem.getCategoryGroupBorn())
                    .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                    .withAge(AGE_YEAR, AGE_MONTH))
                    .closeSidWithOk();
        }
        return this;
    }


    public void cancelClaim() {
        settlementSummary.cancel();
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
        waitForAjaxCompleted();
        settlementSummary.completeClaim();
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
            logger.info("No alert is present");
        }
        return at(CompleteClaimPage.class);
    }

    public SettlementGroupDialog openGroupCreationDialog(){
        $(groupButton).click();
        return BaseDialog.at(SettlementGroupDialog.class);
    }

    public SettlementPage rejectLines(){
        $(rejectButton).click();
        waitForAjaxCompleted();
        return this;
    }

    public SettlementPage deleteGroup(){
        $(deleteGroupButton).click();
        waitForAjaxCompleted();
        $(By.xpath("//span[text() = 'Ja']")).click();
        return this;
    }

    public SettlementPage selectLinesByIndex(int... lines) {
        try {
            selectLines(lines, claimLineDescription);
        }catch (IndexOutOfBoundsException e){
            logger.error(e.getMessage());
        }
        return this;
    }

    public SettlementPage selectLinesByDescriptions(String... descriptions) {
        try {
            Arrays.stream(descriptions).forEach(desc -> new Actions(driver)
                    .keyDown(Keys.CONTROL)
                    .click(claimLineDescription.stream().filter(line -> line.getText().equals(desc)).findFirst().get())
                    .keyUp(Keys.CONTROL).build().perform());
        }catch (IndexOutOfBoundsException e){
            logger.error(e.getMessage());
        }
        return this;
    }

    private void selectLines(int[] lines, List<WebElement> element) {
        Arrays.stream(lines).forEach(line -> new Actions(driver)
                .keyDown(Keys.CONTROL)
                .click(element.get(line))
                .keyUp(Keys.CONTROL).build().perform());
    }

    public boolean isItemPresent(String item) {
        List<WebElement> claims = claimLineDescription;
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

    public List<ClaimLine> getLinesByDescription(String... descriptions) {
        return Arrays.stream(descriptions).map(this::findClaimLine).collect(Collectors.toList());
    }

    public SettlementPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return SettlementPage.this;
    }

    public SettlementPage moveLineFromGroupToGroup(String claimLineDescription, String groupName) {
        new Actions(driver).dragAndDrop(findClaimLine(claimLineDescription).descriptionElement, findClaimLine(groupName).descriptionElement).build().perform();
        waitForAjaxCompleted();
        return this;
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

        public Asserts assertFirstLineIsRejected() {
            assertThat($(By.xpath("(.//*[@id='settlementGrid-body']//table//tr[1])")).getAttribute("class")).contains("rejected");
            return this;
        }

        public Asserts assertSettlementPageIsInFlatView(){
            assertThat(invisible($(By.xpath("//div[contains(@class, 'x-tree-view')]")))).isTrue();
            return this;
        }

        public Asserts assertSettlementPageIsNotInFlatView(){
            assertThat(visible($(By.xpath("//div[contains(@class, 'x-tree-view')]")))).isTrue();
            return this;
        }

        public Asserts assertSettlementContainsLinesWithDescriptions(String... descriptions){
            assertThat(Arrays.stream(descriptions).anyMatch(desc -> claimLineDescription.stream().anyMatch(claim -> claim.getText().equals(desc)))).isTrue();
            return this;
        }
    }


    public class ClaimLine {

        private Table claimLine;

        private String tooltip = "";
        private String description;
        private String category;
        private int quantity;
        private String age;
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

            this.age  = claimLine.findElement(By.xpath(".//*[@data-columnid='settlementAgeColumn']")).getText();

            try {
                purchasePrice = OperationalUtils.getDoubleValue(claimLine.findElement(By.xpath(".//*[@data-columnid='totalPurchasePriceColumn']")).getText());
            }catch (Exception e){
                logger.warn(e.getMessage());
            }
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

        public SettlementGroupDialog editGroup() {
            doubleClickClaimLine();
            return BaseDialog.at(SettlementGroupDialog.class);
        }

        public SettlementDialog editLine() {
            doubleClickClaimLine();
            return BaseDialog.at(SettlementDialog.class);
        }

        private void doubleClickClaimLine() {
            String dblClick = "var targLink    = arguments[0];\n" +
                    "var clickEvent  = document.createEvent ('MouseEvents');\n" +
                    "clickEvent.initEvent ('dblclick', true, true);\n" +
                    "targLink.dispatchEvent (clickEvent);";

            try {
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
            }catch (ScriptTimeoutException e){
                logger.error(e.getMessage());
                ((JavascriptExecutor) driver).executeScript(dblClick,descriptionElement);
            }
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

        public String getAge() {
            return age;
        }

        public int getDepreciation() {
            return depreciation;
        }

        public Table getClaimLine() {
            return claimLine;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public double getVoucherPurchaseAmount() {
            return voucherPurchaseAmount;
        }

        public String getActualColor() {
            return actualColor;
        }

        public String getComputedColor() {
            return computedColor;
        }

        public WebElement getDescriptionElement() {
            return descriptionElement;
        }

        public double getReplacementPrice() {
            return replacementPrice;
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

            public Asserts assertAttachmentsIconIsDisplayed(){
                boolean attachmentsIconPresent = claimLine.findElement(By.xpath(".//*[@data-columnid='hasAttachmentColumn']//img[contains(@src, 'paperclip.png')]")).isDisplayed();
                assertTrue(attachmentsIconPresent, "Attachment icon should be displayed");
                return this;
            }

            public Asserts assertQuantityIs(int quantity){
                assertThat(getQuantity()).isEqualTo(quantity);
                return this;
            }

            public Asserts assertAgeIs(String age){
                assertThat(getAge()).containsIgnoringCase(age);
                return this;
            }

            public Asserts assertDepreciationIs(int depreciation) {
                assertThat(getDepreciation()).isEqualTo(depreciation);
                return this;
            }

            public Asserts assertClaimLineIsCrossedOut(){
                assertThat(claimLine.findElement(By.xpath(".//*[@data-columnid='totalPurchasePriceColumn']/div")).getAttribute("style")).containsIgnoringCase("line-through");
                assertThat(claimLine.findElement(By.xpath(".//*[@data-columnid='replacementAmountColumn']/div")).getAttribute("style")).containsIgnoringCase("line-through");
                assertThat(claimLine.findElement(By.xpath(".//*[@data-columnid='depreciationColumn']/div")).getAttribute("style")).containsIgnoringCase("line-through");
                return this;
            }

            public Asserts assertClaimLineIsRejected() {
                assertThat(claimLine.findElement(By.xpath(".//tr")).getAttribute("class")).containsIgnoringCase("rejected");
                return this;
            }

            public Asserts assertVoucherIconIsDisplayed() {
                assertThat(claimLine.findElement(By.xpath(".//img[contains(@src, 'icons/voucherIcon.png')]")).isDisplayed()).isTrue();
                return this;
            }

            public Asserts assertVoucherTooltipContains(String text) {
                assertThat(tooltip).containsIgnoringCase(text);
                return this;
            }
        }
    }
}
