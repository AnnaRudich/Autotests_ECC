package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FTemplatesManager {

    private Logger logger = LoggerFactory.getLogger(FTemplatesManager.class);

    private Document document;

    public FTemplatesManager(Document document) {
        this.document = document;
    }

    public boolean hasSameValues(List<FtOperation> operations) {
        boolean valuesAreSame = true;

        for (FtOperation operation : operations) {
            boolean hasSameState = operation.hasSameState(document);
            if (!hasSameState) {
                logger.info("FT should be updated because of: {}", operation.toString());
            }
            valuesAreSame &= hasSameState;
        }
        return valuesAreSame;
    }
}
