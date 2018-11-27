package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.exceptions.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.utils.Http.get;
import static com.scalepoint.automation.utils.Http.post;

public class ServiceAgreementApi extends AuthenticationApi {

    public static final String URL_LIST = Configuration.getEccAdminUrl() + "serviceAgreement/list.json?page=1&start=0&limit=25&sort=name&direction=ASC&supplierId=";
    public static final String URL_BY_COMPANY = Configuration.getEccAdminUrl() + "serviceAgreement/templatesForCompany.json?page=1&start=0&limit=25";
    public static final String URL_ADD_AGREEMENT = Configuration.getEccAdminUrl() + "serviceAgreement/addServiceAgreement.json?supplierId=";

    public ServiceAgreementApi(User user) {
        super(user);
    }

    public ServiceAgreementApi(Executor executor) {
        super(executor);
    }

    public void createServiceAgreement(String supplierName, String agreementName, List<String> types, String templateName) {
        SupplierApi supplierApi = new SupplierApi(executor);
        ServiceAgreementApi serviceAgreementApi = new ServiceAgreementApi(executor);

        String supplierId = supplierApi.getSupplierId(supplierName);
        String templateId = serviceAgreementApi.getAgreementTemplateId(templateName);

        createAgreement(supplierId, agreementName, types, templateId);
    }

    public String getAgreementIdForSupplier(String supplierId, String agreementName) {
        try {
            return findAgreementId(URL_LIST + supplierId, agreementName);
        } catch (IOException e) {
            log.error("Can't create Agreement: " + e.getMessage(), e);
            throw new ServerApiException(e);
        }
    }

    public String getAgreementTemplateId(String agreementName) {
        try {
            return findAgreementId(URL_BY_COMPANY, agreementName);
        } catch (IOException e) {
            log.error("Can't create Agreement: " + e.getMessage(), e);
            throw new ServerApiException(e);
        }
    }

    private void createAgreement(String supplierId, String agreementName, List<String> types, String templateId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", agreementName));
        params.add(new BasicNameValuePair("status", "ACTIVE"));
        params.add(new BasicNameValuePair("emailType", "SUPPLIER"));
        params.add(new BasicNameValuePair("defaultTemplateId", templateId));
        types.forEach(t -> params.add(new BasicNameValuePair("types", t)));

        try {
            post(URL_ADD_AGREEMENT + supplierId, params, executor);
        } catch (IOException e) {
            log.error("Can't create Agreement: " + e.getMessage(), e);
            throw new ServerApiException(e);
        }
        /*
        name	Agr1
        types	REPAIR
        types	REPAIR_ESTIMATE
        types	VALUATION
        types	MATCH_SERVICE
        status	ACTIVE
        emailType	SUPPLIER
        defaultTemplateId	3
        */
    }

    private String findAgreementId(String listUrl, String agreementName) throws IOException {
        Response response = get(listUrl, executor);
        ObjectMapper m = new ObjectMapper();
        String id = null;
        JsonNode root = m.readTree(response.returnContent().asStream());
        JsonNode items = root.path("data").path("items");
        for (int i = 0; i < items.size(); i++) {
            JsonNode item = items.path(i);
            if (item.path("name").asText().equals(agreementName)) {
                id = item.path("id").asText();
                break;
            }
        }
        return id;
    }


}
