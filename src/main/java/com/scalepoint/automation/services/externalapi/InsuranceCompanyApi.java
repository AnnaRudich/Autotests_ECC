package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Http;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;

import java.io.IOException;
import java.util.List;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static com.scalepoint.automation.utils.Http.ensure302Code;
import static com.scalepoint.automation.utils.Http.post;

public class InsuranceCompanyApi extends ServerApi {

    public static String URL_CREATE_IC = getEccUrl() + "SaveInsuranceCompany";

    public InsuranceCompanyApi(User user) {
        super(user);
    }

    public InsuranceCompanyApi(Executor executor) {
        super(executor);
    }

    public void createInsuranceCompany(InsuranceCompany insuranceCompany) {
        try {
            ensure302Code(post(URL_CREATE_IC, toParams(insuranceCompany), executor).returnResponse().getStatusLine().getStatusCode());
            log.info("Insurance companyCode was created. IC name: " + insuranceCompany.getIcName());
        } catch (IOException e) {
            log.error("Can't create IC", e);
            throw new ServerApiException(e);
        }
    }

    private static List<NameValuePair> toParams(InsuranceCompany insuranceCompany) {
        String cultureParam;
        String funcTemplateParam;
        String guiTemplateParam;

        switch (Configuration.getLocale()) {
            case "dk":
            default:
                cultureParam = "da-DK";
                break;
            case "gb":
                cultureParam = "en-GB";
                break;
        }

        if (Configuration.getLocale().equals("dk")) {
            funcTemplateParam = "140053";
            guiTemplateParam = "16";
        } else {
            funcTemplateParam = "1";
            guiTemplateParam = "1";
        }

        return Http.ParamsBuilder.create().
                add("icrfnbr", "-1").
                add("icFlagOverrideValue", "0").
                add("id", insuranceCompany.getIcID()).
                add("CompanyCode", insuranceCompany.getIcCode()).
                add("icname", insuranceCompany.getIcName()).
                add("icaddr1", insuranceCompany.getAddress()).
                add("icaddr2", "").
                add("iczipc", insuranceCompany.getZipCode()).
                add("iccity", insuranceCompany.getIcCity()).
                add("icstatecode", "").
                add("iclogo", "").
                add("icnewshoplogo", "").
                add("icNewShopUrlSpecifier", "").
                add("icpdflogo", "").
                add("kvitteringStoreName", "").
                add("icurl", "").
                add("iccommail", insuranceCompany.getCompanyCommonMail()).
                add("icprfnbr", "-1").
                add("icCulture", cultureParam).
                add("icTrustpilotMail", "").
                add("icRecieveSelfServiceNotification", "ON").
                add("icSendSelfServiceNotificationTo", "2").
                add("icRecieveExcelImportNotification", "ON").
                add("icSendExcelImportNotificationTo", "2").
                add("icEmailAddressInCustomerMail", "2").
                add("SMSdisplayName", "").
                add("multistringlanguagechooser", "1030").
                add("icContactNo_1030", insuranceCompany.getContactNumber()).
                add("icOfficeHour_1030", insuranceCompany.getOfficeHours()).
                add("icftnbr", funcTemplateParam).
                add("icgtnbr", guiTemplateParam).
                get();
    }


}
