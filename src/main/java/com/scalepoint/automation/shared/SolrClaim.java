package com.scalepoint.automation.shared;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.solr.client.solrj.beans.Field;

@Data
@NoArgsConstructor
public class SolrClaim {

    @Field("id")
    private long id;

    @Field("email")
    private String email;

    @Field("claim_status")
    private String claimStatus;

    @Field("claimNumber")
    private String claimNumber;

    @Field("ic_name")
    private String icName;

    @Field("address")
    private String address;

    @Field("claim_name")
    private String claimName;
}
