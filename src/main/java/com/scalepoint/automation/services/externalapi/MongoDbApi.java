package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.entity.VoucherPredictionObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoDbApi{

    private static Logger logger = LogManager.getLogger(MongoDbApi.class);

    private MongoTemplate mongoTemplate;

    public MongoDbApi(MongoTemplate template) {
        this.mongoTemplate = template;
    }

    public List<VoucherPredictionObject> getVoucherPredictedObjectsBy(String claimNumber, String claimLineDescription){
        logger.info("querying Mongo db");
        Query query = new Query();
        query.addCriteria(where("claimNumber").is(claimNumber));
        query.addCriteria(where("request.claimLineDescription").is(claimLineDescription));
        return mongoTemplate.find(query, VoucherPredictionObject.class, "VoucherPrediction");
    }
}




