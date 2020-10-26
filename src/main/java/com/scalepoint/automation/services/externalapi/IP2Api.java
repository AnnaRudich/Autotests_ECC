package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.services.restService.common.BaseService;

import static com.scalepoint.automation.services.externalapi.IP2Api.TransactionStatus.getTransactionStatusByCode;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class IP2Api extends BaseService {

    private Integer getTransactionStatusBy(Integer claimNumber){
        return getData().getDatabaseApi().getExternalIntegrationTransactionStatusCodeBy(claimNumber);
    }

    private Boolean ifTransactionIsSuccess(Integer claimNumber){
        Integer transactionStatusCode = getTransactionStatusBy(claimNumber);
        TransactionStatus transactionStatus = getTransactionStatusByCode(transactionStatusCode);


       if (transactionStatus != null) {
           switch (transactionStatus) {
               case INTEGRATION_PROCESSED_WITH_ERRORS:
                   return false;
               case INTEGRATION_UNPROCESSED:
                   return false;
               case INTEGRATION_PROCESSED_SUCCESFULLY:
                   return true;
           }
       }
        return false;
    }

    public void assertTransactionWasProcessed(Integer claimNumber){
        assertThat(ifTransactionIsSuccess(claimNumber))
                .as("ip2 transaction for claim with number: " + claimNumber + "was not successful")
                .isTrue();
    }

    enum TransactionStatus{
        INTEGRATION_PROCESSED_WITH_ERRORS(-1),
        INTEGRATION_UNPROCESSED(0),
        INTEGRATION_PROCESSED_SUCCESFULLY(1);

        int code;

        TransactionStatus(int code) {
            this.code=code;
        }

        public int getCode() {
            return code;
        }

        public static TransactionStatus getTransactionStatusByCode(int code){
            for(TransactionStatus e : TransactionStatus.values()){
                if(code == e.code)
                    return e;
            }
            return null;
        }
    }
}
