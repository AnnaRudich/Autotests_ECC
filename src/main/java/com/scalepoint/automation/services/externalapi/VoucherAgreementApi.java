package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.exceptions.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Http;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.PseudoCategory;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.util.List;

import static com.scalepoint.automation.utils.Http.post;

@SuppressWarnings("unchecked")
public class VoucherAgreementApi extends AuthenticationApi {

    private static final String URL_ADD_VOUCHER = Configuration.getEccAdminUrl() + "voucher/addVoucher.json?supplierId=";

    public VoucherAgreementApi(User user) {
        super(user);
    }

    public PseudoCategory createVoucher(Voucher voucher) {
        SupplierApi supplierApi = new SupplierApi(executor);
        PseudoCategoryApi pseudoCategoryApi = new PseudoCategoryApi(executor);

        String randomSupplier = supplierApi.getRandomSupplier();
        String voucherId = createNewVoucherAgreement(randomSupplier, voucher);
        NameValuePair category = pseudoCategoryApi.getRandomCategory(voucherId);

        pseudoCategoryApi.assignPseudoCategoryToVoucherAgreement(voucherId, category.getValue());
        String[] categories = category.getName().split("(?<=.)(?=(- +\\p{Lu}))");
        categories[1] = categories[1].replaceFirst("-", "");

        return new PseudoCategory(categories[0].trim(), categories[1].trim());
    }

    private String createNewVoucherAgreement(String supplierId, Voucher voucher) {
        List<NameValuePair> params = Http.ParamsBuilder.create().
                add("voucherName", voucher.getVoucherNameSP()).
                add("agreementStatus", "ACTIVE").
                add("useSupplierLogo", "true").
                add("useSupplierUrl", "true").
                add("agreementDiscount", voucher.getDiscount().toString()).
                add("minimumAmount", "1").
                add("stepAmount", "1").
                add("brands", voucher.getBrands()).
                add("tags", voucher.getTags()).
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
}