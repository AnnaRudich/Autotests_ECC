package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

public class ExcelImportCategoriesAndValuationsSelectionTest extends BaseTest {

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider",
            description = "Import Excel where categories are not specified, but line description is meaningful " +
                    "so categories will be auto suggested while importing")
    @FeatureToggleSetting(type = FeatureIds.AUTOCAT_IN_EXCEL_IMPORT)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void autoCategorizationInExcelImport(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = "iphone";

        loginAndCreateClaim(user, claim)
                .importExcelFile(claimItem.getExcelPathWithoutCatAuto())
                .findClaimLine(claimLineDescription)
                .doAssert(claimLine -> claimLine.assertCategory(claimItem.getCategoryMobilePhones().getGroupName(), claimItem.getCategoryMobilePhones().getCategoryName()));
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider",
            description = "Import Excel where categories are not specified, and also line description has no sense" +
                    "so categories should be selected manually")
    @FeatureToggleSetting(type = FeatureIds.AUTOCAT_IN_EXCEL_IMPORT)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void selectCategoryManuallyInExcelImportDialog(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = "abrakadabra1";

        loginAndCreateClaim(user, claim)
                .startImportExcelFile(claimItem.getExcelPathWithoutCatNoAuto())
                .selectCategoryAndSubcategoryForTheErrorLine(claimItem.getCategoryBicycles().getGroupName(),
                        claimItem.getCategoryBicycles().getCategoryName(), claimLineDescription)
                .confirmImportAfterErrorsWereFixed()
                .confirmImportSummary()
                .findClaimLine(claimLineDescription)
                .doAssert(claimLine -> claimLine.assertCategory(claimItem.getCategoryBicycles().getGroupName(), claimItem.getCategoryBicycles().getCategoryName()));
    }
}
