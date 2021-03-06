package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureId;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

public class ExcelImportCategoriesAndValuationsSelectionTest extends BaseUITest {

    @Test(groups = {TestGroups.EXCEL_IMPORT_CATEGORIES_AND_VALUATIONS_SELECTION}, dataProvider = "testDataProvider",
            description = "Import Excel where categories are not specified, but line description is meaningful " +
                    "so categories will be auto suggested while importing")
    @FeatureToggleSetting(type = FeatureId.AUTOCAT_IN_EXCEL_IMPORT)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void autoCategorizationInExcelImport(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = "iphone";

        loginFlow.loginAndCreateClaim(user, claim)
                .importExcelFile(claimItem.getExcelPathWithoutCatAuto())
                .findClaimLine(claimLineDescription)
                .doAssert(claimLine -> claimLine.assertCategory(claimItem.getCategoryMobilePhones().getGroupName(), claimItem.getCategoryMobilePhones().getCategoryName()));
    }

    @Test(groups = {TestGroups.EXCEL_IMPORT_CATEGORIES_AND_VALUATIONS_SELECTION}, dataProvider = "testDataProvider",
            description = "Import Excel where categories are not specified, and also line description has no sense" +
                    "so categories should be selected manually")
    @FeatureToggleSetting(type = FeatureId.AUTOCAT_IN_EXCEL_IMPORT)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void selectCategoryManuallyInExcelImportDialog(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = "abrakadabra1";

        loginFlow.loginAndCreateClaim(user, claim)
                .startImportExcelFile(claimItem.getExcelPathWithoutCatNoAuto())
                .selectCategoryAndSubcategoryForTheErrorLine(claimItem.getCategoryBicycles().getGroupName(),
                        claimItem.getCategoryBicycles().getCategoryName(), claimLineDescription)
                .confirmImportAfterErrorsWereFixed()
                .confirmImportSummary()
                .findClaimLine(claimLineDescription)
                .doAssert(claimLine -> claimLine.assertCategory(claimItem.getCategoryBicycles().getGroupName(), claimItem.getCategoryBicycles().getCategoryName()));
    }
}
