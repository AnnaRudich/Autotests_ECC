package com.scalepoint.automation.tests.sid.categoryBulkUpdate;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;

class ClaimLinesHelper {

    SettlementDialog startAddLine(PseudoCategory pseudoCategory, Boolean automaticDepreciationSetting, Double newPriceValue, int lineAgeYears) {

        return Page.at(SettlementPage.class)
                .openSid()
                .setDescription(RandomUtils.randomName("claimLine"))
                .setCategory(pseudoCategory)
                .setNewPrice(newPriceValue)
                .enableAge(String.valueOf(lineAgeYears))
                .automaticDepreciation(automaticDepreciationSetting);
    }

    void finishAddLine(){
        BaseDialog.at(SettlementDialog.class).closeSidWithOk();
    }

    SettlementPage addLine(PseudoCategory pseudoCategory, Boolean automaticDepreciationSetting, Double newPriceValue, int lineAgeYears){
        return startAddLine(pseudoCategory, automaticDepreciationSetting, newPriceValue, lineAgeYears)
                .closeSidWithOk();
    }
}
