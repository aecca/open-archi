package com.araguacaima.open_archi.web.wrapper;

import com.araguacaima.commons.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jirutka.rsql.parser.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class JsonPathRsqlVisitor implements RSQLVisitor<String, String> {

    private static Logger log = LoggerFactory.getLogger(JsonPathRsqlVisitor.class);
    private Object json;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonUtils jsonUtils = new JsonUtils();
    private String filter;
    public static final String GET_ALL_RESULTS = "id==*";

    public JsonPathRsqlVisitor(Object json) {
        this(json, null);
    }

    public JsonPathRsqlVisitor(Object json, String filter) {
        this.json = json;
        this.filter = filter;
        mapper = jsonUtils.buildObjectMapper(filter);
    }

    public JsonPathRsqlVisitor(ObjectMapper mapper, Object json) {
        this.mapper = mapper;
        this.json = json;
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
        if (GET_ALL_RESULTS.equals(filter)) {
            result = json;
        } else {
            try {
                List<String> arguments = node.getArguments();
                result = JsonParserSpecification.parse(root == null ? json : root,
                        selector,
                        arguments.size() == 1 ? arguments.get(0) : arguments,
                        op);
            } catch (IOException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                log.error(e.getMessage());
            }
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