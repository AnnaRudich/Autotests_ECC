package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.pageobjects.pages.EditFunctionTemplatePage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.externalapi.ftemplates.FTemplatesManager;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

import static com.scalepoint.automation.utils.Http.post;

public class FunctionalTemplatesApi extends ServerApi {

    public FunctionalTemplatesApi(User user) {
        super(user);
    }

    public FunctionalTemplatesApi(Executor executor) {
        super(executor);
    }

    private boolean shouldUpdateFuncTemplates(Integer functionalTemplateId, FtOperation... operations) {
        try {
            Content content = post(Page.getUrl(EditFunctionTemplatePage.class) + functionalTemplateId, executor).returnContent();
            Document doc = Jsoup.parse(content.asString());

            FTemplatesManager manager = new FTemplatesManager(doc);
            return !manager.hasSameValues(Arrays.asList(operations));

        } catch (IOException e) {
            log.error("Can't check Functional Template values", e);
            return true;
        }
    }

    public <T extends Page> T updateTemplate(Integer functionalTemplateId, Class<T> returnPageClass, FtOperation... operations) {
        String currentUrl = Browser.driver().getCurrentUrl();

        if (!shouldUpdateFuncTemplates(functionalTemplateId, operations)) {
            return detectPage(currentUrl, returnPageClass);
        }
        EditFunctionTemplatePage templatePage = Page.to(EditFunctionTemplatePage.class, functionalTemplateId.toString()+"&showHidden=true");
        Arrays.stream(operations).forEach(ftOperation -> ftOperation.updateSetting(templatePage));
        templatePage.saveTemplate();
        return detectPage(currentUrl, returnPageClass);
    }

    private <T extends Page> T detectPage(String initialUrl, Class<T> returnPageClass) {
        if (returnPageClass == null) {
            return null;
        }
        String url = Page.getUrl(returnPageClass);
        log.info("Initial page: {}", initialUrl);
        log.info("Requested page: {}", url);
        if (initialUrl.contains(url)) {
            Browser.open(initialUrl);
            return Page.at(returnPageClass);
        }
        return Page.to(returnPageClass);
    }


}
