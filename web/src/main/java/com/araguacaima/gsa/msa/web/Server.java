package com.araguacaima.gsa.msa.web;

import com.araguacaima.gsa.msa.web.wrapper.RsqlJsonFilter;
import com.araguacaima.gsa.persistence.diagrams.core.Taggable;
import com.araguacaima.gsa.persistence.utils.JPAEntityManagerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.template.jade.JadeTemplateEngine;
import spark.template.jade.loader.SparkClasspathTemplateLoader;

import java.io.*;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Server {
    private static final String DB_PATH = "./db/cards.json";
    private static final String CONTENT_TYPE = "application/json";
    private static final String EMPTY_RESPONSE = "";
    private static JadeConfiguration config = new JadeConfiguration();
    private static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static Logger log = LoggerFactory.getLogger(Server.class);

    private static final ExceptionHandler exceptionHandler = new ExceptionHandlerImpl(Exception.class) {
        @Override
        public void handle(Exception exception, Request request, Response response) {
            String message = exception.getMessage();
            StackTraceElement[] stackTrace = exception.getStackTrace();
            if (message == null) {
                try {
                    message = exception.getCause().getMessage();
                } catch (Throwable ignored) {
                }
                if (message == null) {
                    List ts = Arrays.asList(stackTrace);
                    message = StringUtils.join(ts);
                }
            }
            log.error("Error '" + message + "'");
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("title", "Error");
            errorMap.put("message", message);
            errorMap.put("stack", stackTrace);
            response.body(render(errorMap, "error"));
        }
    };
    private static TemplateLoader templateLoader = new SparkClasspathTemplateLoader("web/views");

    static {
        config.setTemplateLoader(templateLoader);
    }

    public static void main(String[] args)
            throws GeneralSecurityException {

        int assignedPort = getAssignedPort();
        port(assignedPort);
        log.info("Server listen on port '" + assignedPort + "'");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Architectural Model PoC");

        staticFiles.location("/web/public");
        get("/", (req, res) -> new ModelAndView(map, "home"), engine);
        path("/api", () -> {

            exception(Exception.class, exceptionHandler);

            before("/*", (req, res) -> log.info("Received api call to '" + req.pathInfo() + "'"));

            post("/models", (request, response) -> {
                ObjectMapper mapper = new ObjectMapper();
                Taggable model = mapper.readValue(request.body(), Taggable.class);
                try {
                    model.validateCreation();
                    JPAEntityManagerUtils.persist(model);
                    response.status(HTTP_CREATED);
                    response.type(CONTENT_TYPE);
                    response.header("Location", request.pathInfo() + "/models/" + model.getId());
                    return EMPTY_RESPONSE;
                } catch (Exception ex) {
                    return throwError(response, ex);
                }
            });

            get("/models/:uuid", (request, response) -> {
                try {
                    String id = request.params(":uuid");
                    Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                    model.validateRequest();
                    response.status(HTTP_OK);
                    response.type(CONTENT_TYPE);
                    return  dataToJson(model);
                } catch (Exception ex) {
                    return throwError(response, ex);
                }
            });

            get("/models", (request, response) -> {
                response.status(HTTP_OK);
                response.type(CONTENT_TYPE);

                List<Taggable> models = JPAEntityManagerUtils.executeQuery(Taggable.class, Taggable.GET_ALL_MODELS);
                String jsonObjects = dataToJson(models);
                String filter_ = filter(request.queryParams("$filter"), jsonObjects);
                String json = request.pathInfo().replaceFirst("/api/models", "");
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("title", StringUtils.capitalize(json));
                jsonMap.put("json", filter_);
                return render(jsonMap, "json");

                // return dataToJson(model.getAllPosts());
            });

            post("/models/:uuid/children", (request, response) -> {
                response.status(HTTP_NOT_IMPLEMENTED);
                response.type(CONTENT_TYPE);
                return EMPTY_RESPONSE;
            });

            get("/models/:uuid/children", (request, response) -> {
                response.status(HTTP_NOT_IMPLEMENTED);
                response.type(CONTENT_TYPE);
                return EMPTY_RESPONSE;
            });
        });
    }

    private static Object throwError(Response response, Exception ex) {
        response.status(HTTP_BAD_REQUEST);
        response.type(CONTENT_TYPE);
        return ex.getMessage();
    }

    private static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }

    private static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    private static String filter(String query, String jsonPath)
            throws IOException, URISyntaxException {

        String name = "/db/" + jsonPath;
        log.info("Finding path: " + name);
        InputStream resource = Server.class.getResourceAsStream(name);
        log.info("Resource: " + resource);
        assert resource != null;
        String jsonStr = read(resource);
        log.info(jsonPath + " file loaded!!!");
        if (query == null) {
            return jsonStr;
        } else {
            return RsqlJsonFilter.rsql(query, jsonStr);
        }
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return engine.render(new ModelAndView(model, templatePath));
    }

    private static String read(InputStream input)
            throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}

