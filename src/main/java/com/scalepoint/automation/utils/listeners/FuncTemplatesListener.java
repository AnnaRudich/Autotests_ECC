package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.annotations.functemplate.SettingRequired;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.*;

public class FuncTemplatesListener implements IInvokedMethodListener {

    private static final String ROLLBACK_CONTEXT = "rollback_context";

    private static Logger logger = LoggerFactory.getLogger(FuncTemplatesListener.class);

    @Override
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult iTestResult) {

        try {
            if (invokedMethod.isTestMethod()) {
                Optional<User> optionalUser = findUserInParameters(iTestResult);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    List<FtOperation> ftOperations = new ArrayList<>();
                    Set<SettingRequired> allSettings = getAllSettings(invokedMethod.getTestMethod());
                    for (SettingRequired setting : allSettings) {
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

                    FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(user);
                    functionalTemplatesApi.updateTemplate(user.getFtId(), LoginPage.class, ftOperations.toArray(new FtOperation[0]));

                    List<FtOperation> operationsToRollback = functionalTemplatesApi.getOperationsToRollback();
                    logger.info("Found settings to rollback: ");
                    for (FtOperation ftOperation : operationsToRollback) {
                        logger.info("--> {} ", ftOperation.toString());
                    }
                    iTestResult.setAttribute(ROLLBACK_CONTEXT, new RollbackContext(user, operationsToRollback));
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
            printErrorStackTraceIfAny(iTestResult);

            RollbackContext rollbackContext = (RollbackContext) iTestResult.getAttribute(ROLLBACK_CONTEXT);
            if (rollbackContext == null || rollbackContext.operations.isEmpty()) {
                logger.info("No ft settings found to rollback");
                return;
            }
            FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(rollbackContext.user);
            functionalTemplatesApi.updateTemplate(rollbackContext.user.getFtId(), LoginPage.class, rollbackContext.operations.toArray(new FtOperation[0]));
        }
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

    private Set<SettingRequired> getAllSettings(ITestNGMethod testMethod) {
        Set<SettingRequired> requiredSettings = new HashSet<>();
        Set<FTSetting> methodSettings = new HashSet<>();

        Class realClass = testMethod.getRealClass();
        Method method = testMethod.getConstructorOrMethod().getMethod();

        SettingRequired[] methodAnnotations = method.getDeclaredAnnotationsByType(SettingRequired.class);
        if (methodAnnotations != null) {
            Arrays.stream(methodAnnotations).
                    forEach(annotation -> {
                        methodSettings.add(annotation.type());
                        requiredSettings.add(annotation);
                    });
        }

        SettingRequired[] classAnnotations = (SettingRequired[]) realClass.getDeclaredAnnotationsByType(SettingRequired.class);
        if (classAnnotations != null) {
            Arrays.stream(classAnnotations).
                    filter(classAnnotation -> !methodSettings.contains(classAnnotation.type())).
                    forEach(requiredSettings::add);
        }

        return requiredSettings;
    }

    private Optional<User> findUserInParameters(ITestResult iTestResult) {
        return Arrays.stream(iTestResult.getParameters()).
                filter(sc -> sc instanceof User).
                map(sc -> (User) sc).findFirst();
    }

}


