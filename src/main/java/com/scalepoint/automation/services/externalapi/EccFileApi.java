package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EccFileApi extends AuthenticationApi {

    public EccFileApi(User user) {
        super(user);
    }

    public void downloadFile(String href, String localPath) {
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            HttpEntity entity = executor.execute(Request.Get(href)).returnResponse().getEntity();
            in = entity.getContent();
            fos = new FileOutputStream(new File(localPath));
            IOUtils.copy(in, fos);
            log.info("File was downloaded to: " + localPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(fos);
        }
    }
}
