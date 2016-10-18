package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;

@EccPage
public class RoleAddEditPage extends Page {

    @FindBy(name = "roleName")
    private WebElement nameField;

    @FindBy(name = "ADMINISTER_ROLES_PAGES_ACCESS_ID_1")
    private WebElement adminRoleButton;

    @FindBy(name = "ACCESSORY_PAGE_ACCESS_ID_2")
    private WebElement accessoryPageButton;

    @FindBy(name = "USERS_PAGE_ACCESS_ID_3")
    private WebElement usersPageButton;

    @FindBy(name = "INSURANCE_COMPANY_PAGE_ACCESS_ID_4")
    private WebElement icPageButton;

    @FindBy(name = "FUNCTION_TEMPLATE_PAGE_ACCESS_ID_5")
    private WebElement ftPageButton;

    @FindBy(name = "TESTMAIL_PAGE_ACCESS_ID_6")
    private WebElement testMailPageButton;

    @FindBy(name = "TESTMOBILE_PAGE_ACCESS_ID_7")
    private WebElement testMobilePageButton;

    @FindBy(name = "NEWS_PAGE_ACCESS_ID_8")
    private WebElement newsPageButton;

    @FindBy(name = "SALES_TAXES_PAGE_ACCESS_ID_9")
    private WebElement salesTaxesPageButton;

    @FindBy(name = "FLUSH_ALL_PAGE_ACCESS_ID_10")
    private WebElement flushAllPageButton;

    @FindBy(name = "FLUSH_TEXT_CACHE_PAGE_ACCESS_ID_11")
    private WebElement flushTextCashPageButton;

    @FindBy(name = "IMPORT_TASKS_PAGE_ACCESS_ID_12")
    private WebElement importTasksPageButton;

    @FindBy(name = "DBVERSION_PAGE_ACCESS_ID_13")
    private WebElement dbVersionPageButton;

    @FindBy(name = "DATABASE_INFO_PAGE_ACCESS_ID_14")
    private WebElement dbInfoPageButton;

    @FindBy(name = "APPLICATION_INFO_PAGE_ACCESS_ID_15")
    private WebElement applicationInfoPageButton;

    @FindBy(name = "VERSION_INFO_PAGE_ACCESS_ID_16")
    private WebElement versionInfoPageButton;

    @FindBy(name = "SERVICE_PARTNER_REGISTER_PAGE_ACCESS_ID_17")
    private WebElement serPartnerPageButton;

    @FindBy(name = "BBB_INITIALIZE_ORDERS_PAGE_ACCESS_ID_18")
    private WebElement bbbInitOrdersPageButton;

    @FindBy(name = "BBB_SEND_ORDERS_PAGE_ACCESS_ID_19")
    private WebElement bbbSentOrdersPageButton;

    @FindBy(name = "E_VOUCHER_LIST_PAGE_ACCESS_ID_20")
    private WebElement eVouchersPageButton;

    @FindBy(name = "DEPARTMENTS_PAGE_ACCESS_ID_21")
    private WebElement departmentsPageButton;

    @FindBy(name = "ATTRIBUTE_META_DATA_PAGE_ACCESS_ID_22")
    private WebElement attributeDataPageButton;

    @FindBy(name = "ATTRIBUTE_META_DATA_STATUS_PAGE_ACCESS_ID_23")
    private WebElement attributeDataStatusPageButton;

    @FindBy(name = "ATTRIBUTE_TYPE_DEFAULT_SORT_ORDER_PAGE_ACCESS_ID_24")
    private WebElement attributeTypeDefaultPageButton;

    @FindBy(name = "GENERIC_ITEMS_PAGE_ACCESS_ID_25")
    private WebElement generaicItemsPageButton;

    @FindBy(name = "DEPLOYMENT_PAGE_ACCESS_ID_26")
    private WebElement deploymentPageButton;

    @FindBy(name = "REDUCTION_RULE_PAGE_ACCESS_ID_28")
    private WebElement redRulePageButton;

    @FindBy(name = "SETTLEMENT_INTEGRATION_DOCUMENTS_PAGE_ACCESS_ID_29")
    private WebElement setIntPageButton;

    @FindBy(name = "SUPPLIER_ORDER_NOTIFICATION_PAGE_ACCESS_ID_30")
    private WebElement supOrderNotPageButton;

    @FindBy(name = "COMPANY_SPECIFIC_CATEGORIES_PAGE_ACCESS_ID_31")
    private WebElement companySpecificCatPageButton;

    @FindBy(name = "COMPANY_SPECIFIC_CLAIMCATEGORIES_PAGE_ACCESS_ID_32")
    private WebElement companySpecificClaimPageButton;

    @FindBy(name = "PRICERUNNER_MAPPINGS_PAGE_ACCESS_ID_33")
    private WebElement prMappingsPageButton;

    @FindBy(name = "PRICERUNNER_EXTENDED_DATA_PAGE_ACCESS_ID_34")
    private WebElement prExtendedPageButton;

    @FindBy(name = "PRICERUNNER_EXTERNAL_PRICES_PAGE_ACCESS_ID_35")
    private WebElement prExternalPageButton;

    @FindBy(name = "CHECK_SCAN_ENGINE_PAGE_ACCESS_ID_36")
    private WebElement checkScanEnginePageButton;

    @FindBy(name = "CATEGORY_SPECIFIC_IC_CLAIMSHEET_PAGE_ACCESS_ID_37")
    private WebElement catSpecificICClaimPageButton;

    @FindBy(name = "TEXTSEARCH_CONFIGURATION_PAGE_ACCESS_ID_38")
    private WebElement textSearchConfPageButton;

    @FindBy(name = "PSEUDOCATEGORY_MODEL_PAGE_ACCESS_ID_39")
    private WebElement pseudoCatModelPageButton;

    @FindBy(name = "PSEUDOCATEGORY_GROUP_PAGE_ACCESS_ID_40")
    private WebElement pseudoCatGroupPageButton;

    @FindBy(name = "PSEUDOCATEGORIES_PAGE_ACCESS_ID_41")
    private WebElement pseudoCatPageButton;

    @FindBy(name = "ROOMS_PAGE_ACCESS_ID_42")
    private WebElement roomsPageButton;

    @FindBy(name = "EDIT_CUSTOMER_ORDERS_ACCESS_ID_43")
    private WebElement editCastOrderPageButton;

    @FindBy(name = "CAN_REOPEN_CLAIMS_ACCESS_ID_44")
    private WebElement canReopenClaimButton;

    @FindBy(name = "CAN_COMPLETE_CLAIMS_ACCESS_ID_45")
    private WebElement canCompleteClaimButton;

    @FindBy(name = "APPLICATION_STATUS_PAGE_ACCESS_ID_27")
    private WebElement appStatusPageButton;

    @FindBy(name = "TEXTSEARCH_POPULARITY_PAGE_ACCESS_ID_47")
    private WebElement tsPopularityButton;

    @FindBy(xpath = "//td[@class='selectionTD'][1]/input[@name='TEXTSEARCH_POPULARITY_PAGE_ACCESS_ID_47']")
    private WebElement enableTSPopularityButton;

    @FindBy(xpath = "//td[@class='selectionTD'][2]/input[@name='TEXTSEARCH_POPULARITY_PAGE_ACCESS_ID_47']")
    private WebElement disableTSPopularityButton;

    @FindBy(id = "btnOk")
    private WebElement saveButton;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/role_permission_edit.jsp";
    }

    public void addRoleName(String roleName) {
        nameField.sendKeys(roleName);
    }

    public RolesPage selectSaveOption() {
        clickAndWaitForDisplaying(saveButton, By.id("btnAdd"));
        return at(RolesPage.class);
    }

    public void enableAllRoles() {
        List<WebElement> elements = Arrays.asList(adminRoleButton, accessoryPageButton,
                usersPageButton, icPageButton, ftPageButton, testMailPageButton, testMobilePageButton, newsPageButton,
                salesTaxesPageButton, flushAllPageButton, flushTextCashPageButton, importTasksPageButton, dbVersionPageButton,
                dbInfoPageButton, applicationInfoPageButton, versionInfoPageButton, serPartnerPageButton, bbbInitOrdersPageButton,
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
                roomsPageButton,
                editCastOrderPageButton,
                canCompleteClaimButton,
                appStatusPageButton);
        for (WebElement item : elements) {
            scrollTo(item);
            item.click();
        }
    }

    public RolesPage createNewRoleAllRolesEnabled(String roleName) {
        addRoleName(roleName);
        enableAllRoles();
        return selectSaveOption();
    }

    public void enablePopularityPage() {
        scrollTo(enableTSPopularityButton);
        if (!enableTSPopularityButton.isSelected()) {
            enableTSPopularityButton.click();
        }
        selectSaveOption();
    }

    public void disablePopularityPage() {
        scrollTo(disableTSPopularityButton);
        if (!disableTSPopularityButton.isSelected()) {
            disableTSPopularityButton.click();
        }
        selectSaveOption();
    }
}
