package com.scalepoint.automation.services.restService.helper;

import com.scalepoint.automation.services.restService.Common.Data;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.request.ClaimRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bza on 7/24/2017.
 */
public class PrepareSaveCustomerParams {

    private Map<String,String> saveCustomerParams = new HashMap<>();

    public Map<String, String> getSaveCustomerParams() {
        return saveCustomerParams;
    }

    public void setSaveCustomerParams(Map<String, String> saveCustomerParams) {
        this.saveCustomerParams = saveCustomerParams;
    }

    public PrepareSaveCustomerParams prepareSaveCustomerParams(Object object, Data data){
        getFilledSaveCustomerParams(data);
        if(object.getClass() == ClaimRequest.class) {
            setParamsBaseOnClaimRequest((ClaimRequest) object);
        }else if(object.getClass() == EccIntegration.class){
            setParamsBaseOnEccIntegration((EccIntegration) object);
        }
        return this;
    }

    private Map<String, String> getFilledSaveCustomerParams(Data data){
        saveCustomerParams.put("last_name", "");
        saveCustomerParams.put("first_name", "");
        saveCustomerParams.put("replacement", "");
        saveCustomerParams.put("validate_zipcode", "");
        saveCustomerParams.put("send_settlement_preview", "");
        saveCustomerParams.put("customer_id", data.getUserId().toString());
        saveCustomerParams.put("sendSMSPassword", "false");
        saveCustomerParams.put("smsPassword", "");
        saveCustomerParams.put("checkForCancelProcura", "");
        saveCustomerParams.put("isNewProcuraRequested", "");
        saveCustomerParams.put("shopper_id", "");
        saveCustomerParams.put("email", "ecc_auto@scalepoint.com");
        saveCustomerParams.put("changepassword", "1");
        saveCustomerParams.put("password", Constants.DEFAULT_PASSWORD);
        saveCustomerParams.put("customer_note", "");
        saveCustomerParams.put("caseid", data.getUserId().toString());

        return saveCustomerParams;
    }

    private void setParamsBaseOnClaimRequest(ClaimRequest claimRequest){
        saveCustomerParams.put("policy_number", claimRequest.getCaseNumber());
        saveCustomerParams.put("cellPhoneNumber", claimRequest.getCustomer().getMobile());
        saveCustomerParams.put("phone", claimRequest.getCustomer().getMobile());
        saveCustomerParams.put("fname", claimRequest.getCustomer().getFirstName());
        saveCustomerParams.put("lname", claimRequest.getCustomer().getLastName());
        saveCustomerParams.put("adr1", claimRequest.getCustomer().getAddress().getStreet1());
        saveCustomerParams.put("adr2", "");
        saveCustomerParams.put("zipcode", claimRequest.getCustomer().getAddress().getPostalCode());
        saveCustomerParams.put("city", claimRequest.getCustomer().getAddress().getCity());
        saveCustomerParams.put("claim_number", claimRequest.getCaseNumber());
    }

    private void setParamsBaseOnEccIntegration(EccIntegration eccIntegration){
        saveCustomerParams.put("policy_number", eccIntegration.getClaim().getClaimNumber());
        saveCustomerParams.put("cellPhoneNumber", eccIntegration.getClaimant().getPhone());
        saveCustomerParams.put("phone", eccIntegration.getClaimant().getPhone());
        saveCustomerParams.put("fname", eccIntegration.getClaimant().getFirstName());
        saveCustomerParams.put("lname", eccIntegration.getClaimant().getLastName());
        saveCustomerParams.put("adr1", eccIntegration.getClaimant().getAddress1());
        saveCustomerParams.put("adr2", "");
        saveCustomerParams.put("zipcode", eccIntegration.getClaimant().getPostalCode());
        saveCustomerParams.put("city", eccIntegration.getClaimant().getCity());
    }
}
