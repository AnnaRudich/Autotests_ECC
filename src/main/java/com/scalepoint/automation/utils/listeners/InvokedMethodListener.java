package com.scalepoint.automation.utils.listeners;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.scalepoint.automation.exceptions.InvalidFtOperationException;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
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
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
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
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.listeners.DefaultFTOperations.getDefaultFTSettings;

public class InvokedMethodListener implements IInvokedMethodListener {

    public static final String ROLLBACK_CONTEXT = "rollback_context";

    protected Logger logger = LogManager.getLogger(InvokedMethodListener.class);

    private String gridNode;

    private Map<FeatureIds, Boolean> featureTogglesDefaultState = new HashMap<>();

    @Override
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult iTestResult) {

        if (invokedMethod.isTestMethod()) {
            if (Browser.hasDriver()) {
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
            } catch (InvalidFtOperationException e) {
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
            updateFunctionalTemplate(invokedMethod, optionalUser.get());
            Browser.driver().manage().deleteAllCookies();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (Browser.hasDriver()) {
            if (iInvokedMethod.isTestMethod()) {
                try {

                    takeScreenshot(iInvokedMethod.getTestMethod().getConstructorOrMethod().getMethod(), iTestResult);

                    logger.info("-------- InvokedMethodListener after. Thread: {} ----------", Thread.currentThread().getId());
                    printErrorStackTraceIfAny(iTestResult);

                    int left = TestCountdown.countDown(iInvokedMethod.getTestMethod().getRealClass().getSimpleName()
                            + "." + iInvokedMethod.getTestMethod().getMethodName()
                    );

                    logger.info("Left tests: {}", left);

                    cleanUpCDTemplates(iInvokedMethod, iTestResult);

                    if (featureTogglesDefaultState.isEmpty()) {
                        logger.info("No feature toggle to rollback");
                    } else {
                        rollbackToggleSetting(iInvokedMethod);
                    }

                    Browser.open(com.scalepoint.automation.utils.Configuration.getLogoutUrl());
                    Page.to(LoginPage.class);
                } catch (Exception e) {
                    /* if not caught it breaks the call of AfterMethod*/
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void cleanUpCDTemplates(IInvokedMethod iInvokedMethod, ITestResult iTestResult){
        boolean cleanUp = iInvokedMethod
                .getTestMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getDeclaredAnnotation(CommunicationDesignerCleanUp.class) != null;
        if(cleanUp) {
            User user = findMethodParameter(iTestResult, User.class).get();
            Page.to(InsCompAddEditPage.class, user.getCompanyId())
                    .setCommunicationDesignerSection(CommunicationDesigner.reset())
                    .selectSaveOption(false);
        }
    }

    private void rollbackToggleSetting(IInvokedMethod iInvokedMethod) {

        FeaturesToggleAdministrationService featuresToggleAdminApi = new FeaturesToggleAdministrationService();
        final FeatureToggleSetting toggleSetting = getToggleSetting(iInvokedMethod.getTestMethod());
        if (toggleSetting == null) {
            return;
        }

        FeatureIds toggleSettingType = toggleSetting.type();
        Boolean initialState = featureTogglesDefaultState.get(toggleSettingType);
        featuresToggleAdminApi.updateToggle(ActionsOnToggle.of(initialState), toggleSettingType);
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ResultOfMethodCallIgnored"})
    private void takeScreenshot(Method method, ITestResult iTestResult) {
        String fileName = getFileName(method);
        if (!iTestResult.isSuccess()) {
            try {
                takeScreenshot(fileName);
            } catch (Exception e) {
                logger.error("Can't make screenshot with ashot for {} cause {}", method.getName(), e.getMessage());
                Selenide.screenshot(fileName);
            }
        }
    }

    private void takeScreenshot(String fileName) throws IOException {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(Browser.driver());
        BufferedImage image = screenshot.getImage();
        File imageFile = new File(Configuration.reportsFolder, fileName + ".png");
        ImageIO.write(image, "png", imageFile);
    }

    private String getFileName(Method method) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
        return method.getName() + "_" + sdf.format(new Date()) + "_node_" + gridNode.replace("http://", "").replace(":", "")
                + "_" + Browser.getDriverType();
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

        boolean toggleActualState = featureToggleService.getToggleStatus(toggleSetting.type().name());
        boolean toggleExpectedState = toggleSetting.enabled();

        if (toggleActualState != toggleExpectedState) {
            featureTogglesDefaultState.put(toggleSetting.type(), toggleActualState);
            featureToggleService.updateToggle(ActionsOnToggle.of(toggleExpectedState), toggleSetting.type());
        }
    }


    private void updateFunctionalTemplate(IInvokedMethod invokedMethod, User user) {
        List<RequiredSetting> allSettings = getAllSettings(invokedMethod.getTestMethod());

        String companyCode = user.getCompanyCode();

        if(companyCode != null) {

            List<FtOperation> defaultList = getDefaultFTSettings(companyCode);

            if (!allSettings.isEmpty()) {
                for (RequiredSetting setting : allSettings) {
                    FTSetting settingType = setting.type();
                    defaultList = defaultList
                            .stream()
                            .filter(ftOperation ->
                                    !ftOperation.getSetting().equals(settingType))
                            .collect(Collectors.toList());
                    switch (settingType.getOperationType()) {
                        case CHECKBOX:
                            defaultList.add(setting.enabled() ? FTSettings.enable(settingType) : FTSettings.disable(settingType));
                            break;
                        case INPUT:
                            defaultList.add(FTSettings.setValue(settingType, setting.value()));
                            break;
                        case SELECT:
                            defaultList.add(FTSettings.select(settingType, setting.value()));
                    }
                }
            }

            updateFtTemplateWithRequiredSettings(user, defaultList);
        }
    }

    private void updateFtTemplateWithRequiredSettings(User user, List<FtOperation> ftOperations) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(UsersManager.getSystemUser());
        functionalTemplatesApi.updateTemplate(user.getFtId(), LoginPage.class, ftOperations.toArray(new FtOperation[0]));
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


