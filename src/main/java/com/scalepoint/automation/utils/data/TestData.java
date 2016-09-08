package com.scalepoint.automation.utils.data;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.*;
import com.scalepoint.automation.utils.data.entity.credentials.SystemUsers;
import com.scalepoint.automation.utils.data.entity.payments.Payments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class TestData {

    private static Logger log = LoggerFactory.getLogger(TestData.class);

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

    public static SystemUsers getSystemUsers() {
        return (SystemUsers) getData(Data.SYSTEMCREDENTIALS);
    }

    public static NewSystemUser getUser() {
        return (NewSystemUser) getData(Data.NEWSYSTEMUSER);
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

    private static <T> T getData(Data data) {
        String locale = Configuration.getLocale();
        String xmlPath = buildXmlFilePath(locale, data.fileName);
        try {
            Unmarshaller u = data.context.createUnmarshaller();
            InputStream resourceAsStream = TestData.class.getClassLoader()
                    .getResourceAsStream(xmlPath);
            log.info(xmlPath);
            log.info(resourceAsStream.available()+"");
            Object resultObject = (u.unmarshal(resourceAsStream));
            preprocess(resultObject, buildParams());
            return (T) resultObject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
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

    private static String buildXmlFilePath(String locale, String fileName) {
        return "data" + File.separator + locale + File.separator + fileName;
    }

    public static PasswordsVerification getPasswordRules() {
        return (PasswordsVerification) getData(Data.PASSWORDVERIFICATION);
    }

    public static enum Data {
        SUPPLIER("Supplier.xml", Supplier.class),
        LINKS("Links.xml", Links.class),
        SHOP("Shop.xml", Shop.class),
        VOUCHER("Voucher.xml", Voucher.class),
        SYSTEMCREDENTIALS("SystemUsers.xml", SystemUsers.class),
        NEWSYSTEMUSER("NewSystemUser.xml", NewSystemUser.class),
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
        ATTFILES("AttachmentFiles.xml", AttachmentFiles.class);

        private String fileName;
        private JAXBContext context;
        private Class dataClass;

        Data(String xmlPath, Class dataClass) {
            this.fileName = xmlPath;
            try {
                this.context = JAXBContext.newInstance(dataClass);
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

