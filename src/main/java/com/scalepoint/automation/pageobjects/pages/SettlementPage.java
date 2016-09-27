package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.*;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class SettlementPage extends BaseClaimPage {

    @FindBy(css = "#settlementGrid-body table")
    private Table claim;

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

    /**
     * element from the awaited page
     */
    @FindBy(xpath = "//a[contains(@id, 'button-')][1]")
    private WebElement ok;

    /**
     * element from the awaited page
     */
    @FindBy(id = "_OK_button")
    private Button _import;

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

    public SettlementPage requestSelfService(Claim claim, String password) {
        claimOperationsMenu.requestSelfService().
                fill(claim, password).
                send();
        return this;
    }

    public SettlementPage addGenericItem(String genericItemName, String categoryGroup, String category) {
        claimOperationsMenu.addGenericItem().
                chooseItem(genericItemName, categoryGroup, category);
        return this;
    }

    public NotesPage addCustomerNote(String customerNote) {
        return toNotesPage().
                editCustomerNote().
                addCustomerNote(customerNote);
    }

    public ToolBarMenu getToolBarMenu() {
        return toolBarMenu;
    }

    public BottomMenu getBottomMenu() {
        return bottomMenu;
    }

    public TextSearchPage findInCatalogue() {
        functionalMenu.findInCatalogue();
        return Page.at(TextSearchPage.class);
    }

    public TextSearchPage findInCatalogue(String text) {
        return findInCatalogue().searchByProductName(text);
    }

    public SettlementDialog addManually() {
        functionalMenu.addManually();
        Wait.waitForAjaxComplete();
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

    public String fetchPriceByPoint(String point) {
        List<List<String>> priceConclusions = settlementConclusion.getClaimsResult().getRowsAsString();
        String line = priceConclusions.stream().filter(points -> point.contains(point)).findFirst().toString();
        return line.split(" ")[line.split(" ").length - 1].replaceAll("[\\]]]", "");
    }

    public void cancelClaim() {
        bottomMenu.cancel();
    }

    public MyPage saveClaim() {
        bottomMenu.saveClaim();
        return at(MyPage.class);
    }

    public CompleteClaimPage completeClaim() {
        bottomMenu.completeClaim();
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {

        }
        return at(CompleteClaimPage.class);
    }

    public boolean isItemPresent(String _item) {
        List<WebElement> claims = claimDescription;
        return claims.stream().anyMatch(claim -> claim.getText().equals(_item));
    }

    public SettlementPage selectClaimItemByName(String _item) {
        List<WebElement> claim = claimLineID;
        claim.stream().filter(claims -> claims.getText().contains(_item)).findFirst().get().click();
        return this;
    }

    public boolean isSettlementPagePresent() {
        try {
            return driver.findElement(By.cssSelector("#settlementGrid-body div[id*='tableview']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public SettlementPage selectClaimItemByDescription(String _item) {
        List<WebElement> rows = claim.getColumnByIndex(5);
            rows.stream().filter(claim -> claim.getText().contains(_item)).findFirst().get().click();
        return this;
    }

    public Double getFaceTooltipValue() {
        String tooltipText = (iconToolTip.getAttribute("title")).split("\\(")[0];
        String value = tooltipText.replaceAll("[^\\.,0123456789]", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public boolean isDepreciationPercentPresent(String _claim, String _depreciationPercent) {
        List<List<WebElement>> rowsNames = claim.getRows();
        for (List<WebElement> list : rowsNames) {
            String claim = list.get(4).getText();
            if (claim.equals(_claim)) {
                String actualDepreciationPercent = list.get(9).getText();
                return actualDepreciationPercent.equals(_depreciationPercent);
            }
        }
        return false;
    }

    public SettlementDialog openEditSettlementDialogByClaimDescr(String _claimDescr) {
        selectClaimItemByDescription(_claimDescr);
        List<List<WebElement>> rowsNames = claim.getRows();
        for (List<WebElement> list : rowsNames) {
            String claim = list.get(4).getText();
            if (claim.equals(_claimDescr)) {
                list.get(3).click();
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
            }
        }
        return BaseDialog.at(SettlementDialog.class);
    }

    public boolean getClaimColorByDescription(String _item, String _color) {
        selectClaimItemByDescription(_item);
        List<List<WebElement>> rowsNames = claim.getRows();
        for (List<WebElement> list : rowsNames) {
            String claim = list.get(4).getText();
            if (claim.equals(_item)) {
                String actualColor = list.get(4).findElement(By.cssSelector("span")).getAttribute("style");
                return actualColor.equals(_color);
            }
        }
        return false;
    }

    public boolean getComputedClaimColorByDescription(String _item, String _color) {
        selectClaimItemByDescription(_item);
        List<List<WebElement>> rowsNames = claim.getRows();
        for (List<WebElement> list : rowsNames) {
            String claim = list.get(4).getText();
            if (claim.equals(_item)) {
                String color = list.get(4).findElement(By.cssSelector("span")).getCssValue("color");
                return color.equals(_color);
            }
        }
        return false;
    }

    public SettlementPage importExcelFile(String filePath) {
        return functionalMenu.
                openImportExcelDialog().
                uploadExcel(filePath);
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }
}
