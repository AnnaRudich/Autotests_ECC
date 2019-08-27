package com.scalepoint.automation.tests;

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
import org.testng.annotations.Test;

public class ExcelImport extends BaseTest {
/*
 *FToggle is enabled,
 * import line without category but with normal desc
 * assert Category is suggested
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
    public void ecc2631_quickMatchFromExcel(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = claimItem.getSetDialogTextMatch();

       loginAndCreateClaim(user, claim)
                .importExcelFile(claimItem.getExcelPath1())
               .importExcelFile(claimItem.getExcelPath1())
                .doAssert(sid -> sid.assertItemIsPresent(claimItem.getXlsDescr1()));
    }

    /*
     *FToggle is enabled,
     * import line without category but with 'abracadabra' desc
     * select category in Import Dialog
     */
}
