
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "paymentMeansCode",
    "paymentChannelCode",
    "payerFinancialAccount",
    "payeeFinancialAccount",
    "paymentId",
    "instructionId",
    "creditAccount"
})
public class PaymentMeans {

    @JsonProperty("paymentMeansCode")
    private PaymentMeans.PaymentMeansCode paymentMeansCode;
    @JsonProperty("paymentChannelCode")
    private PaymentMeans.PaymentChannelCode paymentChannelCode;
    @JsonProperty("payerFinancialAccount")
    private PayerFinancialAccount payerFinancialAccount;
    @JsonProperty("payeeFinancialAccount")
    private PayeeFinancialAccount payeeFinancialAccount;
    /**
     * Is used to specify the form category on joint info transfer form payment information (Danish abbreviation FIK), and Giro payment forms
     * 
     */
    @JsonProperty("paymentId")
    private String paymentId;
    /**
     * Is used to specify an OCR reference from a payment form using the PaymentID 04, 15, 71 or 75. The number of digits depends on which PaymentID is being used.
     * 
     */
    @JsonProperty("instructionId")
    private String instructionId;
    @JsonProperty("creditAccount")
    private CreditAccount creditAccount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("paymentMeansCode")
    public PaymentMeans.PaymentMeansCode getPaymentMeansCode() {
        return paymentMeansCode;
    }

    @JsonProperty("paymentMeansCode")
    public void setPaymentMeansCode(PaymentMeans.PaymentMeansCode paymentMeansCode) {
        this.paymentMeansCode = paymentMeansCode;
    }

    @JsonProperty("paymentChannelCode")
    public PaymentMeans.PaymentChannelCode getPaymentChannelCode() {
        return paymentChannelCode;
    }

    @JsonProperty("paymentChannelCode")
    public void setPaymentChannelCode(PaymentMeans.PaymentChannelCode paymentChannelCode) {
        this.paymentChannelCode = paymentChannelCode;
    }

    @JsonProperty("payerFinancialAccount")
    public PayerFinancialAccount getPayerFinancialAccount() {
        return payerFinancialAccount;
    }

    @JsonProperty("payerFinancialAccount")
    public void setPayerFinancialAccount(PayerFinancialAccount payerFinancialAccount) {
        this.payerFinancialAccount = payerFinancialAccount;
    }

    @JsonProperty("payeeFinancialAccount")
    public PayeeFinancialAccount getPayeeFinancialAccount() {
        return payeeFinancialAccount;
    }

    @JsonProperty("payeeFinancialAccount")
    public void setPayeeFinancialAccount(PayeeFinancialAccount payeeFinancialAccount) {
        this.payeeFinancialAccount = payeeFinancialAccount;
    }

    /**
     * Is used to specify the form category on joint info transfer form payment information (Danish abbreviation FIK), and Giro payment forms
     * 
     */
    @JsonProperty("paymentId")
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * Is used to specify the form category on joint info transfer form payment information (Danish abbreviation FIK), and Giro payment forms
     * 
     */
    @JsonProperty("paymentId")
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Is used to specify an OCR reference from a payment form using the PaymentID 04, 15, 71 or 75. The number of digits depends on which PaymentID is being used.
     * 
     */
    @JsonProperty("instructionId")
    public String getInstructionId() {
        return instructionId;
    }

    /**
     * Is used to specify an OCR reference from a payment form using the PaymentID 04, 15, 71 or 75. The number of digits depends on which PaymentID is being used.
     * 
     */
    @JsonProperty("instructionId")
    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    @JsonProperty("creditAccount")
    public CreditAccount getCreditAccount() {
        return creditAccount;
    }

    @JsonProperty("creditAccount")
    public void setCreditAccount(CreditAccount creditAccount) {
        this.creditAccount = creditAccount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public enum PaymentChannelCode {

        DK_BANK("DK:BANK"),
        DK_FIK("DK:FIK");
        private final String value;
        private final static Map<String, PaymentMeans.PaymentChannelCode> CONSTANTS = new HashMap<String, PaymentMeans.PaymentChannelCode>();

        static {
            for (PaymentMeans.PaymentChannelCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private PaymentChannelCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static PaymentMeans.PaymentChannelCode fromValue(String value) {
            PaymentMeans.PaymentChannelCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum PaymentMeansCode {

        DOMESTIC_BANK_TRANSFER("DOMESTIC_BANK_TRANSFER"),
        FIK_PAYMENT("FIK_PAYMENT");
        private final String value;
        private final static Map<String, PaymentMeans.PaymentMeansCode> CONSTANTS = new HashMap<String, PaymentMeans.PaymentMeansCode>();

        static {
            for (PaymentMeans.PaymentMeansCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private PaymentMeansCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static PaymentMeans.PaymentMeansCode fromValue(String value) {
            PaymentMeans.PaymentMeansCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
