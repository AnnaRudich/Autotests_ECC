package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.LossImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.LossLineImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.ClaimOperationsMenu;
import com.scalepoint.automation.pageobjects.modules.FunctionalMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.modules.ToolBarMenu;
import com.scalepoint.automation.pageobjects.pages.rnv.TaskWizardPage1;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import com.scalepoint.automation.utils.data.entity.PseudoCategory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Constants.AGE_MONTH;
import static com.scalepoint.automation.utils.Constants.AGE_YEAR;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.invisibleOfElement;
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
    @FindBy(id = "edit-policy-cancel-button-btnInnerEl")
    private WebElement cancelPolicy;

    @FindBy(xpath = "//span[contains(@style, 'selectAllIcon.png')]")
    private WebElement selectAllClaims;

    private By groupButton = By.xpath("//span[contains(@style,'groupIcon.png')]");
    private By deleteGroupButton = By.xpath("//span[contains(@style,'ungroupIcon.png')]");
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
        Table table = new Table($(claimLineXpath));
        return new ClaimLine(table);
    }

    public ClaimLine parseFirstClaimLine() {
        By claimLineXpath = By.xpath(".//*[@id='settlementGrid-body']//table//tr[1]/ancestor::table[1] | .//*[@id='settlementTreeGrid-body']//table//tr[1]/ancestor::table[1]");
        Table table = new Table($(claimLineXpath));
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

    public SettlementPage requestSelfServiceWithEnabledNewPassword(Claim claim, String password) {
        return claimOperationsMenu.requestSelfService()
                .enableNewPassword()
                .fill(claim, password)
                .send();
    }

    public SettlementPage addGenericItemToClaim(GenericItem genericItem) {
        return claimOperationsMenu.addGenericItem().
                chooseItem(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
    }

    public LossImportDialog openImportSelfServiceDialog() {
        return claimOperationsMenu.openImportDialog();
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
        boolean displayedPolicy = Wait.checkIsDisplayed(cancelPolicy);
        if (displayedPolicy) {
            cancelPolicy.click();
        }
        return functionalMenu.addManually();
    }

    public SettlementPage openSid(String description, PseudoCategory pseudoCategory, Double newPrice) {
        return openSidAndFill(formFiller -> formFiller
                .withText(description)
                .withCategory(pseudoCategory.getGroupName())
                .withSubCategory(pseudoCategory.getCategoryName())
                .withNewPrice(newPrice))
                .closeSidWithOk();
    }

    public SettlementDialog openSidAndFill(Consumer<SettlementDialog.FormFiller> fillfunc) {
        return openSid().setDescription(Constants.TEXT_LINE).fill(fillfunc);
    }

    public SettlementDialog openSidAndFill(PseudoCategory pseudoCategory, Consumer<SettlementDialog.FormFiller> fillfunc) {
        return openSid()
                .setDescription(Constants.TEXT_LINE)
                .setCategory(pseudoCategory.getGroupName())
                .setSubCategory(pseudoCategory.getCategoryName())
                .fill(fillfunc);
    }

    public SettlementDialog openSidAndFillWithCustomDescription(Consumer<SettlementDialog.FormFiller> fillfunc) {
        return openSid().fill(fillfunc);
    }

    public SettlementPage addLines(ClaimItem claimItem, String... lineDescriptions) {
        PseudoCategory pseudoCategory = claimItem.getCategoryBabyItems();
        for (String lineDescription : lineDescriptions) {
            openSidAndFillWithCustomDescription(sid -> sid
                    .withText(lineDescription)
                    .withCategory(pseudoCategory.getGroupName())
                    .withSubCategory(pseudoCategory.getCategoryName())
                    .withNewPrice(PRICE_2400)
                    .withAge(AGE_YEAR, AGE_MONTH)
                    .withVoucher(claimItem.getExistingVoucher_10()))
                    .closeSidWithOk();
        }
        return this;
    }

    public SettlementPage addLinesForChosenCategories(String... categories) {
        for (String category : categories) {
            openSidAndFill(sid -> sid
                    .withCategory(category)
                    .withSubCategoryFromTheListByIndex(0)
                    .withNewPrice(PRICE_2400)
                    .withAge(AGE_YEAR, AGE_MONTH))
                    .closeSidWithOk();
        }
        return this;
    }


    public void cancelClaim() {
        settlementSummary.cancel();
    }

    public MyPage saveClaim(Claim claim) {
        settlementSummary.saveClaim();
        SolrApi.waitForClaimStatusChangedTo(claim, ClaimStatus.OPEN);
        return at(MyPage.class);
    }

    public MyPage completeClaimWithoutMail(Claim claim) {
        settlementSummary.completeClaimWithoutMail();
        SolrApi.waitForClaimStatusChangedTo(claim, ClaimStatus.CLOSED_EXTERNALLY);
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

    public SettlementGroupDialog openGroupCreationDialog() {
        $$(groupButton).get(0).click();
        return BaseDialog.at(SettlementGroupDialog.class);
    }

    public SettlementPage rejectLines() {
        $(rejectButton).click();
        waitForAjaxCompleted();
        return this;
    }

    public SettlementPage deleteGroup() {
        $(deleteGroupButton).click();
        waitForAjaxCompleted();
        $(By.xpath("//span[text() = 'Ja']")).click();
        return this;
    }

    public SettlementPage delete() {
        $(By.xpath("//span[contains(@style,'deleteIcon.png')]")).click();
        $(By.xpath("//span[text() = 'Ja']")).click();
        return this;
    }

    public SettlementPage selectLinesByIndex(int... lines) {
        try {
            selectLines(lines, claimLineDescription);
        } catch (IndexOutOfBoundsException e) {
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
        } catch (IndexOutOfBoundsException e) {
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
        return claims.stream().anyMatch(claim -> claim.getText().trim().equals(item));
    }

    public boolean isSettlementPagePresent() {
        try {
            return driver.findElement(By.cssSelector("#settlementGrid-body div[id*='tableview']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public TaskWizardPage1 sendToRnV() {
        toolBarMenu.sendToRepairAndValuation();
        return at(TaskWizardPage1.class);
    }


    public Double getFaceTooltipValue() {
        String tooltipText = (iconToolTip.getAttribute("title")).split("\\(")[0];
        String value = tooltipText.replaceAll("[^\\.,0123456789]", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public SettlementPage importExcelFile(String filePath) {
        return functionalMenu.
                openImportExcelDialog().
                uploadExcelNoErrors(filePath);
    }

    public LossLineImportDialog startImportExcelFile(String filePath){
        return functionalMenu.
                openImportExcelDialog().
                uploadExcelWithErrors(filePath);

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
        dragAndDrop(findClaimLine(claimLineDescription).descriptionElement, findClaimLine(groupName).descriptionElement);
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

        public Asserts assertSettlementPageIsInFlatView() {
            assertThat(invisibleOfElement(By.xpath("//div[contains(@class, 'x-tree-view')]"))).isTrue();
            return this;
        }

        public Asserts assertSettlementPageIsNotInFlatView() {
            assertThat(visible($(By.xpath("//div[contains(@class, 'x-tree-view')]")))).isTrue();
            return this;
        }

        public Asserts assertSettlementContainsLinesWithDescriptions(String... descriptions) {
            assertThat(Arrays.stream(descriptions).anyMatch(desc -> claimLineDescription.stream().anyMatch(claim -> claim.getText().equals(desc)))).isTrue();
            return this;
        }

        public Asserts assertItemNoteIsPresent(String itemCustomerNote) {
            assertThat(claimLineDescription.stream().anyMatch(claim -> claim.findElement(By.tagName("img")).getAttribute("data-qtip").trim().contains(itemCustomerNote))).isTrue();
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
            ElementsCollection elements = $(claimLine).findAll("[data-columnid='voucherImageColumn'] img[title]");
            if (elements.size() > 0) {
                this.tooltip = elements.get(0).getAttribute("title");
            }
            this.descriptionElement = $(claimLine).find("[data-columnid='descriptionColumn'] span");
            this.description = descriptionElement.getText();
            this.actualColor = descriptionElement.getAttribute("style");
            this.computedColor = descriptionElement.getCssValue("color");
            this.category = $(claimLine).find("[data-columnid='categoryGroupColumn']").getText();
            this.quantity = Integer.valueOf($(claimLine).find("[data-columnid='quantityColumn']").getText());

            this.age = $(claimLine).find("[data-columnid='settlementAgeColumn']").getText();

            ElementsCollection purchasePriceElements = $(claimLine).findAll("[data-columnid='totalPurchasePriceColumn']");
            if (!purchasePriceElements.isEmpty()) {
                String text = purchasePriceElements.get(0).getText();
                if (StringUtils.isNotBlank(text)) {
                    purchasePrice = OperationalUtils.getDoubleValue(text);
                }
            }

            String depreciationText = $(claimLine).find("[data-columnid='depreciationColumn']").getText().replace("%", "");
            depreciation = NumberUtils.isNumber(depreciationText) ? Integer.valueOf(depreciationText) : -1;
            replacementPrice = OperationalUtils.getDoubleValue($(claimLine).find("[data-columnid='replacementAmountColumn']").getText());
            try {
                ElementsCollection purchaseAmountElements = $(claimLine)
                        .findAll("[data-columnid='voucherPurchaseAmountValueColumn']");
                if (!purchaseAmountElements.isEmpty()) {
                    String text = purchaseAmountElements.get(0).getText();
                    if (StringUtils.isNotBlank(text)) {
                        this.voucherPurchaseAmount = OperationalUtils.getDoubleValue(text);
                    }
                }
            }catch (ElementNotFound t){
                logger.error(t);
            }

        }

        public Boolean ifRnvIconIsDisplayed(RnvIcon rnvIcon){
            return claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, '"+ rnvIcon +".png')]")).size() > 0;
        }

        public SettlementPage selectLine() {
            descriptionElement.click();
            return SettlementPage.this;
        }

        public SettlementPage toSettlementPage() {
            return SettlementPage.this;
        }

        public SettlementGroupDialog editGroup() {
            doubleClickGroupLine();
            return BaseDialog.at(SettlementGroupDialog.class);
        }

        public SettlementDialog editLine() {
            doubleClickClaimLine();
            $("#main-panel-innerCt").waitUntil(Condition.visible, 6000);
            return BaseDialog.at(SettlementDialog.class);
        }

        private void doubleClickClaimLine() {
            String dblClick = "var targLink    = arguments[0];\n" +
                    "var clickEvent  = document.createEvent ('MouseEvents');\n" +
                    "clickEvent.initEvent ('dblclick', true, true);\n" +
                    "targLink.dispatchEvent (clickEvent);";

            try {
                clickUsingJsIfSeleniumClickReturnError(descriptionElement);
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
            } catch (ScriptTimeoutException e) {
                logger.error(e.getMessage());
                ((JavascriptExecutor) driver).executeScript(dblClick, descriptionElement);
            }
        }

        private void doubleClickGroupLine() {
            try {
                doubleClick(descriptionElement);
                waitForAjaxCompleted();
            } catch (ScriptTimeoutException e) {
                logger.error(e.getMessage());
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
                assertThat(claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'wrench.png')]")).size() > 0)
                        .as("repair icon should be displayed").isTrue();
                return this;
            }

            public Asserts assertLineIsNotSentToRepair() {
                assertThat(claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'wrench.png')]")).size() > 0)
                        .as("repair icon should not be displayed").isFalse();
                return this;
            }

            public Asserts assertLineSentToValuation() {
                assertThat(claimLine.findElements(By.xpath(".//*[@data-columnid='repairValuationColumn']//img[contains(@src, 'view.png')]")).size() > 0)
                        .as("valuation icon should be displayed").isTrue();
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
                OperationalUtils.assertEqualsDouble(voucherPurchaseAmount, expectedAmount, "Expected amount is: " + expectedAmount);
                return this;
            }

            public Asserts assertVoucherPresent() {
                boolean voucherPresent = claimLine.findElements(By.xpath(".//*[@data-columnid='voucherImageColumn']//img[contains(@src, 'voucherIcon.png')]")).size() > 0;
                assertTrue(voucherPresent, "Voucher icon should be displayed");
                return this;
            }

            public Asserts assertProductDetailsIconIsDisplayed() {
                boolean productInfoPresent = claimLine.findElement(By.xpath(".//*[@data-columnid='typeColumn']//img[contains(@src, 'info.png')]")).isDisplayed();
                return this;
            }

            public Asserts assertAttachmentsIconIsDisplayed() {
                boolean attachmentsIconPresent = claimLine.findElement(By.xpath(".//*[@data-columnid='hasAttachmentColumn']//img[contains(@src, 'paperclip.png')]")).isDisplayed();
                assertTrue(attachmentsIconPresent, "Attachment icon should be displayed");
                return this;
            }

            public Asserts assertQuantityIs(int quantity) {
                assertThat(getQuantity()).isEqualTo(quantity);
                return this;
            }

            public Asserts assertAgeIs(String age) {
                assertThat(getAge()).containsIgnoringCase(age);
                return this;
            }

            public Asserts assertDepreciationIs(int depreciation) {
                assertThat(getDepreciation()).isEqualTo(depreciation);
                return this;
            }

            public Asserts assertClaimLineIsCrossedOut() {
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

            public Asserts assertCategory(String expectedCategoryGroup, String expectedCategory){
                String actualCategory = category;
                assertThat(actualCategory)
                        .as("expected category is "+ expectedCategoryGroup + " - " + expectedCategory+ " but was " + actualCategory)
                        .isEqualTo(expectedCategoryGroup+ " - " + expectedCategory);
                return this;
            }
        }
    }

    public enum RnvIcon{
        REPAIR_ICON("wrench"),
        VALUATION_ICON("view");

        public String iconFileName;


        RnvIcon(String iconFileName) {
            this.iconFileName = iconFileName;
        }

    }
}
