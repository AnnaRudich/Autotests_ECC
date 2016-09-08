package com.scalepoint.automation.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Http {

    private static Logger logger = Logger.getLogger(Http.class);

    public static int CONNECTION_TIMEOUT = 90000;
    public static int SOCKET_TIMEOUT = 90000;

    public static Response get(String url, Executor executor) throws IOException {
        logger.info("Get to: " + url);
        return executor.execute(
                Request.Get(url)
                        .useExpectContinue()
                        .connectTimeout(CONNECTION_TIMEOUT)
                        .socketTimeout(SOCKET_TIMEOUT));
    }

    public static Response post(String url, List<NameValuePair> namedParams, Executor executor) throws IOException {
        logger.info("Post to: " + url);
        return executor.execute(
                Request.Post(url)
                        .useExpectContinue()
                        .connectTimeout(CONNECTION_TIMEOUT)
                        .socketTimeout(SOCKET_TIMEOUT)
                        .bodyForm(namedParams));
    }

    public static Response post(String url, Executor executor) throws IOException {
        return post(url, Collections.emptyList(), executor);
    }

    public static void ensure200Code(int responseCode) throws HttpResponseException {
        if (responseCode != 200) {
            throw new HttpResponseException(responseCode, String.format("Unexpected http code [%d], [%d] expected", responseCode, 200));
        }
    }

    public static void ensure302Code(int responseCode) throws HttpResponseException {
        if (responseCode != 302) {
            throw new HttpResponseException(responseCode, String.format("Unexpected http code [%d], [%d] expected", responseCode, 302));
        }
    }

    public static class ParamsBuilder {

        private List<NameValuePair> params = new ArrayList<>();

        public static ParamsBuilder create() {
            return new ParamsBuilder();
        }

        public ParamsBuilder add(String name, String value) {
            params.add(new BasicNameValuePair(name, value));
            return this;
        }

        public ParamsBuilder add(BasicNameValuePair pair) {
            params.add(pair);
            return this;
        }

        public List<NameValuePair> get() {
            return params;
        }
    }

}
