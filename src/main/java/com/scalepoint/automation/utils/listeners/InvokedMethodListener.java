package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class InvokedMethodListener implements IInvokedMethodListener {

    private static final String ROLLBACK_CONTEXT = "rollback_context";

    private static Logger logger = LoggerFactory.getLogger(InvokedMethodListener.class);

    @Override
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult iTestResult) {
        try {
            if (invokedMethod.isTestMethod()) {
                Optional<User> optionalUser = findMethodParameter(iTestResult, User.class);
                logger.info("-------- InvokedMethodListener before. Thread: {} ----------", Thread.currentThread().getId());
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    updateFunctionalTemplate(invokedMethod, iTestResult, user);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iInvokedMethod.isTestMethod()) {
            takeScreenshot(iInvokedMethod.getTestMethod().getConstructorOrMethod().getMethod(), iTestResult);

            logger.info("-------- InvokedMethodListener after. Thread: {} ----------", Thread.currentThread().getId());
            printErrorStackTraceIfAny(iTestResult);

            RollbackContext rollbackContext = (RollbackContext) iTestResult.getAttribute(ROLLBACK_CONTEXT);
            if (rollbackContext == null || rollbackContext.operations.isEmpty()) {
                logger.info("No ft settings found to rollback");
                return;
            }
            FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(UsersManager.getSystemUser());
            List<FtOperation> operations = rollbackContext.operations;
            functionalTemplatesApi.updateTemplate(rollbackContext.user.getFtId(), LoginPage.class, operations.toArray(new FtOperation[0]));
        }
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ResultOfMethodCallIgnored"})
    private void takeScreenshot(Method method, ITestResult iTestResult) {
        if (iTestResult.getThrowable() != null) {
            try {
                File screenshotAs = ((TakesScreenshot) Browser.driver()).getScreenshotAs(OutputType.FILE);
                File destFolder = new File("c:\\tmp");
                destFolder.mkdirs();
                FileUtils.copyFile(screenshotAs, new File(destFolder, method.getName()+".jpg"));
            } catch (IOException e) {
                logger.error("Can't take screenshot: "+e.getMessage());
            }
        }
    }

    private void updateFunctionalTemplate(IInvokedMethod invokedMethod, ITestResult iTestResult, User user) {
        List<FtOperation> ftOperations = new ArrayList<>();

        List<RequiredSetting> allSettings = getAllSettings(invokedMethod.getTestMethod());

        for (RequiredSetting setting : allSettings) {
            FTSetting settingType = setting.type();
            switch (settingType.getOperationType()) {
                case CHECKBOX:
                    ftOperations.add(setting.enabled() ? FTSettings.enable(settingType) : FTSettings.disable(settingType));
                    break;
                case INPUT:
                    ftOperations.add(FTSettings.setValue(settingType, setting.value()));
                    break;
                case SELECT:
                    ftOperations.add(FTSettings.select(settingType, setting.value()));
            }
        }

        User systemUser = UsersManager.getSystemUser();
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(systemUser);
        functionalTemplatesApi.updateTemplate(user.getFtId(), LoginPage.class, ftOperations.toArray(new FtOperation[0]));

        List<FtOperation> operationsToRollback = functionalTemplatesApi.getOperationsToRollback();
        logger.info("Found settings to rollback: ");
        for (FtOperation ftOperation : operationsToRollback) {
            logger.info("--> {} ", ftOperation.toString());
        }
        iTestResult.setAttribute(ROLLBACK_CONTEXT, new RollbackContext(user, operationsToRollback));
    }

    private void printErrorStackTraceIfAny(ITestResult iTestResult) {
        Throwable e = iTestResult.getThrowable();
        if (e != null) {
            logger.error(e.getMessage(), e);
        }
    }

    private class RollbackContext {
        private User user;
        private List<FtOperation> operations;

        RollbackContext(User user, List<FtOperation> operations) {
            this.user = user;
            this.operations = operations;
        }
    }

    private List<RequiredSetting> getAllSettings(ITestNGMethod testMethod) {
        List<RequiredSetting> requiredSettings = new ArrayList<>();
        Set<FTSetting> methodSettings = new HashSet<>();

        Class realClass = testMethod.getRealClass();
        Method method = testMethod.getConstructorOrMethod().getMethod();

        RequiredSetting[] methodAnnotations = method.getDeclaredAnnotationsByType(RequiredSetting.class);
        if (methodAnnotations != null) {
            Arrays.stream(methodAnnotations).
                    forEach(annotation -> {
                        methodSettings.add(annotation.type());
                        requiredSettings.add(annotation);
                    });
        }

        RequiredSetting[] classAnnotations = (RequiredSetting[]) realClass.getDeclaredAnnotationsByType(RequiredSetting.class);
        if (classAnnotations != null) {
            Arrays.stream(classAnnotations).
                    filter(classAnnotation -> !methodSettings.contains(classAnnotation.type())).
                    forEach(requiredSettings::add);
        }


        return requiredSettings;
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> findMethodParameter(ITestResult iTestResult, Class<T> tClass) {
        return Arrays.stream(iTestResult.getParameters()).
                filter(sc -> sc.getClass().equals(tClass)).
                map(sc -> (T) sc).findFirst();
    }

}


