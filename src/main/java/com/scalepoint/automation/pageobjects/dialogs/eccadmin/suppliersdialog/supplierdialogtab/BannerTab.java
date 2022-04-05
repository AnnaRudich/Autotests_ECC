package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.utils.JavascriptHelper;
import org.openqa.selenium.By;

import java.io.File;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertTrue;

public class BannerTab extends SupplierDialog {

    public BannerTab uploadBanner(String bannerPath) {

        SelenideElement elem = $(By.xpath("//input[contains(@id, 'supplierBannerFileId') and contains(@type, 'file')]"));
        elem.uploadFile(new File(bannerPath));
        return this;
    }

    public BannerTab doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new BannerTab.Asserts());
        return BannerTab.this;
    }

    public class Asserts {

        public BannerTab.Asserts assertBannerIsPresent() {

            assertTrue(JavascriptHelper.isImagePresent(waitElementVisible($(By.className("bannerUploadImg")))));
            return this;
        }
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
    }
}
