package com.scalepoint.automation.pageobjects.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ClaimLineAgeDataPicker extends SettlementDialog {

    ClaimLineAgeDataPicker isDataPickerOpened(){
        $(".x-datepicker").shouldBe(Condition.visible);
        return this;
    }

    ClaimLineAgeDataPicker openMonthYearSelector(){
        $(".x-datepicker-month a span span").find(By.cssSelector("data-ref=btnInnerEl")).click();
        $(".x-monthpicker").shouldBe(Condition.visible);
        return this;
    }

    ClaimLineAgeDataPicker selectYear(String yearToSelect){
        ElementsCollection listOfYears = $$(".x-monthpicker-item .x-monthpicker-year a");
        for(SelenideElement year: listOfYears){
            if(year.getText().equals(yearToSelect)){
                year.click();
                //confirm by OK, CRAZY selector
            }
        }
        return this;
    }

    SettlementDialog closePicker(){
        $(".x-datepicker-footer .x-btn").click();
        return at(SettlementDialog.class);
    }

}
