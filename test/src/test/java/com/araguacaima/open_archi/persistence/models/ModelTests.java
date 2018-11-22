package com.araguacaima.open_archi.persistence.models;

import com.araguacaima.commons.utils.JsonUtils;
import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.open_archi.controller.ModelsController;
import com.araguacaima.orpheusdb.utils.JPAEntityManagerUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.fail;

public class ModelTests {

    private static Map<String, String> environment;
    private static ProcessBuilder processBuilder = new ProcessBuilder();
    private static Logger log = LoggerFactory.getLogger(ModelTests.class);
    public static final JsonUtils jsonUtils = new JsonUtils();


    @Before
    public void init() {
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        environment = processBuilder.environment();
        URL url = JPAEntityManagerUtils.class.getResource("/config/config.properties");
        Properties properties = new Properties();
        try {
            properties.load(url.openStream());
            Map<String, String> map = MapUtils.fromProperties(properties);
            if (!map.isEmpty()) {
                environment.putAll(map);
                log.info("Properties taken from config file '" + url.getFile().replace("file:" + File.separator, "") + "'");
            } else {
                log.info("Properties taken from system map...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPAEntityManagerUtils.init(environment);
    }

    @Test
    public void testFindUsagesOf() {
        try {
            String modelName = "Firefox";
            Collection result = ModelsController.findUsagesOf(modelName);
            if (CollectionUtils.isNotEmpty(result)) {
                try {
                    log.debug(jsonUtils.toJSON(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.debug(jsonUtils.toJSON(result));
            } else {
                log.debug("Test done successfully due there is no model with name '" + modelName + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

}

