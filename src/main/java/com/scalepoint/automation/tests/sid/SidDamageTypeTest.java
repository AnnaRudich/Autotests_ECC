package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.PseudoCategory;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.SHOW_DAMAGE_TYPE_CONTROLS_IN_SID;

@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
@RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
public class SidDamageTypeTest extends BaseTest {

    private static final String SONY_HDR_CX450 = "Sony HDR-CX450";

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4453")
    @Test(dataProvider = "testDataProvider",
            description = "Verify is possible to add damage type in manual registration")
    public void damageTypeManualItemTest(User user, Claim claim, ClaimItem claimItem) {
        PseudoCategory pseudoCategory = claimItem.getCategoryVideoCamera();
        String damageType = pseudoCategory.getDamageTypes().get(0);

        loginAndCreateClaim(user, claim)
                .openSid()
                .fill(formFiller -> {
                    formFiller.withText(claimItem.getTextFieldSP())
                            .withCategory(claimItem.getCategoryVideoCamera())
                            .withCustomerDemandPrice(claimItem.getCustomerDemand())
                            .withNewPrice(claimItem.getNewPriceSP());
                })
                .enableDamage()
                .selectDamageType(damageType)
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP()).editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoriesTextIs(claimItem.getCategoryVideoCamera());
                    claimLine.assertDamageTypeVisible();
                    claimLine.assertDamageTypeEqualTo(damageType);
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4453")
    @Test(dataProvider = "testDataProvider",
            description = "Verify is possible to add damage type in registration from catalog")
    public void damageTypeCatalogRegistrationTest(User user, Claim claim, ClaimItem claimItem) {
        PseudoCategory pseudoCategory = claimItem.getCategoryVideoCamera();
        String damageType = pseudoCategory.getDamageTypes().get(0);

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(SONY_HDR_CX450)
                .chooseCategory(claimItem.getCategoryVideoCamera())
                .openSidForFirstProduct()
                .enableDamage()
                .selectDamageType(damageType)
                .closeSidWithOk()
                .findClaimLine(SONY_HDR_CX450).editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionContains(SONY_HDR_CX450);
                    claimLine.assertCategoriesTextIs(claimItem.getCategoryVideoCamera());
                    claimLine.assertDamageTypeVisible();
                    claimLine.assertDamageTypeEqualTo(damageType);
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4453")
    @Test(dataProvider = "testDataProvider",
            description = "Verify damage type is grayed out if no reason(s) under the category under admin")
    public void damageTypeIfNoReasonUnderCategoryTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .fill(formFiller -> {
                    formFiller.withText(claimItem.getTextFieldSP())
                            .withCategory(claimItem.getCategoryShoes())
                            .withCustomerDemandPrice(claimItem.getCustomerDemand())
                            .withNewPrice(claimItem.getNewPriceSP());
                })
                .enableDamage()
                .doAssert(claimLine -> {
                    claimLine.assertDamageTypeDisabled();
                })
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP()).editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDescriptionIs(claimItem.getTextFieldSP());
                    claimLine.assertCategoriesTextIs(claimItem.getCategoryShoes());
                    claimLine.assertCashValueIs(claimItem.getCustomerDemand());
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4453")
    @Test(dataProvider = "testDataProvider",
            description = "Verify damage type is required if the item is damaged and the category has reasons")
    public void damageTypeRequiredTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(SONY_HDR_CX450)
                .chooseCategory(claimItem.getCategoryVideoCamera())
                .openSidForFirstProduct()
                .enableDamage()
                .clickOK()
                .viewDamageTypeValidationErrorMessage()

                .doAssert(claimLine -> {
                    claimLine.assertHasDamageTypeValidationError(claimItem.getDamageTypeValidationError());
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4453")
    @Test(dataProvider = "testDataProvider",
            description = "Verify combo with damage types should contain only damage types relevant for current category")
    public void damageTypeRelevantForCategoryTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .fill(formFiller -> {
                    formFiller.withText(claimItem.getTextFieldSP())
                            .withCategory(claimItem.getCategoryVideoCamera())
                            .withCustomerDemandPrice(claimItem.getCustomerDemand())
                            .withNewPrice(claimItem.getNewPriceSP());
                })
                .enableDamage()
                .clickDamageTypePicker()
                .doAssert(claimLine -> {
                    claimLine.assertDamageTypesRelevantForCategory(claimItem.getCategoryVideoCamera().getDamageTypes());
                });
    }
}
