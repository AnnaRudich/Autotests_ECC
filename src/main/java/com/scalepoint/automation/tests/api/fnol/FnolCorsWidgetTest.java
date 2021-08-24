package com.scalepoint.automation.tests.api.fnol;

import com.scalepoint.automation.services.restService.SelfServiceService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.SelfServiceInitData;
import com.scalepoint.automation.utils.data.request.SelfServiceLossItems;
import lombok.Builder;
import lombok.Data;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class FnolCorsWidgetTest extends FnolBaseTest {

    private static final String FNOL_CORS_DATA_PROVIDER = "fnolCorsDataProvider";
    private static final String FNOL_CORS_WITH_ATTACHMENT_DATA_PROVIDER = "fnolCorsWithAttachmentDataProvider";

    @BeforeMethod
    protected void getSSToken(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        FnolCorsTestData fnolCorsTestData = getObjectBySuperClass(parameters, FnolCorsTestData.class).get(0);

        String tenant = user.getCompanyName().toLowerCase();

        ClaimRequest itemizationRequest = setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant);

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        SelfServiceService selfServiceService = new SelfServiceService();

        String token = unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), itemizationRequest);

        String ssoToken = unifiedIntegrationService.getSSOToken(itemizationRequest.getCountry());

        String ssToken = selfServiceService.getCaseWidget(token, ssoToken).getResponse().header(ACCESS_TOKEN);

        fnolCorsTestData.setSsToken(ssToken);
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify create loss item request sent from an unauthenticated domain")
    public void corsCreateLossItemNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify update loss item request sent from an authenticated domain")
    public void corsUpdateLossItemAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .updateLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify update loss item request sent from an unauthenticated domain")
    public void corsUpdateLostItemNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .updateLossItem(selfServiceLossItems, nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify delete loss item sent from an authenticated domain")
    public void corsDeleteLossItemAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .deleteLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify delete loss item request sent from an unauthenticated domain")
    public void corsDeleteLossItemNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .deleteLossItem(selfServiceLossItems, nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_WITH_ATTACHMENT_DATA_PROVIDER,
            description = "Verify upload attachment request sent from an authenticated domain")
    public void corsUploadAttachmentAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData, File file) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .uploadAttachment(selfServiceLossItems, authOrigin, file)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_WITH_ATTACHMENT_DATA_PROVIDER,
            description = "Verify upload attachment request sent from an unauthenticated domain")
    public void corsUploadAttachmentNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData, File file) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .uploadAttachment(selfServiceLossItems, nonAuthOrigin, file)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify submitted request from an authenticated domain")
    public void corsSubmittedAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .submitted(authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify submitted request from an authenticated domain")
    public void corsSubmittedNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .submitted(nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify saved request from an authenticated domain")
    public void corsSavedAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .saved(authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify saved request from an unauthenticated domain")
    public void corsSavedNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .saved(nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify get attachment request from an authenticated domain")
    public void corsGetAttachmentAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .getAttachment(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify get attachment request from an nauthenticated domain")
    public void corsGetAttachmentNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())

                .getAttachment(selfServiceLossItems, nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_WITH_ATTACHMENT_DATA_PROVIDER,
            description = "Verify delete attachment request from an authenticated domain")
    public void corsDeleteAttachmentAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData, File file) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .uploadAttachment(selfServiceLossItems, authOrigin, file)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .getAttachment(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .deleteAttachment(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_WITH_ATTACHMENT_DATA_PROVIDER,
            description = "Verify delete attachment request from an unauthenticated domain")
    public void corsDeleteAttachmentNonAuthTest(User user, SelfServiceLossItems selfServiceLossItems, FnolCorsPostTestData fnolCorsPostTestData, File file) {

        new SelfServiceService(fnolCorsPostTestData.getSsToken())
                .addLossItem(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .uploadAttachment(selfServiceLossItems, authOrigin, file)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .getAttachment(selfServiceLossItems, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .deleteAttachment(selfServiceLossItems, nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify put loss request from an authenticated domain")
    public void corsUpdateLossAuthTest(User user, FnolCorsPostTestData fnolCorsPostTestData) {

        SelfServiceService selfServiceService = new SelfServiceService(fnolCorsPostTestData.getSsToken());

        SelfServiceInitData selfServiceInitData = selfServiceService
                .getInitData(authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .getResponse()
                .then()
                .log().all().extract().response()
                .getBody().as(SelfServiceInitData.class);

        selfServiceInitData.getData().getSelfServiceLoss().setAccepted(true);

        selfServiceService.updateLoss(selfServiceInitData, authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK());
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_WIDGET}, dataProvider = FNOL_CORS_DATA_PROVIDER,
            description = "Verify put loss request from an unauthenticated domain")
    public void corsUpdateLossNonAuthTest(User user, FnolCorsPostTestData fnolCorsPostTestData) {

        SelfServiceService selfServiceService = new SelfServiceService(fnolCorsPostTestData.getSsToken());

        SelfServiceInitData selfServiceInitData = selfServiceService
                .getInitData(authOrigin)
                .doAssert(ss -> ss.assertStatusCodeOK())
                .getResponse()
                .then()
                .log().all().extract().response()
                .getBody().as(SelfServiceInitData.class);

        selfServiceInitData.getData().getSelfServiceLoss().setAccepted(true);

        selfServiceService.updateLoss(selfServiceInitData, nonAuthOrigin)
                .doAssert(ss -> ss.assertStatusInvalidCors());
    }

    @DataProvider(name = FNOL_CORS_DATA_PROVIDER)
    public static Object[][] fnolCorsWidgetDataProvider(Method method) {

        new File("src\\main\\resources\\attachments\\bw.jpg");

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, FnolCorsPostTestData.builder().build()).toArray()
        };
    }

    @DataProvider(name = FNOL_CORS_WITH_ATTACHMENT_DATA_PROVIDER)
    public static Object[][] fnolCorsWidgetWithFileDataProvider(Method method) {

        File file = new File("src\\main\\resources\\attachments\\bw.jpg");

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, FnolCorsPostTestData.builder().build(), file).toArray()
        };
    }

    @Data
    @Builder
    static class FnolCorsPostTestData extends FnolCorsTestData{}
}
