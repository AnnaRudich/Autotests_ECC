package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
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

public class PseudoCategoryApi extends ServerApi {

    private static final String URL_ASSIGN_CATEGORY = Configuration.getEccAdminUrl() + "category/allCategories.json?voucherId=";
    private static final String URL_ALL_CATEGORIES = Configuration.getEccAdminUrl() + "category/allCategories.json?voucherId=";

    private static final String URL_ALL_CATEGORIES_PAGE = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/pseudo_categories.jsp";
    private static final String URL_EDIT_CATEGORY_PAGE = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/pseudo_category_edit.jsp?pcrfnbr=-1";
    private static final String URL_CREATE_CATEGORY_GROUP = Configuration.getEccUrl() + "SavePseudoCategoryGroup";
    private static final String URL_CREATE_CATEGORY = Configuration.getEccUrl() + "SavePseudoCategory";

    public PseudoCategoryApi(User user) {
        super(user);
    }

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

    public void assignPseudoCategoryToVoucherAgreementName(String voucherName, String... categories) {
        VoucherAgreementApi voucherAgreementApi = new VoucherAgreementApi(executor);
        String voucherId = voucherAgreementApi.getVoucherIdByName(voucherName);
        assignPseudoCategoryToVoucherAgreement(voucherId, categories);
    }

    public String createPseudoCategoryAndReturnId(Category category) {
        try {
            createPseudoCategory(category);
            Content categoryContent = get(URL_ALL_CATEGORIES_PAGE, executor).returnContent();
            String categoryCont = categoryContent.toString();
            String contentByCat = categoryCont.split("\">" + category.getCategoryName())[0];
            String[] psNumSplit = contentByCat.split("<option value=\"");
            return psNumSplit[psNumSplit.length - 1].replaceAll("[^\\d]", "");
        } catch (IOException e) {
            log.error("Can't create Pseudo Category", e);
            throw new ServerApiException(e);
        }
    }

    public void createPseudoCategory(Category category) {
        try {

            Content content = get(URL_EDIT_CATEGORY_PAGE, executor).returnContent();
            String cont = content.toString();
            String[] contSplit = cont.split(category.getGroupName())[0].split("<option value=");
            String groupId = contSplit[contSplit.length - 1].replaceAll("[\"<>]", "");

            List<NameValuePair> params = ParamsBuilder.create().
                    add("pcrfnbr", "-1").
                    add("pseudocatgroupid", "-1").
                    add("fromPCGPage", "0").
                    add("multistringlanguagechooser", "1030").
                    add("pcname_1030", category.getCategoryName()).
                    add("pcPublished", "On").
                    add("pccgnbr", "-1").
                    add("PseudoCatGroupId", groupId).get();

            int statusCode = post(URL_CREATE_CATEGORY, params, executor).returnResponse().getStatusLine().getStatusCode();
            ensure302Code(statusCode);
            log.info("PS category was created. Name is: " + category.getCategoryName());
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public void createPseudoCategoryGroup(Category category) {
        List<NameValuePair> params = ParamsBuilder.create().
                add("pseudocatgroupid", "-1").
                add("defaultpseudocat", "-1").
                add("multistringlanguagechooser", "1030").
                add("pcgroupname_1030", category.getGroupName()).
                add("pcgEnabled", "ON").get();

        try {
            ensure302Code(post(URL_CREATE_CATEGORY_GROUP, params, executor).returnResponse().getStatusLine().getStatusCode());
            log.info("Pseudocategory group was created. Name: " + category.getGroupName());
        } catch (IOException e) {
            log.error("Can't create Pseudo Category Group", e);
            throw new ServerApiException(e);
        }
    }
}