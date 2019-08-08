package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.entity.MongoPredicted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MongoDbApi{

    private static Logger logger = LogManager.getLogger(MongoDbApi.class);

    private MongoTemplate mongoTemplate;

    public MongoDbApi(MongoTemplate template) {
        this.mongoTemplate = template;
    }

    public List<MongoPredicted> getPredictedVoucherNameBy(String claimNumber, String claimLineDescription){

          Query query = new Query();
        query.addCriteria(Criteria.where("claimNumber").is(claimNumber));
        query.addCriteria(Criteria.where("request.claimLineDescription").is(claimLineDescription));
        //query.fields().include("predictedVoucher.voucherName");
        return mongoTemplate.find(query, MongoPredicted.class, "VoucherPrediction");
    }
}




