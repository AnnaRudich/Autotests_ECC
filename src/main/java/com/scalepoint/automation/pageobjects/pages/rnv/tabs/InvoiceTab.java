package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.pageobjects.pages.rnv.dialogs.InvoiceDialog;
import org.openqa.selenium.By;

import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class InvoiceTab extends BaseClaimPage {

    @Override
    protected InvoiceTab ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        waitForJavascriptRecalculation();
        $("#grid-invoice-body").waitUntil(visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public InvoiceDialog openInvoiceDialogForLineWithIndex(int index){
        $$(By.xpath("//img[contains(@data-qtip, 'Vis faktura')]")).get(index).click();
        return BaseDialog.at(InvoiceDialog.class);
    }

    public InvoiceTab doAssert(Consumer<InvoiceTab.Asserts> assertFunc) {
        assertFunc.accept(new InvoiceTab.Asserts());
        return InvoiceTab.this;
    }

    public class Asserts {
        public InvoiceTab.Asserts assertThereIsNoInvoiceGrid() {
            assertThat($(By.xpath("//div[@id = 'grid-invoice']//table/tbody")).is(not(visible)))
                    .as("There should be no invoice lines").isTrue();
            return this;
        }
    }
}
