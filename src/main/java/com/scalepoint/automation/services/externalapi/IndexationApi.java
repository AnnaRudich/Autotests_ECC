package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.client.fluent.Executor;

import java.io.IOException;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static com.scalepoint.automation.utils.Http.ensure302Code;
import static com.scalepoint.automation.utils.Http.post;

public class IndexationApi extends ServerApi {

    public static String URL_REINDEX_CLAIMS = getEccUrl() + "ClaimReindex";

    public IndexationApi(User user) {
        super(user);
    }

    public IndexationApi(Executor executor) {
        super(executor);
    }

    public void indexClaims() {
        try {
            ensure302Code(post(URL_REINDEX_CLAIMS, executor).returnResponse().getStatusLine().getStatusCode());
            log.info("Reindexation claims was performed");
        } catch (IOException e) {
            log.error("Can't start reindexation", e);
            throw new ServerApiException(e);
        }
    }

}
