package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.scalepoint.automation.exceptions.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.Category;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.utils.Http.*;

public class PseudoCategoryApi extends AuthenticationApi {

    private static final String URL_ASSIGN_CATEGORY = Configuration.getEccAdminUrl() + "category/allCategories.json?voucherId=";
    private static final String URL_ALL_CATEGORIES = Configuration.getEccAdminUrl() + "category/allCategories.json?voucherId=";

    public PseudoCategoryApi(Executor executor) {
        super(executor);
    }

    public NameValuePair getRandomCategory(String voucherId) {
        try {
            Response response = get(URL_ALL_CATEGORIES + voucherId, executor);

            ObjectMapper m = new ObjectMapper();
            JsonNode root = m.readTree(response.returnContent().asStream());
            JsonNode data = root.path("data");
            JsonNode item = data.path("allItems").path(0);
            return new BasicNameValuePair(item.path("text").asText(), item.path("value").asText());
        } catch (IOException e) {
            log.error("Can't driver Categories", e);
            throw new RuntimeException(e);
        }
    }

    public void assignPseudoCategoryToVoucherAgreement(String voucherId, String... categories) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("selectedCategories", Joiner.on(",").skipNulls().join(categories)));
        try {
            post(URL_ASSIGN_CATEGORY + voucherId, params, executor);
        } catch (IOException e) {
            log.error("Can't map Category to Voucher", e);
            throw new ServerApiException(e);
        }
    }
}