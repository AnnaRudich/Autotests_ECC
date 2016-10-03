package com.scalepoint.automation.tests;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.FunctionalTemplatesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
public class FuncTemplatesTests extends BaseTest {

    @Test(description = "CHARLIE-555 It's possible to create new ME_FT. New ME_FT is displayed in ME_FT list")
    public void charlie555_createNewFt() {
        String ftName = "CHARLIE-555-" + System.currentTimeMillis();
        boolean containsTemplate = createNewTemplate(ftName).
                containsTemplate(ftName);

        Assert.assertTrue(containsTemplate, "Template is not found");
    }

    @Test(description = "CHARLIE-555 It's possible to delete new ME_FT. New ME_FT is not displayed in ME_FT list")
    public void charlie555_deleteNewFt() {
        String ftName = "CHARLIE-555-" + System.currentTimeMillis();
        boolean deleted = createNewTemplate(ftName).delete(ftName);
        Assert.assertTrue(deleted, "Template can't be deleted");
    }

    @Test(description = "CHARLIE-555 It's possible to edit new ME_FT. Edited ME_FT is displayed in ME_FT list")
    public void charlie555_editFt() {
        String ftName = "CHARLIE-555-" + System.currentTimeMillis();
        String ftNameUpdated = "CHARLIE-555-U-" + System.currentTimeMillis();
        boolean containsTemplate = createNewTemplate(ftName).
                editTemplate(ftName).
                setName(ftNameUpdated).
                saveTemplate().
                containsTemplate(ftNameUpdated);
        Assert.assertTrue(containsTemplate, "Template is not found");
    }

    private FunctionalTemplatesPage createNewTemplate(String ftName) {
        return login(getSystemUser()).
                to(FunctionalTemplatesPage.class).
                copyTemplate("Default").
                setName(ftName).
                saveTemplate();
    }

}
