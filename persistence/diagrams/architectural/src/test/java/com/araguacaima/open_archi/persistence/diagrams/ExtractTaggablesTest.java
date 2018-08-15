package com.araguacaima.open_archi.persistence.diagrams;

import com.araguacaima.commons.utils.JsonUtils;
import com.araguacaima.open_archi.persistence.diagrams.architectural.Model;
import com.araguacaima.open_archi.persistence.diagrams.core.specification.ExtractTaggables;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtractTaggablesTest {

    private static final JsonUtils jsonUtils = new JsonUtils();
    private Object model;
    private ExtractTaggables extractTaggables = new ExtractTaggables();

    public ExtractTaggablesTest() {
        Class<Model> modelClass = Model.class;
        String body = "{\n" +
                "   \"status\":\"INITIAL\",\n" +
                "   \"name\":\"Pattern Test 1\",\n" +
                "   \"kind\":\"ARCHITECTURE_MODEL\",\n" +
                "   \"prototype\":true,\n" +
                "   \"shape\":{\n" +
                "      \"type\":\"ARCHITECTURE_MODEL\"\n" +
                "   },\n" +
                "   \"components\":[\n" +
                "      {\n" +
                "         \"key\":-1,\n" +
                "         \"status\":0,\n" +
                "         \"name\":\"New Element 1\",\n" +
                "         \"kind\":\"COMPONENT\",\n" +
                "         \"prototype\":false,\n" +
                "         \"location\":{\n" +
                "            \"x\":\"71.65841674804688\",\n" +
                "            \"y\":\"17.88341293334961\"\n" +
                "         },\n" +
                "         \"shape\":{\n" +
                "            \"type\":\"COMPONENT\",\n" +
                "            \"fill\":\"#1368BD\"\n" +
                "         },\n" +
                "         \"relationships\":[\n" +
                "            {\n" +
                "               \"sourceId\":-1,\n" +
                "               \"destinationId\":-2\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"key\":-2,\n" +
                "         \"status\":0,\n" +
                "         \"name\":\"New Element 2\",\n" +
                "         \"kind\":\"COMPONENT\",\n" +
                "         \"prototype\":false,\n" +
                "         \"location\":{\n" +
                "            \"x\":\"71.65841674804688\",\n" +
                "            \"y\":\"72.8834129333496\"\n" +
                "         },\n" +
                "         \"shape\":{\n" +
                "            \"type\":\"COMPONENT\",\n" +
                "            \"fill\":\"#1368BD\"\n" +
                "         },\n" +
                "         \"relationships\":[\n" +
                "            {\n" +
                "               \"sourceId\":-1,\n" +
                "               \"destinationId\":-2\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   ]\n" +
                "}";
        try {
            model = jsonUtils.fromJSON(body, modelClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsSatisfiedBy() {
        Map<String, Object> map = new HashMap<>();
        map.put("Initiator", model);
        extractTaggables.isSatisfiedBy(model, map);
    }
}
