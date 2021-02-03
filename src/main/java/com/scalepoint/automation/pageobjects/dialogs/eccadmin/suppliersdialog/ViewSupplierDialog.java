package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog;

import org.openqa.selenium.By;

public class ViewSupplierDialog extends SupplierDialog {

    public ViewSupplierDialog(){
        tabsPath = By.cssSelector(".viewSupplierWindow .x-header a");
    }
}
