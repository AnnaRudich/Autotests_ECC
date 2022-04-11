package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class RoleAddEditPage extends AdminBasePage {

    @FindBy(name = "roleName")
    private SelenideElement nameField;
    @FindBy(name = "ADMINISTER_ROLES_PAGES_ACCESS_ID_1")
    private SelenideElement adminRoleButton;
    @FindBy(name = "ACCESSORY_PAGE_ACCESS_ID_2")
    private SelenideElement accessoryPageButton;
    @FindBy(name = "USERS_PAGE_ACCESS_ID_3")
    private SelenideElement usersPageButton;
    @FindBy(name = "INSURANCE_COMPANY_PAGE_ACCESS_ID_4")
    private SelenideElement icPageButton;
    @FindBy(name = "FUNCTION_TEMPLATE_PAGE_ACCESS_ID_5")
    private SelenideElement ftPageButton;
    @FindBy(name = "TESTMAIL_PAGE_ACCESS_ID_6")
    private SelenideElement testMailPageButton;
    @FindBy(name = "TESTMOBILE_PAGE_ACCESS_ID_7")
    private SelenideElement testMobilePageButton;
    @FindBy(name = "SALES_TAXES_PAGE_ACCESS_ID_9")
    private SelenideElement salesTaxesPageButton;
    @FindBy(name = "FLUSH_ALL_PAGE_ACCESS_ID_10")
    private SelenideElement flushAllPageButton;
    @FindBy(name = "FLUSH_TEXT_CACHE_PAGE_ACCESS_ID_11")
    private SelenideElement flushTextCashPageButton;
    @FindBy(name = "IMPORT_TASKS_PAGE_ACCESS_ID_12")
    private SelenideElement importTasksPageButton;
    @FindBy(name = "DBVERSION_PAGE_ACCESS_ID_13")
    private SelenideElement dbVersionPageButton;
    @FindBy(name = "DATABASE_INFO_PAGE_ACCESS_ID_14")
    private SelenideElement dbInfoPageButton;
    @FindBy(name = "APPLICATION_INFO_PAGE_ACCESS_ID_15")
    private SelenideElement applicationInfoPageButton;
    @FindBy(name = "VERSION_INFO_PAGE_ACCESS_ID_16")
    private SelenideElement versionInfoPageButton;
    @FindBy(name = "BBB_INITIALIZE_ORDERS_PAGE_ACCESS_ID_18")
    private SelenideElement bbbInitOrdersPageButton;
    @FindBy(name = "BBB_SEND_ORDERS_PAGE_ACCESS_ID_19")
    private SelenideElement bbbSentOrdersPageButton;
    @FindBy(name = "E_VOUCHER_LIST_PAGE_ACCESS_ID_20")
    private SelenideElement eVouchersPageButton;
    @FindBy(name = "DEPARTMENTS_PAGE_ACCESS_ID_21")
    private SelenideElement departmentsPageButton;
    @FindBy(name = "ATTRIBUTE_META_DATA_PAGE_ACCESS_ID_22")
    private SelenideElement attributeDataPageButton;
    @FindBy(name = "ATTRIBUTE_META_DATA_STATUS_PAGE_ACCESS_ID_23")
    private SelenideElement attributeDataStatusPageButton;
    @FindBy(name = "ATTRIBUTE_TYPE_DEFAULT_SORT_ORDER_PAGE_ACCESS_ID_24")
    private SelenideElement attributeTypeDefaultPageButton;
    @FindBy(name = "GENERIC_ITEMS_PAGE_ACCESS_ID_25")
    private SelenideElement generaicItemsPageButton;
    @FindBy(name = "DEPLOYMENT_PAGE_ACCESS_ID_26")
    private SelenideElement deploymentPageButton;
    @FindBy(name = "REDUCTION_RULE_PAGE_ACCESS_ID_28")
    private SelenideElement redRulePageButton;
    @FindBy(name = "SETTLEMENT_INTEGRATION_DOCUMENTS_PAGE_ACCESS_ID_29")
    private SelenideElement setIntPageButton;
    @FindBy(name = "SUPPLIER_ORDER_NOTIFICATION_PAGE_ACCESS_ID_30")
    private SelenideElement supOrderNotPageButton;
    @FindBy(name = "COMPANY_SPECIFIC_CATEGORIES_PAGE_ACCESS_ID_31")
    private SelenideElement companySpecificCatPageButton;
    @FindBy(name = "COMPANY_SPECIFIC_CLAIMCATEGORIES_PAGE_ACCESS_ID_32")
    private SelenideElement companySpecificClaimPageButton;
    @FindBy(name = "PRICERUNNER_MAPPINGS_PAGE_ACCESS_ID_33")
    private SelenideElement prMappingsPageButton;
    @FindBy(name = "PRICERUNNER_EXTENDED_DATA_PAGE_ACCESS_ID_34")
    private SelenideElement prExtendedPageButton;
    @FindBy(name = "PRICERUNNER_EXTERNAL_PRICES_PAGE_ACCESS_ID_35")
    private SelenideElement prExternalPageButton;
    @FindBy(name = "CHECK_SCAN_ENGINE_PAGE_ACCESS_ID_36")
    private SelenideElement checkScanEnginePageButton;
    @FindBy(name = "CATEGORY_SPECIFIC_IC_CLAIMSHEET_PAGE_ACCESS_ID_37")
    private SelenideElement catSpecificICClaimPageButton;
    @FindBy(name = "TEXTSEARCH_CONFIGURATION_PAGE_ACCESS_ID_38")
    private SelenideElement textSearchConfPageButton;
    @FindBy(name = "PSEUDOCATEGORY_MODEL_PAGE_ACCESS_ID_39")
    private SelenideElement pseudoCatModelPageButton;
    @FindBy(name = "PSEUDOCATEGORY_GROUP_PAGE_ACCESS_ID_40")
    private SelenideElement pseudoCatGroupPageButton;
    @FindBy(name = "PSEUDOCATEGORIES_PAGE_ACCESS_ID_41")
    private SelenideElement pseudoCatPageButton;
    @FindBy(name = "EDIT_CUSTOMER_ORDERS_ACCESS_ID_43")
    private SelenideElement editCastOrderPageButton;
    @FindBy(name = "CAN_REOPEN_CLAIMS_ACCESS_ID_44")
    private SelenideElement canReopenClaimButton;
    @FindBy(name = "CAN_COMPLETE_CLAIMS_ACCESS_ID_45")
    private SelenideElement canCompleteClaimButton;
    @FindBy(name = "TEXTSEARCH_POPULARITY_PAGE_ACCESS_ID_47")
    private SelenideElement tsPopularityButton;
    @FindBy(xpath = "//td[@class='selectionTD'][1]/input[@name='TEXTSEARCH_POPULARITY_PAGE_ACCESS_ID_47']")
    private SelenideElement enableTSPopularityButton;
    @FindBy(xpath = "//td[@class='selectionTD'][2]/input[@name='TEXTSEARCH_POPULARITY_PAGE_ACCESS_ID_47']")
    private SelenideElement disableTSPopularityButton;
    @FindBy(id = "btnOk")
    private SelenideElement saveButton;

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        saveButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/role_permission_edit.jsp";
    }

    public void addRoleName(String roleName) {

        nameField.sendKeys(roleName);
    }

    public RolesPage selectSaveOption() {

        hoverAndClick(saveButton);
        return at(RolesPage.class);
    }

    public void enableAllRoles() {

        List<SelenideElement> elements = Arrays.asList(adminRoleButton, accessoryPageButton,
                usersPageButton, icPageButton, ftPageButton, testMailPageButton, testMobilePageButton,
                salesTaxesPageButton, flushAllPageButton, flushTextCashPageButton, importTasksPageButton, dbVersionPageButton,
                dbInfoPageButton, applicationInfoPageButton, versionInfoPageButton, bbbInitOrdersPageButton,
                bbbSentOrdersPageButton,
                eVouchersPageButton,
                departmentsPageButton,
                attributeDataPageButton,
                attributeDataStatusPageButton,
                attributeTypeDefaultPageButton,
                generaicItemsPageButton,
                deploymentPageButton,
                redRulePageButton,
                setIntPageButton,
                supOrderNotPageButton,
                companySpecificCatPageButton,
                companySpecificClaimPageButton,
                prMappingsPageButton,
                prExtendedPageButton,
                prExternalPageButton,
                checkScanEnginePageButton,
                catSpecificICClaimPageButton,
                textSearchConfPageButton,
                pseudoCatModelPageButton,
                pseudoCatGroupPageButton,
                pseudoCatPageButton,
                editCastOrderPageButton,
                canCompleteClaimButton);

        for (WebElement item : elements) {

            hoverAndClick($(item).scrollTo());
        }
    }

    public RolesPage createNewRoleAllRolesEnabled(String roleName) {

        addRoleName(roleName);
        enableAllRoles();
        return selectSaveOption();
    }

}
