package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class ExcelImport extends BaseTest {

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
        String claimLineDescription = "Line1";

      SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .importExcelFile(claimItem.getExcelPathWithoutCatAuto());

        Assert.assertEquals(settlementPage.getLinesByDescription(claimLineDescription),
                claimItem.getCategoryBicycles().getGroupName() + " - " + claimItem.getCategoryBicycles().getCategoryName());
    }

    /*
    manual category selection in Excel import
     *FToggle is enabled,
     * import line without category but with 'abracadabra' desc
     * select category in Import Dialog
     */

    @RunOn(DriverType.CHROME)
    @Jira("")
    @Test(dataProvider = "testDataProvider",
            description = "")
    @FeatureToggleSetting(type = FeatureIds.AUTOCAT_IN_EXCEL_IMPORT)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void selectCategoryManuallyInExcelImportDialog(User user, Claim claim, ClaimItem claimItem) {
        List<String> claimLineDescriptions = Arrays.asList("abrakadabra1", "abrakadabra2");

        loginAndCreateClaim(user, claim)
                .startImportExcelFile(claimItem.getExcelPathWithoutCatNoAuto())
                .selectCategoryAndSubcategoryForTheErrorLine(claimItem.getCategoryBicycles().getGroupName(), claimItem.getCategoryBicycles().getCategoryName(), claimLineDescriptions)
                .confirmImportAfterErrorsWereFixed()
                .confirmImportSummary()
                .doAssert(asserts -> asserts.assertItemIsPresent(claimLineDescriptions.get(0)));

        List<SettlementPage.ClaimLine> claimLines = new SettlementPage().getLinesByDescription("abrakadabra1", "abrakadabra2");

        Assert.assertEquals(claimLines.get(0).getCategory(),
                claimItem.getCategoryBicycles().getGroupName() + " - " + claimItem.getCategoryBicycles().getCategoryName());

    }
}
