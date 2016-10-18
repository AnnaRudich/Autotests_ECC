package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.modules.AdminMenu;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class AdminPage extends Page {

    private AdminMenu adminMenu = new AdminMenu();

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin";
    }

    @Override
    public AdminPage ensureWeAreOnPage() {
        waitForVisible(adminMenu.getMatchingEngine());
        return this;
    }

    public SettlementPage toSettlement() {
        return adminMenu.toSettlement();
    }

    public FunctionalTemplatesPage toFunctionalTemplatesPage() {
        return adminMenu.toFunctionalTemplatesPage();
    }

    public GenericItemsAdminPage toGenericItemsPage() {
        return adminMenu.toGenericItemsPage();
    }

    public UsersPage toUsersPage() {
        return adminMenu.toUsers();
    }

    public MyPage toMatchingEngine() {
        return adminMenu.toMatchingEngine();
    }
}
