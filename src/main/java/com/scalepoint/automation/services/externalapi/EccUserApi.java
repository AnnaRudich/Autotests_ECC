package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.NewSystemUser;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

import static com.scalepoint.automation.utils.Http.*;

public class EccUserApi extends ServerApi {

    public static final String URL_USER_ADD_PAGE = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/user_edit.jsp?shrfnbr=-1&url=/webshop/jsp/toAdminPage/users.jsp";
    public static final String URL_CREATE_USER = Configuration.getEccUrl() + "SaveUser";
    public static final String URL_ALL_USERS_PAGE = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/users.jsp";

    public EccUserApi(User user) {
        super(user);
    }

    public EccUserApi(Executor executor) {
        super(executor);
    }


    public void createEccUser(NewSystemUser newSystemUser) {
        try {
            Content content = get(URL_USER_ADD_PAGE, executor).returnContent();
            String cont = content.toString();
            String[] contSplit = cont.split(newSystemUser.getCompanyIC())[0].split("<option value=\"");
            String companyId = contSplit[contSplit.length - 1].replaceAll("[\",<> ']", "");

            List<NameValuePair> userParams = toParams(newSystemUser, companyId);

            HttpResponse httpResponse = post(URL_CREATE_USER, userParams, executor).returnResponse();
            ensure302Code(httpResponse.getStatusLine().getStatusCode());

            log.info("NewSystemUser " + newSystemUser.getFirstName() + " was created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<NameValuePair> toParams(NewSystemUser newSystemUser, String companyId) {
        BasicNameValuePair userCulture;
        String locale = Configuration.getLocale().getValue();
        switch (locale) {
            case "dk":
            default:
                userCulture = new BasicNameValuePair("userCulture", "da-DK");
                break;
            case "gb":
                userCulture = new BasicNameValuePair("userCulture", "en-GB");
                break;
        }

        return ParamsBuilder.create().
                add("url", URL_ALL_USERS_PAGE).
                add("shrfnbr", "-1").
                add("userCompanyId", companyId).
                add("userLogin", newSystemUser.getLogin()).
                add("userPassword1", newSystemUser.getPassword()).
                add("btnGenerate", "-&lt;&lt; <U>G</U>enerate").
                add("userPassword2", newSystemUser.getPassword()).
                add("userFirstName", newSystemUser.getFirstName()).
                add("userMiddleName", "").
                add("userLastName", newSystemUser.getLastName()).
                add("userEmail", newSystemUser.getEmail()).
                add("address1", "").
                add("address2", "").
                add("city", "").
                add("postalcode", "").
                add("mobileno", "").
                add("userCanModifyClaimnumbers", "ON").
                add("userCanCreateNewCases", "ON").
                add("userType", "2").
                add("userType", "8").
                add("userPasswordGenerateType", "R").
                add("userEnabled", "ON").
                add(userCulture).get();
    }

}
