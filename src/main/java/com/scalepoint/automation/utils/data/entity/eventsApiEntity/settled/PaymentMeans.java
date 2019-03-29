
package com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled;

import com.fasterxml.jackson.annotation.*;
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
        "creditAccount",
        "financialAccount"
})
public class PaymentMeans {

    @JsonProperty("paymentMeansCode")
    private PaymentMeans.PaymentMeansCode paymentMeansCode;
    @JsonProperty("paymentChannelCode")
    private PaymentMeans.PaymentChannelCode paymentChannelCode;
    @JsonProperty("payerFinancialAccount")
    private FinancialAccount payerFinancialAccount;
    @JsonProperty("payeeFinancialAccount")
    private FinancialAccount payeeFinancialAccount;
    /**
     * Is used to specify the form category on joint info transfer form payment information (Danish abbreviation FIK), and Giro payment forms
     */
    @JsonProperty("paymentId")
    private String paymentId;
    /**
     * Is used to specify an OCR reference from a payment form using the PaymentID 04, 15, 71 or 75. The number of digits depends on which PaymentID is being used.
     */
    @JsonProperty("instructionId")
    private String instructionId;
    @JsonProperty("creditAccount")
    private CreditAccount creditAccount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public PaymentMeans.PaymentMeansCode getPaymentMeansCode() {
        return paymentMeansCode;
    }

    public void setPaymentMeansCode(PaymentMeans.PaymentMeansCode paymentMeansCode) {
        this.paymentMeansCode = paymentMeansCode;
    }

    public PaymentMeans.PaymentChannelCode getPaymentChannelCode() {
        return paymentChannelCode;
    }

    public void setPaymentChannelCode(PaymentMeans.PaymentChannelCode paymentChannelCode) {
        this.paymentChannelCode = paymentChannelCode;
    }

    public FinancialAccount getPayerFinancialAccount() {
        return payerFinancialAccount;
    }

    public void setPayerFinancialAccount(FinancialAccount payerFinancialAccount) {
        this.payerFinancialAccount = payerFinancialAccount;
    }

    public FinancialAccount getPayeeFinancialAccount() {
        return payeeFinancialAccount;
    }

    public void setPayeeFinancialAccount(FinancialAccount payeeFinancialAccount) {
        this.payeeFinancialAccount = payeeFinancialAccount;
    }

    /**
     * Is used to specify the form category on joint info transfer form payment information (Danish abbreviation FIK), and Giro payment forms
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * Is used to specify the form category on joint info transfer form payment information (Danish abbreviation FIK), and Giro payment forms
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Is used to specify an OCR reference from a payment form using the PaymentID 04, 15, 71 or 75. The number of digits depends on which PaymentID is being used.
     */
    public String getInstructionId() {
        return instructionId;
    }

    /**
     * Is used to specify an OCR reference from a payment form using the PaymentID 04, 15, 71 or 75. The number of digits depends on which PaymentID is being used.
     */
    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public CreditAccount getCreditAccount() {
        return creditAccount;
    }

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
        DK_FIK("DK:FIK"),
        IBAN("IBAN"),
        DK_NEMKONTO("DK:NEMKONTO"),
        SE_GIRO("SE:GIRO"),
        NO_BANK("NO:BANK");

        private final String value;
        private final static Map<String, PaymentMeans.PaymentChannelCode> CONSTANTS = new HashMap<String, PaymentMeans.PaymentChannelCode>();

        static {
            for (PaymentMeans.PaymentChannelCode c : values()) {
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
        FIK_PAYMENT("FIK_PAYMENT"),
        INTERNATIONAL_BANKA_TRANSFER("INTERNATIONAL_BANK_TRANSFER"),
        NEMKONTO_BANKTRANSFER("NEMKONTO_BANK_TRANSFER"),
        GIRO_TRANSFER("GIRO_TRANSFER");

        private final String value;
        private final static Map<String, PaymentMeans.PaymentMeansCode> CONSTANTS = new HashMap<String, PaymentMeans.PaymentMeansCode>();

        static {
            for (PaymentMeans.PaymentMeansCode c : values()) {
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
