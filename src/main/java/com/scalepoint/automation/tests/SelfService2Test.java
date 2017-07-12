package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import org.testng.annotations.Test;

/**
 * Created by aru on 2017-07-10.
 */
@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
@RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
//add all possible fields? or basic?
public class SelfService2Test extends BaseTest{

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add lines. Required fields")
    public void charlie504_addLineRequiredFields(){

    }
}