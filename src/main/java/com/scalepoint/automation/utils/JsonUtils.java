package com.scalepoint.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    public static String getJSONasString(String path) throws IOException {
        InputStream classLoader = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(path);

        return IOUtils.toString(classLoader);
    }

    public static JsonNode getJSONfromResources(String resource) throws IOException {
        return new ObjectMapper().readTree(new File(SystemUtils.getResourcePath(resource)));
    }

    public static JsonNode stringToJsonNode(String string){
        try {
            return new ObjectMapper().readTree(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
