package com.scalepoint.automation.utils.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.Assignment;
import com.scalepoint.automation.utils.data.entity.AttachmentFiles;
import com.scalepoint.automation.utils.data.entity.Category;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.ClaimLineGroup;
import com.scalepoint.automation.utils.data.entity.DepreciationType;
import com.scalepoint.automation.utils.data.entity.DiscretionaryReason;
import com.scalepoint.automation.utils.data.entity.Errors;
import com.scalepoint.automation.utils.data.entity.ExistingSuppliers;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.Integration;
import com.scalepoint.automation.utils.data.entity.Links;
import com.scalepoint.automation.utils.data.entity.Mail;
import com.scalepoint.automation.utils.data.entity.Notifications;
import com.scalepoint.automation.utils.data.entity.OrderDetails;
import com.scalepoint.automation.utils.data.entity.PasswordsVerification;
import com.scalepoint.automation.utils.data.entity.PriceRule;
import com.scalepoint.automation.utils.data.entity.RRLinesFields;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import com.scalepoint.automation.utils.data.entity.Roles;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Shop;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.SystemUser;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.ExistingUsers;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.entity.payments.Payments;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


@SuppressWarnings("unchecked")
public class TestData {

    private static Logger log = LoggerFactory.getLogger(TestData.class);

    public static ExistingSuppliers getSuppliers() {
        return (ExistingSuppliers) getData(Data.EXISTING_SUPPLIER);
    }

    public static Supplier getSupplier() {
        return (Supplier) getData(Data.SUPPLIER);
    }

    public static Links getLinks() {
        return (Links) getData(Data.LINKS);
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

    public static Roles getRoles() {
        return (Roles) getData(Data.ROLES);
    }

    public static ClaimItem getClaimItem() {
        return (ClaimItem) getData(Data.CLAIMITEM);
    }

    public static TextSearch getTextSearch() {
        return (TextSearch) getData(Data.TEXTSEARCH);
    }

    public static Mail getMails() {
        return (Mail) getData(Data.MAILS);
    }

    public static Errors getErrors() {
        return (Errors) getData(Data.ERRORS);
    }

    public static Notifications getNotifications() {
        return (Notifications) getData(Data.NOTIFICATIONS);
    }

    public static GenericItem getGenericItem() {
        return (GenericItem) getData(Data.GENERICITEM);
    }

    public static Integration getIntegration() {
        return (Integration) getData(Data.INTEGRATION);
    }

    public static DepreciationType getDepreciationType() {
        return (DepreciationType) getData(Data.DEPRECIATIONTYPE);
    }

    public static RRLinesFields getRRLinesFields() {
        return (RRLinesFields) getData(Data.RRLINEFIELDS);
    }

    public static ServiceAgreement getServiceAgreement() {
        return (ServiceAgreement) getData(Data.SERVAGREEMENT);
    }

    public static OrderDetails getOrderDetails() {
        return (OrderDetails) getData(Data.ORDERDETAILS);
    }

    public static PriceRule getPriceRule() {
        return (PriceRule) getData(Data.PRICERULE);
    }

    public static ClaimLineGroup getClaimLineGroup() {
        return (ClaimLineGroup) getData(Data.CLGROUP);
    }

    public static AttachmentFiles getAttachmentFiles() {
        return (AttachmentFiles) getData(Data.ATTFILES);
    }

    public static Payments getPayments() {
        return (Payments) getData(Data.PAYMENTS);
    }

    public static DiscretionaryReason getDiscretionaryReason() {
        return (DiscretionaryReason) getData(Data.DISCRETIONARYREASON);
    }

    public static ClaimRequest getClaimRequest(){
        return (ClaimRequest) getData(Data.CWA_CLAIM);
    }

    public static Assignment getAssignment(){ return (Assignment) getData(Data.ASSIGNMENT); }

    public static InsertSettlementItem getInsertSettlementItem() {
        return (InsertSettlementItem) getData(Data.CLAIM_ITEM);
    }

    public static EccIntegration getEccIntegration(){
        return  (EccIntegration) getData(Data.ECC_INTEGRATION);
    }

    private static <T> T getData(Data data) {
        String locale = Configuration.getLocale().getValue();
        String filePath = buildDataFilePath(locale, data.fileName);
        Object resultObject;
        InputStream inputStream = TestData.class.getClassLoader().getResourceAsStream(filePath);
        try {
            if(data.fileName.endsWith(".xml")) {
                resultObject = data.context.createUnmarshaller().unmarshal(inputStream);
            }
            else if(data.fileName.endsWith(".json")){
                resultObject = new ObjectMapper().readValue(inputStream, data.dataClass);
            }else{
                throw new IOException("File should be xml or json, file is not valid " + filePath);
            }
            preprocess(resultObject, buildParams());
            return (T) resultObject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String objectAsXml(Object object) {
        java.io.StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(object, sw);
        }catch (JAXBException exception){
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
            e.printStackTrace();
        }
    }

    private static String buildDataFilePath(String locale, String fileName) {
        return "data" + File.separator + locale + File.separator + fileName;
    }

    public static PasswordsVerification getPasswordRules() {
        return (PasswordsVerification) getData(Data.PASSWORDVERIFICATION);
    }

    public enum Data {
        EXISTING_SUPPLIER("ExistingSuppliers.xml", ExistingSuppliers.class),
        SUPPLIER("Supplier.xml", Supplier.class),
        LINKS("Links.xml", Links.class),
        SHOP("Shop.xml", Shop.class),
        VOUCHER("Voucher.xml", Voucher.class),
        SYSTEMCREDENTIALS("ExistingUsers.xml", ExistingUsers.class),
        NEWSYSTEMUSER("SystemUser.xml", SystemUser.class),
        CATEGORIES("Category.xml", Category.class),
        INSURANCECOMPANY("InsuranceCompany.xml", InsuranceCompany.class),
        REDUCTIONRULE("ReductionRule.xml", ReductionRule.class),
        ROLES("Roles.xml", Roles.class),
        CLAIM("Claim.xml", Claim.class),
        CLAIMITEM("ClaimItem.xml", ClaimItem.class),
        TEXTSEARCH("TextSearch.xml", TextSearch.class),
        MAILS("Mail.xml", Mail.class),
        ERRORS("Errors.xml", Errors.class),
        NOTIFICATIONS("Notifications.xml", Notifications.class),
        GENERICITEM("GenericItem.xml", GenericItem.class),
        INTEGRATION("Integration.xml", Integration.class),
        PASSWORDVERIFICATION("PasswordsVerification.xml", PasswordsVerification.class),
        DEPRECIATIONTYPE("DepreciationType.xml", DepreciationType.class),
        RRLINEFIELDS("RRLinesFields.xml", RRLinesFields.class),
        SERVAGREEMENT("ServiceAgreement.xml", ServiceAgreement.class),
        ORDERDETAILS("OrderDetails.xml", OrderDetails.class),
        PRICERULE("PriceRule.xml", PriceRule.class),
        CLGROUP("ClaimLineGroup.xml", ClaimLineGroup.class),
        PAYMENTS("Payments.xml", Payments.class),
        ATTFILES("AttachmentFiles.xml", AttachmentFiles.class),
        DISCRETIONARYREASON("DiscretionaryReason.xml", DiscretionaryReason.class),
        CWA_CLAIM("Claim\\ClaimRequest.json", ClaimRequest.class),
        ASSIGNMENT("Assignment.xml", Assignment.class),
        CLAIM_ITEM("Claim\\ClaimItem.xml",InsertSettlementItem.class),
        ECC_INTEGRATION("Claim\\EccIntegration.xml",EccIntegration.class);

        private String fileName;
        private JAXBContext context;
        private Class dataClass;

        Data(String filePath, Class dataClass) {
            this.fileName = filePath;
            try {
                if(fileName.endsWith(".xml")) {
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

