package com.araguacaima.gsa.msa.web;

import com.araguacaima.commons.utils.JsonUtils;
import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.gsa.msa.web.wrapper.RsqlJsonFilter;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Taggable;
import com.araguacaima.gsa.persistence.utils.JPAEntityManagerUtils;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.template.jade.JadeTemplateEngine;
import spark.template.jade.loader.SparkClasspathTemplateLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Server {
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String HTML_CONTENT_TYPE = "text/html";
    private static final String EMPTY_RESPONSE = "";
    private static JadeConfiguration config = new JadeConfiguration();
    private static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static Logger log = LoggerFactory.getLogger(Server.class);
    private static final JsonUtils jsonUtils = new JsonUtils();
    private static final ExceptionHandler exceptionHandler = new ExceptionHandlerImpl(Exception.class) {
        @Override
        public void handle(Exception exception, Request request, Response response) {
            String message = exception.getMessage();
            response.type("text/html");
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
    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);
    private static Taggable deeplyFulfilledParentModel;
    private static com.araguacaima.gsa.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel;
    private static com.araguacaima.gsa.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel;
    private static com.araguacaima.gsa.persistence.diagrams.er.Model deeplyFulfilledERModel;
    private static com.araguacaima.gsa.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel;
    private static com.araguacaima.gsa.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel;
    private static com.araguacaima.gsa.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel;
    private static com.araguacaima.gsa.persistence.diagrams.classes.Model deeplyFulfilledClassesModel;

    static {
        config.setTemplateLoader(templateLoader);
        deeplyFulfilledParentModel = reflectionUtils.deepInitialization(Taggable.class);
        deeplyFulfilledArchitectureModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.architectural.Model.class);
        deeplyFulfilledArchitectureModel.setKind(ElementKind.ARCHITECTURAL_MODEL);
        deeplyFulfilledBpmModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.bpm.Model.class);
        deeplyFulfilledBpmModel.setKind(ElementKind.BPM_MODEL);
        deeplyFulfilledERModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.er.Model.class);
        deeplyFulfilledERModel.setKind(ElementKind.ENTITY_RELATIONSHIP_MODEL);
        deeplyFulfilledFlowchartModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.flowchart.Model.class);
        deeplyFulfilledFlowchartModel.setKind(ElementKind.FLOWCHART_MODEL);
        deeplyFulfilledGanttModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.gantt.Model.class);
        deeplyFulfilledGanttModel.setKind(ElementKind.GANTT_MODEL);
        deeplyFulfilledSequenceModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.sequence.Model.class);
        deeplyFulfilledSequenceModel.setKind(ElementKind.SEQUENCE_MODEL);
        deeplyFulfilledClassesModel = reflectionUtils.deepInitialization(com.araguacaima.gsa.persistence.diagrams.classes.Model.class);
        deeplyFulfilledClassesModel.setKind(ElementKind.UML_CLASS_MODEL);
    }

    public static void main(String[] args)
            throws GeneralSecurityException {

        int assignedPort = getAssignedPort();
        port(assignedPort);
        log.info("Server listen on port '" + assignedPort + "'");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "OpenArchi API");

        staticFiles.location("/web/public");
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
        get("/", (req, res) -> new ModelAndView(map, "home"), engine);
        path("/api", () -> {
            exception(Exception.class, exceptionHandler);
            before("/*", (req, res) -> log.info("Received api call to " + req.requestMethod() + " " + req.pathInfo()));
            options("/models", (request, response) -> {
                response.status(HTTP_OK);
                response.header("Allow", "POST, GET");
                response.header("Content-Type", JSON_CONTENT_TYPE + ", " + HTML_CONTENT_TYPE);
                String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                if (accessControlRequestHeaders != null) {
                    response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                }
                String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                if (accessControlRequestMethod != null) {
                    response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                }
                return jsonUtils.toJSON(deeplyFulfilledParentModel);
            });
            post("/models", (request, response) -> {
                Taggable model = jsonUtils.fromJSON(request.body(), Taggable.class);
                try {
                    model.validateCreation();
                    Util.populate(model);
                    response.status(HTTP_CREATED);
                    response.type(JSON_CONTENT_TYPE);
                    response.header("Location", request.pathInfo() + "/models/" + model.getId());
                    return EMPTY_RESPONSE;
                } catch (Throwable ex) {
                    return throwError(response, ex);
                }
            });
            get("/models/:uuid", (request, response) -> {
                try {
                    String id = request.params(":uuid");
                    Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                    model.validateRequest();
                    response.status(HTTP_OK);
                    response.type(JSON_CONTENT_TYPE);
                    return jsonUtils.toJSON(model);
                } catch (Exception ex) {
                    return throwError(response, ex);
                }
            });
            get("/models", (request, response) -> {
                return getList(request, response, Taggable.GET_ALL_MODELS, null);
            });
            post("/models/:uuid/children", (request, response) -> {
                response.status(HTTP_NOT_IMPLEMENTED);
                response.type(JSON_CONTENT_TYPE);
                return EMPTY_RESPONSE;
            });
            get("/models/:uuid/children", (request, response) -> {
                response.status(HTTP_NOT_IMPLEMENTED);
                response.type(JSON_CONTENT_TYPE);
                return EMPTY_RESPONSE;
            });
            get("/models/:uuid/parent", (request, response) -> {
                response.status(HTTP_NOT_IMPLEMENTED);
                response.type(JSON_CONTENT_TYPE);
                return EMPTY_RESPONSE;
            });
            options("/models/architectures", (request, response) -> getOptions(request, response, deeplyFulfilledArchitectureModel));
            get("/models/architectures", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "ArchitectureModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
            options("/models/bpms", (request, response) -> {
                return getOptions(request, response, deeplyFulfilledBpmModel);
            });
            get("/models/bpms", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "BpmModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
            options("/models/ers", (request, response) -> {
                return getOptions(request, response, deeplyFulfilledERModel);
            });
            get("/models/ers", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "ERModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
            options("/models/flowcharts", (request, response) -> {
                return getOptions(request, response, deeplyFulfilledFlowchartModel);
            });
            get("/models/flowcharts", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "FlowchartModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
            options("/models/gantts", (request, response) -> {
                return getOptions(request, response, deeplyFulfilledGanttModel);
            });
            get("/models/gantts", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "GanttModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
            options("/models/sequences", (request, response) -> {
                return getOptions(request, response, deeplyFulfilledSequenceModel);
            });
            get("/models/sequences", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "SequenceModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
            options("/models/classes", (request, response) -> {
                return getOptions(request, response, deeplyFulfilledClassesModel);
            });
            get("/models/classes", (request, response) -> {
                Map<String, Object> params = new HashMap<>();
                params.put("modelType", "ClassesModel");
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(getList(request, response, Taggable.GET_MODELS_BY_TYPE, params));
            });
        });
    }

    private static Object getOptions(Request request, Response response, Object object) throws IOException {
        response.status(HTTP_OK);
        response.header("Allow", "GET");
        Map<String, Object> jsonMap = new HashMap<>();

        String contentType = getContentType(request);
        response.header("Content-Type", contentType);
        try {
            if (contentType.equals(HTML_CONTENT_TYPE)) {
                String json = request.pathInfo().replaceFirst(request.pathInfo(), "");
                jsonMap.put("title", StringUtils.capitalize(json));
                jsonMap.put("json", jsonUtils.toJSON(object));
                return render(jsonMap, "json");
            } else {
                return jsonUtils.toJSON(object);
            }
        } catch (Throwable t) {
            jsonMap = new HashMap<>();
            jsonMap.put("error", t.getMessage());
            if (contentType.equals(HTML_CONTENT_TYPE)) {
                return render(jsonMap, "json");
            } else {
                return jsonUtils.toJSON(jsonMap);
            }
        }
    }

    private static Object getList(Request request, Response response, String query, Map<String, Object> params) throws IOException, URISyntaxException {
        response.status(HTTP_OK);

        List<Taggable> models = JPAEntityManagerUtils.executeQuery(Taggable.class, query, params);
        String jsonObjects = jsonUtils.toJSON(models);
        Object filter_ = filter(request.queryParams("$filter"), jsonObjects);
        String json = request.pathInfo().replaceFirst("/api/models", "");
        String contentType = getContentType(request);
        response.header("Content-Type", contentType);
        if (contentType.equals(HTML_CONTENT_TYPE)) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("title", StringUtils.capitalize(json));
            jsonMap.put("json", jsonUtils.toJSON(filter_));
            return render(jsonMap, "json");
        } else {
            return filter_.getClass().equals(String.class) ? filter_ : jsonUtils.toJSON(filter_);
        }
    }

    private static Object throwError(Response response, Throwable ex) {
        response.status(HTTP_BAD_REQUEST);
        response.type(JSON_CONTENT_TYPE);
        return ex.getMessage();
    }

    private static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    private static Object filter(String query, String json)
            throws IOException, URISyntaxException {

        if (query == null) {
            return json;
        } else {
            return RsqlJsonFilter.rsql(query, json);
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

    private static String getContentType(Request request) {
        String accept = request.headers("Accept");
        if (accept == null) {
            accept = request.headers("ACCEPT");
        }
        if (accept == null) {
            accept = request.headers("accept");
        }
        if (accept != null && accept.trim().equalsIgnoreCase(HTML_CONTENT_TYPE)) {
            return HTML_CONTENT_TYPE;
        } else {
            return JSON_CONTENT_TYPE;
        }
    }

    private static String renderContent(String htmlFile) {
        try {
            // If you are using maven then your files
            // will be in a folder called resources.
            // getResource() gets that folder
            // and any files you specify.
            URL url = Server.class.getResource("/web/views/" + "index.html");

            // Return a String which has all
            // the contents of the file.
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {
            // Add your own exception handlers here.
        }
        return null;
    }
}

