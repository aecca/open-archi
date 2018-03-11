package com.araguacaima.open_archi.web;

import com.araguacaima.commons.utils.EnumsUtils;
import com.araguacaima.commons.utils.JsonUtils;
import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.commons.IdName;
import com.araguacaima.open_archi.persistence.diagrams.architectural.*;
import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.wrapper.RsqlJsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.template.jade.JadeTemplateEngine;
import spark.template.jade.loader.SparkClasspathTemplateLoader;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
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
    private static EnumsUtils enumsUtils = new EnumsUtils();
    private static String DIAGRAMS_PACKAGES = "com.araguacaima.open_archi.persistence.diagrams";
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
                    List<StackTraceElement> ts = Arrays.asList(stackTrace);
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
    private static Taggable deeplyFulfilledParentModel_1;
    private static Taggable deeplyFulfilledParentModel_2;
    private static String deeplyFulfilledDiagramType;
    private static Map<String, String> deeplyFulfilledIdValue = new HashMap<>();
    private static Map<String, String> deeplyFulfilledIdValue_1 = new HashMap<>();
    private static Map<String, String> deeplyFulfilledIdValue_2 = new HashMap<>();
    private static MetaData deeplyFulfilledMetaData;

    private static Collection<Map<String, String>> deeplyFulfilledIdValueCollection = new ArrayList<>();
    private static Collection<Taggable> deeplyFulfilledParentModelCollection = new ArrayList<>();
    private static Collection<String> deeplyFulfilledDiagramTypesCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.architectural.Model> deeplyFulfilledArchitectureModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.bpm.Model> deeplyFulfilledBpmModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.er.Model> deeplyFulfilledERModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.flowchart.Model> deeplyFulfilledFlowchartModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.gantt.Model> deeplyFulfilledGanttModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.sequence.Model> deeplyFulfilledSequenceModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.classes.Model> deeplyFulfilledClassesModelCollection = new ArrayList<>();
    private static Collection<com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship> deeplyFulfilledArchitectureRelationshipCollection = new ArrayList<>();
    private static Collection<CompositeElement> deeplyFulfilledFeaturesCollection = new ArrayList<>();

    private static com.araguacaima.open_archi.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel;
    private static com.araguacaima.open_archi.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel;
    private static com.araguacaima.open_archi.persistence.diagrams.er.Model deeplyFulfilledERModel;
    private static com.araguacaima.open_archi.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel;
    private static com.araguacaima.open_archi.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel;
    private static com.araguacaima.open_archi.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel;
    private static com.araguacaima.open_archi.persistence.diagrams.classes.Model deeplyFulfilledClassesModel;

    private static com.araguacaima.open_archi.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel_1;
    private static com.araguacaima.open_archi.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel_1;
    private static com.araguacaima.open_archi.persistence.diagrams.er.Model deeplyFulfilledERModel_1;
    private static com.araguacaima.open_archi.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel_1;
    private static com.araguacaima.open_archi.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel_1;
    private static com.araguacaima.open_archi.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel_1;
    private static com.araguacaima.open_archi.persistence.diagrams.classes.Model deeplyFulfilledClassesModel_1;


    private static com.araguacaima.open_archi.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel_2;
    private static com.araguacaima.open_archi.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel_2;
    private static com.araguacaima.open_archi.persistence.diagrams.er.Model deeplyFulfilledERModel_2;
    private static com.araguacaima.open_archi.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel_2;
    private static com.araguacaima.open_archi.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel_2;
    private static com.araguacaima.open_archi.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel_2;
    private static com.araguacaima.open_archi.persistence.diagrams.classes.Model deeplyFulfilledClassesModel_2;

    private static com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship deeplyFulfilledArchitectureRelationship;
    private static com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship deeplyFulfilledArchitectureRelationship_1;
    private static com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship deeplyFulfilledArchitectureRelationship_2;

    private static CompositeElement deeplyFulfilledFeature;
    private static CompositeElement deeplyFulfilledFeature_1;
    private static CompositeElement deeplyFulfilledFeature_2;

    private static Set<Class<? extends Taggable>> modelsClasses;
    private static Reflections diagramsReflections;

    private enum HttpMethod {
        get,
        post,
        put,
        patch,
        delete,
        header
    }

    private enum InputOutput {
        input,
        output
    }

    static {
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setTemplateLoader(templateLoader);
        deeplyFulfilledMetaData = reflectionUtils.deepInitialization(MetaData.class);
        deeplyFulfilledParentModel = reflectionUtils.deepInitialization(Taggable.class);
        deeplyFulfilledArchitectureModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
        deeplyFulfilledArchitectureModel.setKind(ElementKind.ARCHITECTURE_MODEL);
        deeplyFulfilledBpmModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
        deeplyFulfilledBpmModel.setKind(ElementKind.BPM_MODEL);
        deeplyFulfilledERModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
        deeplyFulfilledERModel.setKind(ElementKind.ENTITY_RELATIONSHIP_MODEL);
        deeplyFulfilledFlowchartModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
        deeplyFulfilledFlowchartModel.setKind(ElementKind.FLOWCHART_MODEL);
        deeplyFulfilledGanttModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
        deeplyFulfilledGanttModel.setKind(ElementKind.GANTT_MODEL);
        deeplyFulfilledSequenceModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
        deeplyFulfilledSequenceModel.setKind(ElementKind.SEQUENCE_MODEL);
        deeplyFulfilledClassesModel = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
        deeplyFulfilledClassesModel.setKind(ElementKind.UML_CLASS_MODEL);
        deeplyFulfilledArchitectureRelationship = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship.class);

        deeplyFulfilledParentModel_1 = reflectionUtils.deepInitialization(Taggable.class);
        deeplyFulfilledArchitectureModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
        deeplyFulfilledArchitectureModel_1.setKind(ElementKind.ARCHITECTURE_MODEL);
        deeplyFulfilledBpmModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
        deeplyFulfilledBpmModel_1.setKind(ElementKind.BPM_MODEL);
        deeplyFulfilledERModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
        deeplyFulfilledERModel_1.setKind(ElementKind.ENTITY_RELATIONSHIP_MODEL);
        deeplyFulfilledFlowchartModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
        deeplyFulfilledFlowchartModel_1.setKind(ElementKind.FLOWCHART_MODEL);
        deeplyFulfilledGanttModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
        deeplyFulfilledGanttModel_1.setKind(ElementKind.GANTT_MODEL);
        deeplyFulfilledSequenceModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
        deeplyFulfilledSequenceModel_1.setKind(ElementKind.SEQUENCE_MODEL);
        deeplyFulfilledClassesModel_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
        deeplyFulfilledClassesModel_1.setKind(ElementKind.UML_CLASS_MODEL);
        deeplyFulfilledArchitectureRelationship_1 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship.class);

        deeplyFulfilledParentModel_2 = reflectionUtils.deepInitialization(Taggable.class);
        deeplyFulfilledArchitectureModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
        deeplyFulfilledArchitectureModel_2.setKind(ElementKind.ARCHITECTURE_MODEL);
        deeplyFulfilledBpmModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
        deeplyFulfilledBpmModel_2.setKind(ElementKind.BPM_MODEL);
        deeplyFulfilledERModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
        deeplyFulfilledERModel_2.setKind(ElementKind.ENTITY_RELATIONSHIP_MODEL);
        deeplyFulfilledFlowchartModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
        deeplyFulfilledFlowchartModel_2.setKind(ElementKind.FLOWCHART_MODEL);
        deeplyFulfilledGanttModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
        deeplyFulfilledGanttModel_2.setKind(ElementKind.GANTT_MODEL);
        deeplyFulfilledSequenceModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
        deeplyFulfilledSequenceModel_2.setKind(ElementKind.SEQUENCE_MODEL);
        deeplyFulfilledClassesModel_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
        deeplyFulfilledClassesModel_2.setKind(ElementKind.UML_CLASS_MODEL);
        deeplyFulfilledArchitectureRelationship_2 = reflectionUtils.deepInitialization(com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship.class);

        deeplyFulfilledParentModelCollection.add(deeplyFulfilledArchitectureModel);
        deeplyFulfilledParentModelCollection.add(deeplyFulfilledArchitectureModel_1);
        deeplyFulfilledParentModelCollection.add(deeplyFulfilledArchitectureModel_2);

        deeplyFulfilledArchitectureModelCollection.add(deeplyFulfilledArchitectureModel);
        deeplyFulfilledArchitectureModelCollection.add(deeplyFulfilledArchitectureModel_1);
        deeplyFulfilledArchitectureModelCollection.add(deeplyFulfilledArchitectureModel_2);

        deeplyFulfilledBpmModelCollection.add(deeplyFulfilledBpmModel);
        deeplyFulfilledBpmModelCollection.add(deeplyFulfilledBpmModel_1);
        deeplyFulfilledBpmModelCollection.add(deeplyFulfilledBpmModel_2);

        deeplyFulfilledERModelCollection.add(deeplyFulfilledERModel);
        deeplyFulfilledERModelCollection.add(deeplyFulfilledERModel_1);
        deeplyFulfilledERModelCollection.add(deeplyFulfilledERModel_2);

        deeplyFulfilledFlowchartModelCollection.add(deeplyFulfilledFlowchartModel);
        deeplyFulfilledFlowchartModelCollection.add(deeplyFulfilledFlowchartModel_1);
        deeplyFulfilledFlowchartModelCollection.add(deeplyFulfilledFlowchartModel_2);

        deeplyFulfilledGanttModelCollection.add(deeplyFulfilledGanttModel);
        deeplyFulfilledGanttModelCollection.add(deeplyFulfilledGanttModel_1);
        deeplyFulfilledGanttModelCollection.add(deeplyFulfilledGanttModel_2);

        deeplyFulfilledSequenceModelCollection.add(deeplyFulfilledSequenceModel);
        deeplyFulfilledSequenceModelCollection.add(deeplyFulfilledSequenceModel_1);
        deeplyFulfilledSequenceModelCollection.add(deeplyFulfilledSequenceModel_2);

        deeplyFulfilledClassesModelCollection.add(deeplyFulfilledClassesModel);
        deeplyFulfilledClassesModelCollection.add(deeplyFulfilledClassesModel_1);
        deeplyFulfilledClassesModelCollection.add(deeplyFulfilledClassesModel_2);

        diagramsReflections = new Reflections(DIAGRAMS_PACKAGES, Taggable.class.getClassLoader());
        modelsClasses = diagramsReflections.getSubTypesOf(Taggable.class);
        CollectionUtils.filter(modelsClasses, clazz -> clazz.getSuperclass().equals(Element.class) && !Modifier.isAbstract(clazz.getModifiers()));

        Set<Class<? extends DiagramableElement>> diagramTypes = diagramsReflections.getSubTypesOf(DiagramableElement.class);
        IterableUtils.forEach(diagramTypes, input -> {
            try {
                deeplyFulfilledDiagramTypesCollection.add(input.newInstance().getKind().name());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        deeplyFulfilledDiagramType = deeplyFulfilledDiagramTypesCollection.iterator().next();

        deeplyFulfilledIdValue.put("id", UUID.randomUUID().toString());
        deeplyFulfilledIdValue.put("name", RandomStringUtils.random(20, true, false));

        deeplyFulfilledIdValue_1.put("id", UUID.randomUUID().toString());
        deeplyFulfilledIdValue_1.put("name", RandomStringUtils.random(20, true, false));

        deeplyFulfilledIdValue_2.put("id", UUID.randomUUID().toString());
        deeplyFulfilledIdValue_2.put("name", RandomStringUtils.random(20, true, false));

        deeplyFulfilledIdValueCollection.add(deeplyFulfilledIdValue);
        deeplyFulfilledIdValueCollection.add(deeplyFulfilledIdValue_1);
        deeplyFulfilledIdValueCollection.add(deeplyFulfilledIdValue_2);

        //noinspection ResultOfMethodCallIgnored
        JPAEntityManagerUtils.getEntityManager();
    }

    public static void main(String[] args) throws GeneralSecurityException {

        int assignedPort = getAssignedPort();
        port(assignedPort);
        log.info("Server listen on port '" + assignedPort + "'");

        staticFiles.location("/web/public");
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
        path("/", () -> {
            Map<String, Object> mapHome = new HashMap<>();
            mapHome.put("title", "Home");
            get("/", (req, res) ->  new ModelAndView(mapHome, "home"), engine);
        });
        redirect.get("/open-archi", "/open-archi/", Redirect.Status.TEMPORARY_REDIRECT);
        path("/open-archi", () -> {
            redirect.get("/api", "/open-archi/api/", Redirect.Status.PERMANENT_REDIRECT);
            redirect.get("/editor", "/open-archi/editor/", Redirect.Status.PERMANENT_REDIRECT);
            Map<String, Object> mapHome = new HashMap<>();
            mapHome.put("title", "OpenArchi");
            get("/", (req, res) -> new ModelAndView(mapHome, "/open-archi/home"), engine);
            path("/editor", () -> {
                Map<String, Object> mapEditor = new HashMap<>();
                exception(Exception.class, exceptionHandler);
                mapEditor.put("title", "Editor");
                mapEditor.put("diagramTypes", deeplyFulfilledDiagramTypesCollection);
                get("/", (req, res) -> {
                    mapEditor.put("palette", jsonUtils.toJSON(getArchitecturePalette()));
                    mapEditor.put("source", "basic");
                    mapEditor.put("examples", getExamples());
                    return new ModelAndView(mapEditor, "/open-archi/editor");
                }, engine);
            });
            path("/samples", () -> {
                get("/basic", (request, response) -> {
                    Map<String, Object> map = new HashMap<>();
                    List nodeDataArray = jsonUtils.fromJSON("[\n" +
                            "                    {key: 1, text: \"Alpha\", color: \"lightblue\"},\n" +
                            "                    {key: 2, text: \"Beta\", color: \"orange\"},\n" +
                            "                    {key: 3, text: \"Gamma\", color: \"lightgreen\", group: 5},\n" +
                            "                    {key: 4, text: \"Delta\", color: \"pink\", group: 5},\n" +
                            "                    {key: 5, text: \"Epsilon\", color: \"green\", isGroup: true}\n" +
                            "        ]", List.class);
                    List linkDataArray = jsonUtils.fromJSON("[\n" +
                            "                    {from: 1, to: 2, color: \"blue\"},\n" +
                            "                    {from: 2, to: 2},\n" +
                            "                    {from: 3, to: 4, color: \"green\"},\n" +
                            "                    {from: 3, to: 1, color: \"purple\"}\n" +
                            "        ]", List.class);
                    map.put("nodeDataArray", nodeDataArray);
                    map.put("linkDataArray", linkDataArray);
                    map.put("source", "basic");
                    map.put("mainTitle", "Propuesta para diagrama básico de componentes - Primer nivel");
                    map.put("caption", "¡Leyendo ya desde Open Archi!");
                    map.put("fullDescription", "Sencillo, pero fácil de adaptar para construir modelos de solución a alto nivel. Intuitivo y fácil de usar.");
                    List<String> steps = new ArrayList<>();
                    steps.add("Con doble-click en cualquier área vacía del canvas se crea un nuevo componente (siempre será una cajita)");
                    steps.add("Con doble-click en cualquier componente (cajita) se editará su nombre");
                    steps.add("Al hacer click en el borde de un componente se puede crear conectores (flechas) hacia cualquier componente");
                    map.put("steps", steps);
                    return new ModelAndView(map, "editor");
                });
            });
            before("/api/*", (req, res) -> log.info("Received api call to " + req.requestMethod() + " " + req.pathInfo()));
            path("/api", () -> {
                Map<String, Object> mapApi = new HashMap<>();
                exception(Exception.class, exceptionHandler);
                mapApi.put("title", "Api");
                get("/", (req, res) -> new ModelAndView(mapApi, "/open-archi/apis"), engine);
                options("/diagrams/architectures", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledArchitectureModelCollection, deeplyFulfilledArchitectureModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/architectures", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/architectures", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.architectural.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.ARCHITECTURE_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.ARCHITECTURE_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        return throwError(response, ex);
                    }
                });
                patch("/diagrams/architectures/:uuid", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.architectural.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid kind of model");
                        }
                        String id = request.params(":uuid");
                        model.setId(id);
                        model.validateModification();
                        DBUtil.update(model);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/architectures/:uuid/relationships", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledArchitectureRelationshipCollection, deeplyFulfilledArchitectureRelationship, HttpMethod.get, HttpMethod.put);
                    return getOptions(request, response, output);
                });
                get("/diagrams/architectures/:uuid/relationships", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":uuid"));
                    return getList(request, response, com.araguacaima.open_archi.persistence.diagrams.architectural.Model.GET_ALL_RELATIONSHIPS, params, Collection.class);
                });
                put("/diagrams/architectures/:uuid/relationships", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship feature = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship.class);
                        if (feature == null) {
                            throw new Exception("Invalid kind of relationship");
                        }
                        feature.validateReplacement();
                        DBUtil.populate(feature);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + feature.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/architectures/:uuid/consumers", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.put);
                    return getOptions(request, response, output);
                });
                get("/diagrams/architectures/:uuid/consumers", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":uuid"));
                    return getList(request, response, com.araguacaima.open_archi.persistence.diagrams.architectural.Model.GET_ALL_CONSUMERS_FOR_MODEL, params, Collection.class);
                });
                post("/diagrams/architectures/:uuid/consumers", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.architectural.Consumer feature = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.Consumer.class);
                        if (feature == null) {
                            throw new Exception("Invalid kind of relationship");
                        }
                        feature.validateReplacement();
                        DBUtil.populate(feature);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + feature.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/diagrams/architectures/:uuid/consumers/:cuuid", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    String id = request.params(":uuid");
                    String cid = request.params(":cuuid");
                    params.put("id", id);
                    params.put("cid", cid);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Model.GET_CONSUMER_FOR_MODEL, params, null);
                });
                put("/diagrams/architectures/:uuid/consumers/:cuuid", (request, response) -> {
                    try {
                        Taggable model = jsonUtils.fromJSON(request.body(), Taggable.class);
                        if (model == null) {
                            throw new Exception("Invalid kind of model");
                        }
                        String id = request.params(":cuuid");
                        model.setId(id);
                        model.validateReplacement();
                        DBUtil.replace(model);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                patch("/diagrams/architectures/:uuid/consumers/:cuuid", (request, response) -> {
                    try {
                        Consumer consumer = jsonUtils.fromJSON(request.body(), Consumer.class);
                        if (consumer == null) {
                            throw new Exception("Invalid kind of consumer");
                        }
                        String id = request.params(":cuuid");
                        consumer.setId(id);
                        consumer.validateModification();
                        DBUtil.update(consumer);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/diagrams/architectures/:uuid/software-systems", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":uuid"));
                    return getList(request, response, com.araguacaima.open_archi.persistence.diagrams.architectural.Model.GET_ALL_SOFTWARE_SYSTEMS, params, Collection.class);
                });
                post("/diagrams/architectures/:uuid/software-systems", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.architectural.SoftwareSystem softwareSystem = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.SoftwareSystem.class);
                        if (softwareSystem == null) {
                            throw new Exception("Invalid kind of container");
                        }
                        String id = request.params(":uuid");
                        softwareSystem.validateCreation();
                        Model model = JPAEntityManagerUtils.find(com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class, id);
                        DBUtil.persist(softwareSystem);
                        model.getSoftwareSystems().add(softwareSystem);
                        DBUtil.update(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + softwareSystem.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/diagrams/architectures/:uuid/software-systems/:suuid", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    String id = request.params(":uuid");
                    String sid = request.params(":suuid");
                    params.put("id", id);
                    params.put("sid", sid);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, com.araguacaima.open_archi.persistence.diagrams.architectural.Model.GET_SOFTWARE_SYSTEM, params, null);
                });
                put("/diagrams/architectures/:uuid/software-systems/:suuid", (request, response) -> {
                    try {
                        Container model = jsonUtils.fromJSON(request.body(), Container.class);
                        if (model == null) {
                            throw new Exception("Invalid kind of container");
                        }
                        String id = request.params(":cuuid");
                        model.setId(id);
                        model.validateReplacement();
                        DBUtil.replace(model);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                patch("/diagrams/architectures/:uuid/software-systems/:suuid", (request, response) -> {
                    try {
                        Container container = jsonUtils.fromJSON(request.body(), Container.class);
                        if (container == null) {
                            throw new Exception("Invalid kind of container");
                        }
                        String id = request.params(":cuuid");
                        container.setId(id);
                        container.validateModification();
                        DBUtil.update(container);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/diagrams/architectures/:uuid/software-systems/:suuid/containers", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":suuid"));
                    return getList(request, response, SoftwareSystem.GET_ALL_CONTAINERS, params, Collection.class);
                });
                post("/diagrams/architectures/:uuid/software-systems/:suuid/containers", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.architectural.Container container = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.Container.class);
                        if (container == null) {
                            throw new Exception("Invalid kind of container");
                        }
                        String id = request.params(":suuid");
                        container.validateCreation();
                        SoftwareSystem softwareSystem = JPAEntityManagerUtils.find(SoftwareSystem.class, id);
                        DBUtil.persist(container);
                        softwareSystem.getContainers().add(container);
                        DBUtil.update(softwareSystem);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + container.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/diagrams/architectures/:uuid/software-systems/:suuid/containers/:cuuid", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    String id = request.params(":suuid");
                    String cid = request.params(":cuuid");
                    params.put("id", id);
                    params.put("cid", cid);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, SoftwareSystem.GET_CONTAINER, params, null);
                });
                put("/diagrams/architectures/:uuid/software-systems/:suuid/containers/:cuuid", (request, response) -> {
                    try {
                        Container model = jsonUtils.fromJSON(request.body(), Container.class);
                        if (model == null) {
                            throw new Exception("Invalid kind of container");
                        }
                        String id = request.params(":cuuid");
                        model.setId(id);
                        model.validateReplacement();
                        DBUtil.replace(model);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                patch("/diagrams/architectures/:uuid/software-systems/:suuid/containers/:cuuid", (request, response) -> {
                    try {
                        Container container = jsonUtils.fromJSON(request.body(), Container.class);
                        if (container == null) {
                            throw new Exception("Invalid kind of container");
                        }
                        String id = request.params(":cuuid");
                        container.setId(id);
                        container.validateModification();
                        DBUtil.update(container);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/bpms", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledBpmModelCollection, deeplyFulfilledBpmModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/bpms", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/bpms", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.bpm.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.BPM_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.BPM_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/ers", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledERModelCollection, deeplyFulfilledERModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/ers", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/ers", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.er.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.ENTITY_RELATIONSHIP_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.ENTITY_RELATIONSHIP_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/flowcharts", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledFlowchartModelCollection, deeplyFulfilledFlowchartModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/flowcharts", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/flowcharts", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.flowchart.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.FLOWCHART_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.FLOWCHART_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/gantts", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledGanttModelCollection, deeplyFulfilledGanttModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/gantts", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/gantts", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.gantt.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.GANTT_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.GANTT_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/sequences", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledSequenceModelCollection, deeplyFulfilledSequenceModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/sequences", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/sequences", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.sequence.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.SEQUENCE_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.SEQUENCE_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/diagrams/classes", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledClassesModelCollection, deeplyFulfilledClassesModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/diagrams/classes", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
                    response.type(JSON_CONTENT_TYPE);
                    return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
                });
                post("/diagrams/classes", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.classes.Model model = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
                        if (model == null) {
                            throw new Exception("Invalid model");
                        }
                        if (model.getKind() != ElementKind.UML_CLASS_MODEL) {
                            throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.UML_CLASS_MODEL + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/models", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                post("/models", (request, response) -> {
                    try {
                        Taggable model = null;
                        Map<String, Object> incomingModel = (Map<String, Object>) jsonUtils.fromJSON(request.body(), Map.class);
                        Object kind = incomingModel.get("kind");
                        for (Class<? extends Taggable> modelClass : modelsClasses) {
                            Field field = reflectionUtils.getField(modelClass, "kind");
                            if (field != null) {
                                field.setAccessible(true);
                                Object obj = modelClass.newInstance();
                                Object thisKind = field.get(obj);
                                thisKind = enumsUtils.getStringValue((Enum) thisKind);
                                if (kind.equals(thisKind)) {
                                    model = jsonUtils.fromJSON(request.body(), modelClass);
                                    break;
                                }
                            }
                        }
                        if (model == null) {
                            throw new Exception("Invalid kind of model '" + kind + "'");
                        }
                        model.validateCreation();
                        DBUtil.populate(model);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/models", (request, response) -> getList(request, response, Taggable.GET_ALL_MODELS, null, null));
                options("/models/:uuid", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModel, null, HttpMethod.get, null);
                    return getOptions(request, response, output);
                });
                get("/models/:uuid", (request, response) -> {
                    try {
                        String id = request.params(":uuid");
                        Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                        if (model != null) {
                            model.validateRequest();
                        }
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(model);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                put("/models/:uuid", (request, response) -> {
                    try {
                        Taggable model = jsonUtils.fromJSON(request.body(), Taggable.class);
                        if (model == null) {
                            throw new Exception("Invalid kind of model");
                        }
                        String id = request.params(":uuid");
                        model.setId(id);
                        model.validateReplacement();
                        DBUtil.replace(model);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                patch("/models/:uuid", (request, response) -> {
                    try {
                        Taggable model = jsonUtils.fromJSON(request.body(), Taggable.class);
                        if (model == null) {
                            throw new Exception("Invalid kind of model");
                        }
                        String id = request.params(":uuid");
                        model.setId(id);
                        model.validateModification();
                        DBUtil.update(model);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/models/:uuid/children", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.put);
                    return getOptions(request, response, output);
                });
                get("/models/:uuid/children", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":uuid"));
                    return getList(request, response, Item.GET_ALL_CHILDREN, params, Collection.class);
                });
                put("/models/:uuid/children", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    response.type(JSON_CONTENT_TYPE);
                    return EMPTY_RESPONSE;
                });
                options("/models/:uuid/parent", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModel, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/models/:uuid/parent", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    response.type(JSON_CONTENT_TYPE);
                    return EMPTY_RESPONSE;
                });
                post("/models/:uuid/parent", (request, response) -> {
                    try {
                        CompositeElement model;
                        try {
                            model = jsonUtils.fromJSON(request.body(), CompositeElement.class);
                        } catch (Throwable t) {
                            throw new Exception("Invalid kind of parent model info due: '" + t.getMessage() + "'");
                        }
                        DBUtil.persist(model);
                        response.status(HTTP_CREATED);
                        response.header("Location", request.pathInfo() + "/" + model.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/models/:uuid/meta-data", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(null, deeplyFulfilledMetaData, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                get("/models/:uuid/meta-data", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":uuid"));
                    return getElement(request, response, Item.GET_META_DATA, params, MetaData.class);
                });
                post("/models/:uuid/meta-data", (request, response) -> {
                    try {
                        MetaData metaData = jsonUtils.fromJSON(request.body(), MetaData.class);
                        if (metaData == null) {
                            throw new Exception("Invalid metadata");
                        }
                        metaData.validateCreation();
                        DBUtil.populate(metaData);
                        response.status(HTTP_CREATED);
                        response.header("Location", request.pathInfo() + "/" + request.params(":uuid") + "/meta-data");
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/models/:uuid/features", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledFeaturesCollection, deeplyFulfilledFeature, HttpMethod.get, HttpMethod.put);
                    return getOptions(request, response, output);
                });
                get("/models/:uuid/features", (request, response) -> {
                    Map<String, Object> params = new HashMap<>();
                    params.put("id", request.params(":uuid"));
                    return getList(request, response, Element.GET_ALL_FEATURES, params, Collection.class);
                });
                put("/models/:uuid/features", (request, response) -> {
                    try {

                        CompositeElement feature = jsonUtils.fromJSON(request.body(), CompositeElement.class);
                        if (feature == null) {
                            throw new Exception("Invalid kind of feature");
                        }
                        feature.validateReplacement();
                        DBUtil.persist(feature);
                        response.status(HTTP_CREATED);
                        response.header("Location", request.pathInfo() + "/" + feature.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                options("/catalogs/diagram-types", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledDiagramTypesCollection, deeplyFulfilledDiagramType, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                post("/catalogs/diagram-types", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    response.type(JSON_CONTENT_TYPE);
                    return EMPTY_RESPONSE;
                });
                get("/catalogs/diagram-types", (request, response) -> getList(request, response, deeplyFulfilledDiagramTypesCollection));
                options("/catalogs/diagram-names", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledIdValueCollection, deeplyFulfilledIdValue, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                post("/catalogs/diagram-names", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    response.type(JSON_CONTENT_TYPE);
                    return EMPTY_RESPONSE;
                });
                get("/catalogs/diagram-names", (request, response) -> getItemNames(request, response, Item.GET_ALL_DIAGRAM_NAMES));
                options("/catalogs/prototype-names", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledIdValueCollection, deeplyFulfilledIdValue, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                post("/catalogs/prototype-names", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    response.type(JSON_CONTENT_TYPE);
                    return EMPTY_RESPONSE;
                });
                get("/catalogs/prototype-names", (request, response) -> getItemNames(request, response, Item.GET_ALL_PROTOTYPE_NAMES));
                options("/catalogs/consumer-names", (request, response) -> {
                    setCORS(request, response);
                    Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledIdValueCollection, deeplyFulfilledIdValue, HttpMethod.get, HttpMethod.post);
                    return getOptions(request, response, output);
                });
                post("/catalogs/consumer-names", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    response.type(JSON_CONTENT_TYPE);
                    return EMPTY_RESPONSE;
                });
                get("/catalogs/consumer-names", (request, response) -> {
                    String diagramNames = (String) getList(request, response, Item.GET_ALL_CONSUMER_NAMES, null, IdName.class);
                    List diagramNamesList = getListIdName(diagramNames);
                    return getList(request, response, diagramNamesList);
                });
                get("/consumers", (request, response) -> getList(request, response, Item.GET_ALL_CONSUMERS, null, null));
                post("/consumers", (request, response) -> {
                    try {
                        Consumer consumer = jsonUtils.fromJSON(request.body(), Consumer.class);
                        if (consumer == null) {
                            throw new Exception("Invalid kind for consumer");
                        }
                        consumer.validateCreation();
                        DBUtil.populate(consumer);
                        response.status(HTTP_CREATED);
                        response.type(JSON_CONTENT_TYPE);
                        response.header("Location", request.pathInfo() + "/" + consumer.getId());
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/consumers/:uuid", (request, response) -> {
                    try {
                        String id = request.params(":uuid");
                        Consumer consumer = JPAEntityManagerUtils.find(Consumer.class, id);
                        if (consumer != null) {
                            consumer.validateRequest();
                        }
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(consumer);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                put("/consumers/:uuid", (request, response) -> {
                    try {
                        Consumer consumer = jsonUtils.fromJSON(request.body(), Consumer.class);
                        if (consumer == null) {
                            throw new Exception("Invalid kind of consumer");
                        }
                        String id = request.params(":uuid");
                        consumer.setId(id);
                        consumer.validateReplacement();
                        DBUtil.replace(consumer);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                patch("/consumers/:uuid", (request, response) -> {
                    try {
                        Consumer consumer = jsonUtils.fromJSON(request.body(), Consumer.class);
                        if (consumer == null) {
                            throw new Exception("Invalid kind of consumer");
                        }
                        String id = request.params(":uuid");
                        consumer.setId(id);
                        consumer.validateModification();
                        DBUtil.update(consumer);
                        response.status(HTTP_OK);
                        return EMPTY_RESPONSE;
                    } catch (EntityNotFoundException ex) {
                        response.status(HTTP_NOT_FOUND);
                        return EMPTY_RESPONSE;
                    } catch (Throwable ex) {
                        return throwError(response, ex);
                    }
                });
                get("/palette/architectures", (request, response) -> {
                    try {
                        Palette palette = getArchitecturePalette();
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(palette);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                post("/palette/architectures", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    return EMPTY_RESPONSE;
                });
                get("/palette/bpms", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.bpm.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.bpm.Palette();
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(palette);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                post("/palette/bpms", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    return EMPTY_RESPONSE;
                });
                get("/palette/flowcharts", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.flowchart.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.flowchart.Palette();
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(palette);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                post("/palette/flowcharts", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    return EMPTY_RESPONSE;
                });
                get("/palette/gantts", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.gantt.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.gantt.Palette();
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(palette);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                post("/palette/gantts", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    return EMPTY_RESPONSE;
                });
                get("/palette/sequences", (request, response) -> {
                    try {
                        com.araguacaima.open_archi.persistence.diagrams.sequence.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.sequence.Palette();
                        response.status(HTTP_OK);
                        response.type(JSON_CONTENT_TYPE);
                        return jsonUtils.toJSON(palette);
                    } catch (Exception ex) {
                        return throwError(response, ex);
                    }
                });
                post("/palette/sequences", (request, response) -> {
                    response.status(HTTP_NOT_IMPLEMENTED);
                    return EMPTY_RESPONSE;
                });
            });
        });
    }

    private static Object getItemNames(Request request, Response response, String query) throws IOException, URISyntaxException {
        return getItemNames(request, response, query, null);
    }

    private static Object getItemNames(Request request, Response response, String query, String contentType) throws IOException, URISyntaxException {
        String diagramNames = (String) getList(request, response, query, null, IdName.class);
        List diagramNamesList = getListIdName(diagramNames);
        return getList(request, response, diagramNamesList, contentType);
    }

    private static Collection<ExampleData> getExamples() {
        Collection<ExampleData> result = new ArrayList<>();
        result.add(new ExampleData("/diagrams/basic.html", "Diagrama Básico"));
        result.add(new ExampleData("/diagrams/checkBoxes.html", "Features (checkbox)"));
        result.add(new ExampleData("/diagrams/columnResizing.html", "Ajuste de tamaños"));
        result.add(new ExampleData("/diagrams/comments.html", "Comentarios"));
        result.add(new ExampleData("/diagrams/contextMenu.html", "Menú Contextual"));
        result.add(new ExampleData("/diagrams/dataInspector.html", "Meta Datos"));
        result.add(new ExampleData("/diagrams/dragCreating.html", "Creación Ágil"));
        result.add(new ExampleData("/diagrams/draggableLink.html", "Constraints"));
        result.add(new ExampleData("/diagrams/entityRelationship.html", "Entidad Relación"));
        result.add(new ExampleData("/diagrams/flowchart.html", "Flujo de Secuencia"));
        result.add(new ExampleData("/diagrams/gantt.html", "Diagramas Gantt"));
        result.add(new ExampleData("/diagrams/grouping.html", "Expansión"));
        result.add(new ExampleData("/diagrams/guidedDragging.html", "Guías visuales"));
        result.add(new ExampleData("/diagrams/htmlInteraction.html", "Interoperatividad HTML"));
        result.add(new ExampleData("/diagrams/icons.html", "Iconos SVG"));
        result.add(new ExampleData("/diagrams/kanban.html", "Tablero Kanban"));
        result.add(new ExampleData("/diagrams/logicCircuit.html", "Flujo y Secuencia 1"));
        result.add(new ExampleData("/diagrams/mindMap.html", "Mapas Estratégicos"));
        result.add(new ExampleData("/diagrams/navigation.html", "Seguimiento de Flujos"));
        result.add(new ExampleData("/diagrams/orgChartStatic.html", "Zooming"));
        result.add(new ExampleData("/diagrams/records.html", "Mapeo de Features"));
        result.add(new ExampleData("/diagrams/sequenceDiagram.html", "UML de Secuencia"));
        result.add(new ExampleData("/diagrams/shopFloorMonitor.html", "Flujo y Secuencia 2"));
        result.add(new ExampleData("/diagrams/swimBands.html", "Release Planning"));
        result.add(new ExampleData("/diagrams/swimLanes.html", "Diagrama de Procesos"));
        result.add(new ExampleData("/diagrams/umlClass.html", "UML de Clases"));
        result.add(new ExampleData("/diagrams/updateDemo.html", "Actualización Realtime"));
        return result;
    }

    private static Palette getArchitecturePalette() {
        Palette palette = new Palette();
        Map<String, Object> params = new HashMap<>();
        params.put("type", SoftwareSystem.class);
        List<IdName> models;
        models = JPAEntityManagerUtils.executeQuery(IdName.class, Item.GET_ALL_PROTOTYPE_NAMES_BY_TYPE, params);
        int rank = 0;
        for (IdName model : models) {
            PaletteItem item = new PaletteItem();
            item.setRank(rank);
            item.setKind(ElementKind.ARCHITECTURE_MODEL);
            item.setName(model.getName());
            item.setShapeType(ShapeType.RoundedRectangle);
            item.setId(model.getId());
            item.setSize(new Size(40, 40));
            item.setFill(SoftwareSystem.SHAPE_COLOR);
            palette.getSoftwareSystems().add(item);
            rank++;
        }

        params.put("type", Container.class);
        models = JPAEntityManagerUtils.executeQuery(IdName.class, Item.GET_ALL_PROTOTYPE_NAMES_BY_TYPE, params);
        rank = 0;
        for (IdName model : models) {
            PaletteItem item = new PaletteItem();
            item.setRank(rank);
            item.setKind(ElementKind.ARCHITECTURE_MODEL);
            item.setName(model.getName());
            item.setShapeType(ShapeType.RoundedRectangle);
            item.setId(model.getId());
            item.setSize(new Size(40, 40));
            item.setFill(Container.SHAPE_COLOR);
            palette.getContainers().add(item);
            rank++;
        }

        params.put("type", Component.class);
        models = JPAEntityManagerUtils.executeQuery(IdName.class, Item.GET_ALL_PROTOTYPE_NAMES_BY_TYPE, params);
        rank = 0;
        for (IdName model : models) {
            PaletteItem item = new PaletteItem();
            item.setRank(rank);
            item.setKind(ElementKind.ARCHITECTURE_MODEL);
            item.setName(model.getName());
            item.setShapeType(ShapeType.RoundedRectangle);
            item.setId(model.getId());
            item.setSize(new Size(40, 40));
            item.setFill(Component.SHAPE_COLOR);
            palette.getComponents().add(item);
            rank++;
        }
        return palette;
    }

    private static List getListIdName(String diagramNames) throws IOException {
        List diagramNamesList = jsonUtils.fromJSON(diagramNames, List.class);
        CollectionUtils.filter(diagramNamesList, (Predicate<Map<String, String>>) map -> {
            String className = map.get("clazz");
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
                boolean assignableFrom = DiagramableElement.class.isAssignableFrom(clazz);
                if (assignableFrom) {
                    map.put("type", ((DiagramableElement) clazz.newInstance()).getKind().name());
                    map.remove("clazz");
                }
                return assignableFrom;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        });
        return diagramNamesList;
    }

    private static Map<HttpMethod, Map<InputOutput, Object>> setOptionsOutputStructure(
            Object object_httpMethod_1, Object object_httpMethod_2, HttpMethod httpMethod_1, HttpMethod httpMethod_2) {
        Map<HttpMethod, Map<InputOutput, Object>> output = new HashMap<>();
        if (object_httpMethod_1 != null) {
            Map<InputOutput, Object> output_httpMethod_1 = new HashMap<>();
            output_httpMethod_1.put(InputOutput.output, object_httpMethod_1);
            output.put(httpMethod_1, output_httpMethod_1);
        }
        if (object_httpMethod_2 != null) {
            Map<InputOutput, Object> input_httpMethod_2 = new HashMap<>();
            input_httpMethod_2.put(InputOutput.input, object_httpMethod_2);
            output.put(httpMethod_2, input_httpMethod_2);
        }
        return output;
    }

    private static void setCORS(Request request, Response response) {
        response.status(HTTP_OK);
        response.header("Allow", "POST, GET, PUT, OPTIONS, HEAD");
        response.header("Content-Type", JSON_CONTENT_TYPE + ", " + HTML_CONTENT_TYPE);
        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
        if (accessControlRequestHeaders != null) {
            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
        }
        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
        if (accessControlRequestMethod != null) {
            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
        }
    }

    private static String getOptions(Request request, Response response, Map<HttpMethod, Map<InputOutput, Object>> object) throws IOException {
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

    private static Object getList(Request request, Response response, String query, Map<String, Object> params, Class type) throws IOException, URISyntaxException {
        return getList(request, response, query, params, type, null);
    }

    private static Object getList(Request request, Response response, String query, Map<String, Object> params, Class type, String contentType) throws IOException, URISyntaxException {
        response.status(HTTP_OK);

        List models;
        String jsonObjects;
        try {
            models = JPAEntityManagerUtils.executeQuery(type == null ? Taggable.class : type, query, params);
            jsonObjects = jsonUtils.toJSON(models);
        } catch (IllegalArgumentException ignored) {
            models = JPAEntityManagerUtils.executeQuery(Object[].class, query, params);
            jsonObjects = jsonUtils.toJSON(models);
        }

        Object filter_ = filter(request.queryParams("$filter"), jsonObjects);
        String json = request.pathInfo().replaceFirst("/api/models", "");
        contentType = StringUtils.defaultString(contentType, getContentType(request));
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

    private static Object getList(Request request, Response response, Collection models) throws IOException, URISyntaxException {
        return getList(request, response, models, null);
    }

    private static Object getList(Request request, Response response, Collection models, String contentType) throws IOException, URISyntaxException {
        response.status(HTTP_OK);
        String jsonObjects = jsonUtils.toJSON(models);
        Object filter_ = filter(request.queryParams("$filter"), jsonObjects);
        String json = request.pathInfo().replaceFirst("/api/models", "");
        contentType = StringUtils.defaultString(contentType, getContentType(request));
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

    private static Object getElement(Request request, Response response, String query, Map<String, Object> params, Class type) throws IOException, URISyntaxException {
        response.status(HTTP_OK);

        Object element = JPAEntityManagerUtils.executeQuery(type, query, params);
        String jsonElement = jsonUtils.toJSON(element);
        String json = request.pathInfo().replaceFirst("/api/models", "");
        String contentType = getContentType(request);
        response.header("Content-Type", contentType);
        if (contentType.equals(HTML_CONTENT_TYPE)) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("title", StringUtils.capitalize(json));
            jsonMap.put("json", jsonElement);
            return render(jsonMap, "json");
        } else {
            return jsonElement;
        }
    }

    private static String throwError(Response response, Throwable ex) {
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

    private static Object filter(String query, String json) throws IOException, URISyntaxException {

        if (query == null) {
            return json;
        } else {
            return RsqlJsonFilter.rsql(query, json);
        }
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return engine.render(new ModelAndView(model, templatePath));
    }

    private static String read(InputStream input) throws IOException {
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

