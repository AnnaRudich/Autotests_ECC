package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.pageobjects.dialogs.*;
import com.scalepoint.automation.pageobjects.modules.ClaimOperationsMenu;
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
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.GenericItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Constants.*;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

@ClaimSpecificPage
@EccPage
public class SettlementPage extends BaseClaimPage {

    @FindBy(xpath = "//td[contains(@class,'voucherImageColumn')]//img")
    private SelenideElement iconToolTip;
    @FindBy(xpath = "//a[contains(@id, 'button-')][1]")
    private SelenideElement ok;
    @FindBy(id = "edit-policy-cancel-button-btnInnerEl")
    private SelenideElement cancelPolicy;
    @FindBy(xpath = "//span[contains(@style, 'selectAllIcon.png')]")
    private SelenideElement selectAllClaims;
    @FindBy(css = ".x-grid-cell-descriptionColumn")
    private ElementsCollection claimLineDescription;
    @FindBy(css = ".x-grid-cell-claimLineIDColumn")
    private ElementsCollection claimLineID;

    private Table getFirstClaim(){

        return new Table($(By.cssSelector("#settlementGrid-body table:first-child")));
    }

    private Button getImport(){

        return new Button($(By.id("_OK_button")));
    }

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

    private ToolBarMenu toolBarMenu = new ToolBarMenu();

    private ClaimOperationsMenu claimOperationsMenu = new ClaimOperationsMenu();

    private MainMenu mainMenu = new MainMenu();

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/matching_engine/settlement.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        ok.should(Condition.visible);
    }

    public SettlementPage cancelPolicy(){

        hoverAndClick(cancelPolicy);
        return this;
    }

    public ClaimLine findClaimLine(String description) {

        By claimLineXpath = By.xpath(".//*[@id='settlementGrid-body']//table//span[contains(text(), '" + description + "')]/ancestor::table | .//*[@id='settlementTreeGrid-body']//table//span[contains(text(), '" + description + "')]/ancestor::table");
        Table table = new Table($(claimLineXpath));
        return new ClaimLine(table);
    }

    public ClaimLine parseFirstClaimLine() {

        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        By claimLineXpath = By.xpath(".//*[@id='settlementGrid-body']//table//tr[1]/ancestor::table[1] | .//*[@id='settlementTreeGrid-body']//table//tr[1]/ancestor::table[1]");
        Table table = new Table($(claimLineXpath));
        return new ClaimLine(table);
    }

    public SettlementDialog editFirstClaimLine() {

        Wait.waitForAjaxCompleted();
        Wait.waitForJavascriptRecalculation();
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

    public SettlementPage requestSelfServiceWithEnabledNewPassword(ClaimRequest claimRequest, String password) {

        return claimOperationsMenu.requestSelfService()
                .enableNewPassword()
                .fill(claimRequest, password)
                .sendWithoutGdpr();
    }


    public SettlementPage addGenericItemToClaim(GenericItem genericItem) {

        return claimOperationsMenu.addGenericItem().
                chooseItem(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
    }

    public LossImportDialog openImportSelfServiceDialog() {

        return claimOperationsMenu.openImportExcelDialog();
    }

    public ClaimOperationsMenu getClaimOperationsMenu() {

        return claimOperationsMenu;
    }

    public ToolBarMenu getToolBarMenu() {

        return toolBarMenu;
    }

    public SettlementSummary getSettlementSummary() {

        return settlementSummary.expand();
    }

    public TextSearchPage toTextSearchPage() {

        claimOperationsMenu.findInCatalogue();
        return Page.at(TextSearchPage.class);
    }

    public TextSearchPage toTextSearchPage(String text) {

        return toTextSearchPage().searchByProductName(text);
    }

    public SettlementDialog openSid() {

        return claimOperationsMenu.addManually();
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
                    .withVoucher(claimItem.getExistingVoucher1()))
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

        completeClaim();
        return at(CompleteClaimPage.class);
    }

    private void completeClaim(){

        waitForAjaxCompleted();
        waitForJavascriptRecalculation();
        settlementSummary.completeClaim();
        try {

            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {

            logger.info("No alert is present");
        }
    }

    public DeductibleWarningDialog toDeductibleWarning(){

        completeClaim();
        return BaseDialog.at(DeductibleWarningDialog.class);
    }

    public SettlementGroupDialog openGroupCreationDialog() {

        $$(groupButton).get(0)
                .should(Condition.visible)
                .should(Condition.enabled)
                .click();
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
        Wait.waitForJavascriptRecalculation();
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

            Arrays.stream(descriptions)
                    .forEach(desc -> new Actions(driver)
                            .keyDown(Keys.CONTROL)
                            .click(claimLineDescription.stream().filter(line -> line.getText().equals(desc)).findFirst().get())
                            .keyUp(Keys.CONTROL).build().perform());
        } catch (IndexOutOfBoundsException e) {

            logger.error(e.getMessage());
        }
        return this;
    }

    private void selectLines(int[] lines, ElementsCollection element) {

        Arrays.stream(lines).forEach(line -> new Actions(driver)
                .keyDown(Keys.CONTROL)
                .click(element.get(line))
                .keyUp(Keys.CONTROL).build().perform());
    }

    public boolean isItemPresent(String item) {

        ElementsCollection claims = claimLineDescription;
        return claims.stream().anyMatch(claim -> claim.getText().trim().equals(item));
    }

    public boolean isSettlementPagePresent() {

        try {

            return $(By.cssSelector("#settlementGrid-body div[id*='tableview']")).isDisplayed();
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

        return claimOperationsMenu.
                openImportExcelDialog().
                uploadExcelNoErrors(filePath);
    }

    public LossLineImportDialog startImportExcelFile(String filePath){

        return claimOperationsMenu.
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

            assertFalse(isItemPresent(claimLineDescription),
                    errorMessage("The claim item [%s] must be absent: ", claimLineDescription));
            return this;
        }


        public Asserts assertGenericItemIsNotPresent(GenericItem genericItem) {

            boolean genericItemIsPresent = claimOperationsMenu.addGenericItem().isGenericItemPresent(genericItem.getName(), genericItem.getGroup(), genericItem.getCategory());
            assertFalse(genericItemIsPresent);
            return this;
        }

        public Asserts assertFirstLineIsRejected() {

            assertThat($(By.xpath("(.//*[@id='settlementGrid-body']//table//tr[1])")).getAttribute("class")).contains("rejected");
            return this;
        }

        public Asserts assertSettlementPageIsInFlatView() {

            assertThat($(By.xpath("//div[contains(@class, 'x-tree-view')]")).shouldNot(visible).has(visible)).isFalse();
            return this;
        }

        public Asserts assertSettlementPageIsNotInFlatView() {

            assertThat($(By.xpath("//div[contains(@class, 'x-tree-view')]")).should(visible).has(visible)).isTrue();
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

        public Asserts assertEditPolicyTypeDialogIsNotPresent(){

            assertFalse(BaseDialog.isOn(EditPolicyTypeDialog.class));
            return this;
        }
    }

    public class ClaimLine {

        @Getter
        private Table claimLine;
        private SelenideElement  claimLineNotesIconElement;
        @Getter
        private String tooltip = "";
        @Getter
        private String description;
        @Getter
        private String category;
        @Getter
        private int quantity;
        @Getter
        private String age;
        @Getter
        private double purchasePrice;
        @Getter
        private int depreciation;
        private double replacementPrice;
        @Getter
        private double voucherPurchaseAmount;
        @Getter
        private String actualColor;
        @Getter
        private String computedColor;
        private WebElement descriptionElement;

        public ClaimLine(Table claimLine) {

            this.claimLine = claimLine;
            claimLineNotesIconElement = $(claimLine.getWrappedElement()).find("img[src*=notes_claim_line]");
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

        public SettlementPage selectLine() {

            descriptionElement.click();
            return SettlementPage.this;
        }
        public SettlementPage selectLineByControlKey() {

            select(descriptionElement);
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
            $("#main-panel-innerCt").should(Condition.visible);
            return BaseDialog.at(SettlementDialog.class);
        }

        private void doubleClickClaimLine() {

            hoverAndClick($(descriptionElement));
            $(descriptionElement).doubleClick();
            waitForAjaxCompletedAndJsRecalculation();
        }

        SelenideElement claimLineNotesIconElementShouldBe(Condition condition){

            return claimLineNotesIconElement
                    .should(condition);
        }

        public ClaimLineNotesDialog toClaimLineNote(){

            hoverAndClick(claimLineNotesIconElement);
            return BaseDialog.at(ClaimLineNotesDialog.class);
        }

        private void doubleClickGroupLine() {

            try {

                $(descriptionElement).doubleClick();
                waitForAjaxCompletedAndJsRecalculation();
            } catch (ScriptTimeoutException e) {

                logger.error(e.getMessage());
            }
        }

        public double getReplacementPrice() {

            return replacementPrice;
        }

        boolean isTooltipPresent(String expectedText) {

            return expectedText.equals(getTooltip());
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
            public Asserts assertClaimLineNotesIconPresent() {

                assertThat(claimLineNotesIconElementShouldBe(Condition.visible)
                        .isDisplayed())
                        .as("Claim line notes icon is missing for lineName: " + description).isTrue();
                return this;
            }

            public Asserts assertClaimLineNotesIconMissing() {

                assertThat(claimLineNotesIconElementShouldBe(not(Condition.visible))
                        .isDisplayed())
                        .as("Claim line notes icon exists for lineName: " + description).isFalse();
                return this;
            }
        }
    }
}
