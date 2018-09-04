package com.araguacaima.open_archi.web.wrapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.jirutka.rsql.parser.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class JsonPathRsqlVisitor implements RSQLVisitor<String, String> {

    private static Logger log = LoggerFactory.getLogger(JsonPathRsqlVisitor.class);
    private String json;
    private ObjectMapper mapper = new ObjectMapper();

    public JsonPathRsqlVisitor(String json) {
        this.json = json;
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.ANY);
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String visit(final AndNode node, String root) {
        return processNodes(node.getChildren(), root);
    }

    @Override
    public String visit(final OrNode node, String root) {
        return processNodes(node.getChildren(), root);
    }

    @Override
    public String visit(final ComparisonNode node, String root) {

        ComparisonOperator op = node.getOperator();

        String selector = node.getSelector();
        Object result = null;
        List<String> arguments = node.getArguments();

        try {
            result = JsonParserSpecification.parse(root == null ? json : root,
                    selector,
                    arguments.size() == 1 ? arguments.get(0) : arguments,
                    op);
        } catch (IOException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error(e.getMessage());
        }

        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, result);
            return sw.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private String processNodes(List<Node> nodes, String root) {
        String result = root;
        for (Node node : nodes) {
            result = node.accept(this, result);
        }
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, result);
            return sw.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}