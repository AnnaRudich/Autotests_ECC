package com.scalepoint.automation.utils.listeners;

import com.codeborne.selenide.Selenide;
import com.scalepoint.automation.exceptions.InvalidFtOperationException;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.FeaturesToggleAdministrationService;
import com.scalepoint.automation.services.restService.FeaturesToggleAdministrationService.ActionsOnToggle;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.GridInfoUtils;
import com.scalepoint.automation.utils.SystemUtils;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InvokedMethodListener implements IInvokedMethodListener {

    public static final String ROLLBACK_CONTEXT = "rollback_context";

    protected Logger logger = LogManager.getLogger(InvokedMethodListener.class);

    private String gridNode;

    private Map<FeatureIds, Boolean> featureTogglesDefaultState = new HashMap<>();

    @Override
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult iTestResult) {
        if (invokedMethod.isTestMethod()) {
            if (Browser.driver() != null) {
                logger.info("Using driver type: " + Browser.getDriverType());
                logger.info("Start from: " + SystemUtils.getHostname());
                gridNode = GridInfoUtils.getGridNodeName(((RemoteWebDriver) Browser.driver()).getSessionId());
                logger.info("Running on grid node: " + gridNode);
                retryUpdateFtTemplate(invokedMethod, iTestResult);
                updateFeatureToggle(invokedMethod);
            }
        }
    }

    private void retryUpdateFtTemplate(IInvokedMethod invokedMethod, ITestResult iTestResult) {
        int attempt = 0;
            /*sometimes we get java.net.SocketTimeoutException: Read timed out, so lets try again*/
        while (attempt <= 1) {
            try {
                updateTemplate(invokedMethod, iTestResult);
                break;
            } catch (InvalidFtOperationException e){
                throw e;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                logger.error("Next attempt");
                attempt++;
                if (attempt > 1) {
                    throw e;
                }
            }
        }
    }

    private void updateTemplate(IInvokedMethod invokedMethod, ITestResult iTestResult) {
        Optional<User> optionalUser = findMethodParameter(iTestResult, User.class);
        logger.info("-------- InvokedMethodListener before. Thread: {} ----------", Thread.currentThread().getId());
        if (optionalUser.isPresent()) {
            Page.to(LoginPage.class);
            updateFunctionalTemplate(invokedMethod, iTestResult, optionalUser.get());
            Browser.driver().manage().deleteAllCookies();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if(Browser.driver() != null) {
            if (iInvokedMethod.isTestMethod()) {
                try {
                    takeScreenshot(iInvokedMethod.getTestMethod().getConstructorOrMethod().getMethod(), iTestResult);

                    logger.info("-------- InvokedMethodListener after. Thread: {} ----------", Thread.currentThread().getId());
                    printErrorStackTraceIfAny(iTestResult);

                    RollbackContext rollbackContext = (RollbackContext) iTestResult.getAttribute(ROLLBACK_CONTEXT);
                    if (rollbackContext == null || rollbackContext.getOperations().isEmpty()) {
                        logger.info("No ft settings found to rollback");
                        return;
                    }

                    if(featureTogglesDefaultState.isEmpty()){
                        logger.info("No feature toggle to rollback");
                    }

                    Page.to(LoginPage.class);

                    FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(UsersManager.getSystemUser());
                    List<FtOperation> operations = rollbackContext.getOperations();
                    functionalTemplatesApi.updateTemplate(rollbackContext.getUser().getFtId(), LoginPage.class, operations.toArray(new FtOperation[0]));

                    FeaturesToggleAdministrationService featuresToggleAdminApi = new FeaturesToggleAdministrationService();
                    FeatureIds toggleSetting = getToggleSetting(iInvokedMethod.getTestMethod()).type();
                    ActionsOnToggle expectedActionOnToggle;

                    if (!featureTogglesDefaultState.get((toggleSetting))) {
                        expectedActionOnToggle = ActionsOnToggle.enable;
                    } else expectedActionOnToggle = ActionsOnToggle.disable;

                    featuresToggleAdminApi.updateToggle(expectedActionOnToggle, toggleSetting);


                } catch (Exception e) {
                /* if not caught it breaks the call of AfterMethod*/
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ResultOfMethodCallIgnored"})
    private void takeScreenshot(Method method, ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            Selenide.screenshot(getFileName(method));
        }
    }

    private String getFileName(Method method) {
        return "node_" + gridNode.replace("http://", "").replace(gridNode.substring(gridNode.lastIndexOf(":")), "")
                + "_" + Browser.getDriverType()
                + "_" + method.getName();
    }


    private FeatureToggleSetting getToggleSetting(ITestNGMethod testMethod) {
        Method method = testMethod.getConstructorOrMethod().getMethod();
        return method.getDeclaredAnnotation(FeatureToggleSetting.class);
    }


    private void updateFeatureToggle(IInvokedMethod invokedMethod) {
        FeaturesToggleAdministrationService featureToggleService = new FeaturesToggleAdministrationService();

        FeatureToggleSetting toggleSetting = getToggleSetting(invokedMethod.getTestMethod());
        if (toggleSetting == null) {
            return;
        }

        featureTogglesDefaultState.put(toggleSetting.type(), featureToggleService.getToggleStatus(toggleSetting.type().name()));

        if (toggleSetting.enabled()) {
            featureToggleService.updateToggle(ActionsOnToggle.enable, toggleSetting.type());
        } else {
            featureToggleService.updateToggle(ActionsOnToggle.disable, toggleSetting.type());
        }
    }


    private void updateFunctionalTemplate(IInvokedMethod invokedMethod, ITestResult iTestResult, User user) {
        List<FtOperation> ftOperations = new ArrayList<>();
        List<FtOperation> defaultFtOperations = new ArrayList<>();

        List<RequiredSetting> allSettings = getAllSettings(invokedMethod.getTestMethod());

        if (allSettings.isEmpty()) {
            return;
        }

        for (RequiredSetting setting : allSettings) {
            FTSetting settingType = setting.type();
            switch (settingType.getOperationType()) {
                case CHECKBOX:
                    if (setting.isDefault()) {
                        defaultFtOperations.add(setting.enabled() ? FTSettings.enable(settingType) : FTSettings.disable(settingType));
                    } else {
                        ftOperations.add(setting.enabled() ? FTSettings.enable(settingType) : FTSettings.disable(settingType));
                    }
                    break;
                case INPUT:
                    if (setting.isDefault()) {
                        defaultFtOperations.add(FTSettings.setValue(settingType, setting.value()));
                    } else {
                        ftOperations.add(FTSettings.setValue(settingType, setting.value()));
                    }
                    break;
                case SELECT:
                    if (setting.isDefault()) {
                        defaultFtOperations.add(FTSettings.select(settingType, setting.value()));
                    } else {
                        ftOperations.add(FTSettings.select(settingType, setting.value()));
                    }
            }
        }

        checkDefaultSettings(user, defaultFtOperations);
        updateFtTemplateWithRequiredSettings(user, ftOperations, iTestResult);
    }

    private void checkDefaultSettings(User user, List<FtOperation> ftOperations){
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(UsersManager.getSystemUser());
        FTSettings.ComparingResult comparingResult = functionalTemplatesApi.findDifferences(user.getFtId(), ftOperations.toArray(new FtOperation[0]));

        if(!comparingResult.hasSameStateAsRequested()){
            throw new InvalidFtOperationException(comparingResult.getDifferedOperations());
        }
    }

    private void updateFtTemplateWithRequiredSettings(User user, List<FtOperation> ftOperations, ITestResult iTestResult){
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(UsersManager.getSystemUser());
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


