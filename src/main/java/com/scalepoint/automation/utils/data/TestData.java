package com.scalepoint.automation.utils.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.*;
import com.scalepoint.automation.utils.data.entity.payments.Payments;
import com.scalepoint.automation.utils.data.request.*;
import com.scalepoint.automation.utils.listeners.DefaultFTOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings("unchecked")
public class TestData {

    private static Logger log = LogManager.getLogger(TestData.class);

    public static ExistingSuppliers getSuppliers() {
        return (ExistingSuppliers) getData(Data.EXISTING_SUPPLIER);
    }

    public static Supplier getSupplier() {
        return (Supplier) getData(Data.SUPPLIER);
    }

    public static Shop getShop() {
        return (Shop) getData(Data.SHOP);
    }

    public static Voucher getVoucher() {
        return (Voucher) getData(Data.VOUCHER);
    }

    public static ExistingUsers getSystemUsers() {
        return (ExistingUsers) getData(Data.SYSTEMCREDENTIALS);
    }

    public static SystemUser getSystemUser() {
        return (SystemUser) getData(Data.NEWSYSTEMUSER);
    }

    public static Category getCategories() {
        return (Category) getData(Data.CATEGORIES);
    }

    public static InsuranceCompany getInsuranceCompany() {
        return (InsuranceCompany) getData(Data.INSURANCECOMPANY);
    }

    public static ReductionRule getReductionRule() {
        return (ReductionRule) getData(Data.REDUCTIONRULE);
    }

    public static Claim getClaim() {
        return (Claim) getData(Data.CLAIM);
    }

    public static ClaimItem getClaimItem() {
        return (ClaimItem) getData(Data.CLAIMITEM);
    }

    public static UserPasswordRules getUserPasswordRules() {
        return (UserPasswordRules) getData(Data.USER_PASSWORD_RULES);
    }

    public static CommunicationDesignerEmailTemplates getCommunicationDesignerEmialTemplates() {
        return (CommunicationDesignerEmailTemplates) getData(Data.COMMUNICATION_DESIGNER_EMAIL_TEMPLATES);
    }

    public static GenericItem getGenericItem() {
        return (GenericItem) getData(Data.GENERICITEM);
    }

    public static ServiceAgreement getServiceAgreement() {
        return (ServiceAgreement) getData(Data.SERVAGREEMENT);
    }

    public static ClaimLineGroup getClaimLineGroup() {
        return (ClaimLineGroup) getData(Data.CLGROUP);
    }

    public static AttachmentFiles getAttachmentFiles() {
        return (AttachmentFiles) getData(Data.ATTFILES);
    }

    public static BankAccount getBankAccount() {
        return (BankAccount) getData(Data.BANK_ACCOUNT);
    }

    public static Payments getPayments() {
        return (Payments) getData(Data.PAYMENTS);
    }

    public static SelfServiceRequest getSelfServiceRequest() {
        return (SelfServiceRequest) getData(Data.SELF_SERVICE_REQUEST);
    }

    public static SelfServiceLossItems getSelfServiceLossItems() {
        return (SelfServiceLossItems) getData(Data.SELF_SERVICE_LOSS_ITEMS);
    }

    public static Translations getTranslations() {
        return (Translations) getData(Data.TRANSLATIONS);
    }

    public static ClaimRequest getClaimRequest() {
        return (ClaimRequest) getData(Data.CWA_CLAIM);
    }

    public static ClaimRequest getClaimRequestFraudAlert() {
        return (ClaimRequest) getData(Data.FRAUD_ALERT_CLAIM);
    }

    public static ClaimRequest getClaimRequestItemizationCaseTopdanmarkFNOL() {
        ClaimRequest claimRequest = getData(Data.TOPDANMARK_FNOL_ITEMIZATION_CASE);
        claimRequest.setCaseNumber(UUID.randomUUID().toString());
        Policy policy = new Policy();
        policy.setNumber("dk".concat(Integer.toString(RandomUtils.randomInt())));
        claimRequest.setPolicy(policy);
        claimRequest.setExternalReference(UUID.randomUUID().toString());

        return claimRequest;
    }

    public static ClaimRequest getClaimRequestCreateClaimTopdanmarkFNOL() {
        ClaimRequest claimRequest = getData(Data.TOPDANMARK_FNOL_CREATE_CLAIM);
        claimRequest.setCaseNumber(UUID.randomUUID().toString());
        Policy policy = new Policy();
        policy.setNumber("dk".concat(Integer.toString(RandomUtils.randomInt())));
        claimRequest.setPolicy(policy);
        claimRequest.setExternalReference(UUID.randomUUID().toString());

        return claimRequest;
    }

    public static Assignment getAssignment() {
        return (Assignment) getData(Data.ASSIGNMENT);
    }

    public static InsertSettlementItem getInsertSettlementItem() {
        return getData(Data.CLAIM_ITEM);
    }

    public static InsertSettlementItem getPerformanceInsertSettlementItem() {
        return getData(Data.PERFORMANCE_INSERT_SETTLEMENT_ITEM);
    }

    public static InsertSettlementItem getRnvInsertSettlementItem() {
        return getData(Data.RNV_INSERT_SETTLEMENT_ITEM);
    }

    public static UpdateSettlementItem getUpdateSettlementItem() {
        return (UpdateSettlementItem) getData(Data.UPDATED_CLAIM_ITEM);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForFuture(){
        return getData(Data.FUTURE_DEFAULT_FT_SETTINGS);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForAlka(){
        return getData(Data.ALKA_DEFAULT_FT_SETTINGS);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForTopdanmark(){
        return getData(Data.TOPDANMARK_DEFAULT_FT_SETTINGS);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForScalepoint(){
        return getData(Data.SCALEPOINT_DEFAULT_FT_SETTINGS);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForTrygforsikring(){
        return getData(Data.TRYGFORSIKRING_DEFAULT_FT_SETTINGS);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForBauta(){
        return getData(Data.BAUTA_DEFAULT_FT_SETTINGS);
    }

    public static DefaultFTOperations.DefaultFTSettings getDefaultFTSettingsForTrygholding(){
        return getData(Data.TRYGHOLDING_DEFAULT_FT_SETTINGS);
    }

    public static EccIntegration getEccIntegration() {
        return (EccIntegration) getData(Data.ECC_INTEGRATION);
    }

    @SuppressWarnings("ConstantConditions")
    public static Map<ClaimStatus, String> getClaimStatuses() {
        Map<ClaimStatus, String> statusPerText = new HashMap<>();
        ((ClaimStatuses) getData(Data.CLAIM_STATUS)).getClaimStatuses().forEach(state -> statusPerText.put(ClaimStatus.findByStatus(state.getStatus()), state.getName()));
        return statusPerText;
    }

    private static <T> T getData(Data data) {
        String locale = Configuration.getLocale().getValue();
        Object resultObject;
        InputStream inputStream = getInputStreamFromResources(locale, data.fileName);
        try {
            if (data.fileName.endsWith(".xml")) {
                resultObject = data.context
                        .createUnmarshaller()
                        .unmarshal(inputStream);
            } else if (data.fileName.endsWith(".json")) {
                resultObject = new ObjectMapper().readValue(inputStream, data.dataClass);
            } else {
                throw new IOException("File should be xml or json, file is not valid " + data.fileName);
            }
            preprocess(resultObject, buildParams());
            return (T) resultObject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static InputStream getInputStreamFromResources(String locale, String fileName){

        String filePath = buildDataFilePath(locale, fileName);
        return TestData.class.getClassLoader().getResourceAsStream(filePath);
    }

    public static String objectAsXml(Object object) {
        java.io.StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(object, sw);
        } catch (JAXBException exception) {
            log.error(exception.getMessage());
        }
        return sw.toString();
    }

    private static Map<String, String> buildParams() {
        Map<String, String> params = new HashMap<>();
        params.put("{ecc}", Configuration.getEccUrl());
        params.put("{eccadmin}", Configuration.getEccAdminUrl());
        return params;
    }

    private static void preprocess(Object object, Map<String, String> params) {
        try {
            Class<?> aClass = object.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(object);
                if (o != null && o.getClass().equals(String.class)) {
                    String value = o.toString();
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        value = value.replace(param.getKey(), param.getValue());
                    }
                    field.set(object, value);
                }
            }
        } catch (IllegalAccessException e) {
            log.info(e.getMessage());
        }
    }

    private static String buildDataFilePath(String locale, String fileName) {
        return "data" + File.separator + locale + File.separator + fileName;
    }

    public enum Data {
        EXISTING_SUPPLIER("input/ExistingSuppliers.xml", ExistingSuppliers.class),
        SUPPLIER("input/Supplier.xml", Supplier.class),
        SHOP("input/Shop.xml", Shop.class),
        VOUCHER("input/Voucher.xml", Voucher.class),
        COMMUNICATION_DESIGNER_EMAIL_TEMPLATES("input/CommunicationDesignerEmailTemplates.xml", CommunicationDesignerEmailTemplates.class),
        SYSTEMCREDENTIALS("input/ExistingUsers.xml", ExistingUsers.class),
        NEWSYSTEMUSER("input/SystemUser.xml", SystemUser.class),
        CATEGORIES("input/Category.xml", Category.class),
        INSURANCECOMPANY("input/InsuranceCompany.xml", InsuranceCompany.class),
        REDUCTIONRULE("input/ReductionRule.xml", ReductionRule.class),
        CLAIM("input/Claim.xml", Claim.class),
        CLAIMITEM("input/ClaimItem.xml", ClaimItem.class),
        GENERICITEM("input/GenericItem.xml", GenericItem.class),
        SERVAGREEMENT("input/ServiceAgreement.xml", ServiceAgreement.class),
        CLGROUP("input/ClaimLineGroup.xml", ClaimLineGroup.class),
        PAYMENTS("input/Payments.xml", Payments.class),
        ATTFILES("input/AttachmentFiles.xml", AttachmentFiles.class),
        BANK_ACCOUNT("input/BankAccount.xml", BankAccount.class),
        CWA_CLAIM("request/Claim/ClaimRequest.json", ClaimRequest.class),
        FRAUD_ALERT_CLAIM("request/Claim/fraudAlert/ClaimRequestFraudAlert.json", ClaimRequest.class),
        TOPDANMARK_FNOL_ITEMIZATION_CASE("request/Claim/fraudAlert/ClaimRequestTopadanmarkFNOL.json", ClaimRequest.class),
        TOPDANMARK_FNOL_CREATE_CLAIM("request/Claim/fraudAlert/CreateClaimWithFNOLTopdanmark.json", ClaimRequest.class),
        ASSIGNMENT("input/Assignment.xml", Assignment.class),
        CLAIM_ITEM("request/Claim/ClaimItem.xml", InsertSettlementItem.class),
        UPDATED_CLAIM_ITEM("request/Claim/UpdatedClaimItem.xml", UpdateSettlementItem.class),
        ECC_INTEGRATION("request/Claim/EccIntegration.xml", EccIntegration.class),
        CLAIM_STATUS("ClaimStatuses.json", ClaimStatuses.class),
        TRANSLATIONS("input/Translations.xml", Translations.class),
        SELF_SERVICE_REQUEST("request/selfService/SelfServiceRequest.json", SelfServiceRequest.class),
        SELF_SERVICE_LOSS_ITEMS("request/selfService/SelfServiceLossItems.json", SelfServiceLossItems.class),
        FUTURE_DEFAULT_FT_SETTINGS("DefaultFTSettings/Future.xml", DefaultFTOperations.DefaultFTSettings.class),
        ALKA_DEFAULT_FT_SETTINGS("DefaultFTSettings/Alka.xml", DefaultFTOperations.DefaultFTSettings.class),
        TOPDANMARK_DEFAULT_FT_SETTINGS("DefaultFTSettings/Topdanmark.xml", DefaultFTOperations.DefaultFTSettings.class),
        SCALEPOINT_DEFAULT_FT_SETTINGS("DefaultFTSettings/Scalepoint.xml", DefaultFTOperations.DefaultFTSettings.class),
        TRYGFORSIKRING_DEFAULT_FT_SETTINGS("DefaultFTSettings/Trygforsikring.xml", DefaultFTOperations.DefaultFTSettings.class),
        BAUTA_DEFAULT_FT_SETTINGS("DefaultFTSettings/Bauta.xml", DefaultFTOperations.DefaultFTSettings.class),
        TRYGHOLDING_DEFAULT_FT_SETTINGS("DefaultFTSettings/Trygholding.xml", DefaultFTOperations.DefaultFTSettings.class),
        PERFORMANCE_INSERT_SETTLEMENT_ITEM("request/Claim/PerformanceInsertSettlementItem.xml", InsertSettlementItem.class),
        RNV_INSERT_SETTLEMENT_ITEM("request/Claim/RnvInsertSettlementItem.xml", InsertSettlementItem.class),
        USER_PASSWORD_RULES("input/UserPasswordRules.xml", UserPasswordRules.class);

        private String fileName;
        private JAXBContext context;
        private Class dataClass;

        Data(String filePath, Class dataClass) {
            this.fileName = filePath;
            try {
                if (fileName.endsWith(".xml")) {
                    this.context = JAXBContext.newInstance(dataClass);
                }
                this.dataClass = dataClass;
            } catch (JAXBException e) {
                log.error(e.getMessage(), e);
            }
        }

        public static Object getInstance(Class dataClass) {
            for (Data data : Data.values()) {
                if (data.dataClass.equals(dataClass)) {
                    return getData(data);
                }
            }
            throw new IllegalArgumentException("Data is not found for class: " + dataClass);
        }
    }
}

