package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.exceptions.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Http;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.util.List;

import static com.scalepoint.automation.utils.Http.post;

public class SupplierApi extends AuthenticationApi {

    public static final String SUPPLIER_LIST = Configuration.getEccAdminUrl() + "supplier/list.json";

    public SupplierApi(User user) {
        super(user);
    }

    public SupplierApi(Executor executor) {
        super(executor);
    }

    public String getRandomSupplier() {
        List<NameValuePair> params = Http.ParamsBuilder.create().
                add("page", "1").
                add("start", "0").
                add("limit", "1000").
                add("sort", "name").
                add("direction", "ASC").get();

        try {
            Response response = post(SUPPLIER_LIST, params, executor);

            ObjectMapper m = new ObjectMapper();
            JsonNode root = m.readTree(response.returnContent().asStream());
            JsonNode items = root.path("data").path("items");
            JsonNode item = items.path(2);
            return item.path("id").asText();
        } catch (IOException e) {
            log.error("Can't retrieve Suppliers: " + e.getMessage(), e);
            throw new ServerApiException(e);
        }
    }

    public String getSupplierId(String supplierName) {
        List<NameValuePair> params = Http.ParamsBuilder.create().
                add("search", supplierName).
                add("page", "1").
                add("start", "0").
                add("limit", "1000").
                add("sort", "name").
                add("direction", "ASC").get();

        try {
            Response response = post(SUPPLIER_LIST, params, executor);
            ObjectMapper m = new ObjectMapper();
            JsonNode root = m.readTree(response.returnContent().asStream());
            JsonNode items = root.path("data").path("items");
            JsonNode item = items.path(0);
            return item.path("id").asText();
        } catch (IOException e) {
            log.error("Can't retrieve Suppliers: " + e.getMessage(), e);
            throw new ServerApiException(e);
        }
    }
}
