package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Http;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.scalepoint.automation.utils.Http.post;

@SuppressWarnings("unchecked")
public class VoucherAgreementApi extends AuthenticationApi {

    private static final String URL_ADD_VOUCHER = Configuration.getEccAdminUrl() + "voucher/addVoucher.json?supplierId=";
    private static final String URL_LIST = Configuration.getEccAdminUrl() + "voucher/listAll.json";

    public VoucherAgreementApi(User user) {
        super(user);
    }

    public VoucherAgreementApi(Executor executor) {
        super(executor);
    }

    public AssignedCategory createVoucher(Voucher voucher) {
        SupplierApi supplierApi = new SupplierApi(executor);
        PseudoCategoryApi pseudoCategoryApi = new PseudoCategoryApi(executor);

        String randomSupplier = supplierApi.getRandomSupplier();
        String voucherId = createNewVoucherAgreement(randomSupplier, voucher);
        NameValuePair category = pseudoCategoryApi.getRandomCategory(voucherId);

        pseudoCategoryApi.assignPseudoCategoryToVoucherAgreement(voucherId, category.getValue());
        String[] categories = category.getName().split("(?<=.)(?=(- +\\p{Lu}))");
        categories[1] = categories[1].replaceFirst("-", "");

        return new AssignedCategory(categories[0].trim(), categories[1].trim());
    }

    public String getVoucherIdByName(String name) {
        List<NameValuePair> params = Http.ParamsBuilder.create().
                add("sort", "voucherName").
                add("direction", "ASC").
                add("start", "0").
                add("limit", "500").get();

        String id = "";
        try {
            Content content = post(URL_LIST, params, executor).returnContent();
            HashMap<String, Object> result = new ObjectMapper().readValue(content.toString(), HashMap.class);
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            ArrayList<HashMap<String, Object>> items = (ArrayList) data.get("items");
            for (HashMap<String, Object> hashMap : items) {
                if (hashMap.get("voucherName").equals(name))
                    id = String.valueOf(hashMap.get("voucherId"));
            }
        } catch (IOException e) {
            log.error("Can't driver voucher by id", e);
            throw new ServerApiException(e);
        }
        return id;
    }


    private String createNewVoucherAgreement(String supplierId, Voucher voucher) {
        List<NameValuePair> params = Http.ParamsBuilder.create().
                add("voucherName", voucher.getVoucherGeneratedName()).
                add("agreementStatus", "ACTIVE").
                add("useSupplierLogo", "true").
                add("useSupplierUrl", "true").
                add("agreementDiscount", voucher.getDiscount().toString()).
                add("minimumAmount", "1").
                add("stepAmount", "1").
                add("brands", voucher.getBrandsText()).
                add("tags", voucher.getTagsText()).
                add("orderFeeId", "-1").get();

        try {
            Response response = post(URL_ADD_VOUCHER + supplierId, params, executor);

            ObjectMapper m = new ObjectMapper();
            JsonNode root = m.readTree(response.returnContent().asStream());
            JsonNode data = root.path("data");
            return data.path("id").asText();

        } catch (IOException e) {
            log.error("Can't create New Voucher Agreement", e);
            throw new ServerApiException(e);
        }
    }

    public static class AssignedCategory {
        private String category;
        private String subCategory;

        public AssignedCategory(String category, String subCategory) {
            this.category = category;
            this.subCategory = subCategory;
        }

        public String getCategory() {
            return category;
        }

        public String getSubCategory() {
            return subCategory;
        }
    }
}