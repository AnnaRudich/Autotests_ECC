package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.scalepoint.automation.utils.Http.post;

public class FunctionalTemplatesApi extends AuthenticationApi {

    private static Lock lock = new ReentrantLock();

    private List<FtOperation> operationsToRollback;

    public FunctionalTemplatesApi(User user) {
        super(user);
    }

    public FunctionalTemplatesApi(Executor executor) {
        super(executor);
    }

    public FTSettings.ComparingResult findDifferences(Integer functionalTemplateId, FtOperation... operations) {
        try {
            Content content = post(Page.getUrl(EditFunctionTemplatePage.class, functionalTemplateId), executor).returnContent();
            Document doc = Jsoup.parse(content.asString());
            return FTSettings.differences(doc, Arrays.asList(operations));
        } catch (IOException e) {
            log.error("Can't check Functional Template values", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T extends Page> T updateTemplate(Integer functionalTemplateId, Class<T> returnPageClass, FtOperation... operations) {
        try {
            if (lock.tryLock(240, TimeUnit.SECONDS)) {
                try {
                    log.info("Lock acquired for FT update");

                    String currentUrl = Browser.driver().getCurrentUrl();

                    FTSettings.ComparingResult comparingResult = findDifferences(functionalTemplateId, operations);
                    log.info("Requested to update: {} Found diffs: {}", operations.length, comparingResult.getDifferedOperations().size());

                    operationsToRollback = comparingResult.getInitialStates();

                    if (comparingResult.hasSameStateAsRequested()) {
                        return detectPage(currentUrl, returnPageClass);
                    }

                    EditFunctionTemplatePage templatePage = Page.to(EditFunctionTemplatePage.class, functionalTemplateId);
                    Arrays.stream(operations).
                            sorted((o1, o2) -> o2.getSetting().hasDependency().compareTo(o1.getSetting().hasDependency())).
                            forEach(ftOperation -> ftOperation.updateSetting(templatePage));

                    List<FtOperation> notUpdateTemplates = templatePage.findDifferences(operations);
                    if (!notUpdateTemplates.isEmpty()) {
                        for (FtOperation notUpdatedSetting : notUpdateTemplates) {
                            log.error("Couldn't update settings: " + notUpdatedSetting.toString());
                        }
                        throw new IllegalStateException("Couldn't update setting! Probably some of them interconnected and undo changes!");
                    }

                    templatePage.saveTemplate();

                    return detectPage(currentUrl, returnPageClass);

                } finally {
                    log.info("Lock unlocked for FT update");
                    lock.unlock();
                }
            } else {
                throw new IllegalStateException("Can't get lock on FT update");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public List<FtOperation> getOperationsToRollback() {
        return operationsToRollback;
    }
}
