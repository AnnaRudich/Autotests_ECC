package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.admin.FunctionalTemplatesPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-555")
public class FuncTemplatesTests extends BaseTest {

    private String ftText = "FT template ÆæØøÅåß";

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES}, description = "CHARLIE-555 It's possible to create new ME_FT. New ME_FT is displayed in ME_FT list")
    public void charlie555_createNewFt() {
        String ftName = ftText.concat(String.valueOf(System.currentTimeMillis()));
        createNewTemplate(ftName).assertTemplateExists(ftName);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES}, description = "CHARLIE-555 It's possible to delete new ME_FT. New ME_FT is not displayed in ME_FT list")
    public void charlie555_deleteNewFt() {
        String ftName = ftText.concat(String.valueOf(System.currentTimeMillis()));
        boolean deleted = createNewTemplate(ftName).delete(ftName);
        Assert.assertTrue(deleted, "Template can't be deleted");
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES}, description = "CHARLIE-555 It's possible to edit new ME_FT. Edited ME_FT is displayed in ME_FT list")
    public void charlie555_editFt() {
        String ftName = ftText.concat(String.valueOf(System.currentTimeMillis()));
        String ftNameUpdated = "CHARLIE-555-U-" + System.currentTimeMillis();
        createNewTemplate(ftName).editTemplate(ftName)
                .setName(ftNameUpdated)
                .saveTemplate()
                .assertTemplateExists(ftNameUpdated);
    }

    private FunctionalTemplatesPage createNewTemplate(String ftName) {
        return login(getSystemUser()).
                to(FunctionalTemplatesPage.class).
                copyTemplate("Default").
                setName(ftName).
                saveTemplate();
    }

}
