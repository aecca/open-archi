package com.araguacaima.open_archi.web.common;

import com.araguacaima.commons.utils.EnumsUtils;
import com.araguacaima.commons.utils.JsonUtils;
import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.Avatar;
import com.araguacaima.open_archi.persistence.meta.Role;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.Server;
import com.araguacaima.open_archi.web.wrapper.AccountWrapper;
import com.araguacaima.open_archi.web.wrapper.RolesWrapper;
import com.araguacaima.open_archi.web.wrapper.RsqlJsonFilter;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.route.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.araguacaima.open_archi.web.Server.engine;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.pac4j.core.context.HttpConstants.HTML_CONTENT_TYPE;

public class Commons {

    public static final String clients = "Google2Client";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String EMPTY_RESPONSE = StringUtils.EMPTY;
    public static final JsonUtils jsonUtils = new JsonUtils();
    public static final String OPEN_ARCHI = "Open-Archi";
    public static final EnumsUtils enumsUtils = new EnumsUtils();
    public static final String DEFAULT_PATH = "/";
    public static final String SEPARATOR_PATH = "/";
    public static ReflectionUtils reflectionUtils = new ReflectionUtils(null);
    public static Taggable deeplyFulfilledParentModel;
    public static Taggable deeplyFulfilledParentModel_1;
    public static Taggable deeplyFulfilledParentModel_2;
    public static String deeplyFulfilledDiagramType;
    public static Map<String, String> deeplyFulfilledIdValue = new HashMap<>();
    public static Map<String, String> deeplyFulfilledIdValue_1 = new HashMap<>();
    public static Map<String, String> deeplyFulfilledIdValue_2 = new HashMap<>();
    public static MetaData deeplyFulfilledMetaData;
    public static Collection<Map<String, String>> deeplyFulfilledIdValueCollection = new ArrayList<>();
    public static Collection<Taggable> deeplyFulfilledParentModelCollection = new ArrayList<>();
    public static Collection<String> deeplyFulfilledDiagramTypesCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.architectural.Model> deeplyFulfilledArchitectureModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.bpm.Model> deeplyFulfilledBpmModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.er.Model> deeplyFulfilledERModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.flowchart.Model> deeplyFulfilledFlowchartModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.gantt.Model> deeplyFulfilledGanttModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.sequence.Model> deeplyFulfilledSequenceModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.classes.Model> deeplyFulfilledClassesModelCollection = new ArrayList<>();
    public static Collection<com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship> deeplyFulfilledArchitectureRelationshipCollection = new ArrayList<>();
    public static Collection<CompositeElement<ElementKind>> deeplyFulfilledFeaturesCollection = new ArrayList<>();
    public static com.araguacaima.open_archi.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel;
    public static com.araguacaima.open_archi.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel;
    public static com.araguacaima.open_archi.persistence.diagrams.er.Model deeplyFulfilledERModel;
    public static com.araguacaima.open_archi.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel;
    public static com.araguacaima.open_archi.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel;
    public static com.araguacaima.open_archi.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel;
    public static com.araguacaima.open_archi.persistence.diagrams.classes.Model deeplyFulfilledClassesModel;
    public static com.araguacaima.open_archi.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.er.Model deeplyFulfilledERModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.classes.Model deeplyFulfilledClassesModel_1;
    public static com.araguacaima.open_archi.persistence.diagrams.architectural.Model deeplyFulfilledArchitectureModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.bpm.Model deeplyFulfilledBpmModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.er.Model deeplyFulfilledERModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.flowchart.Model deeplyFulfilledFlowchartModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.gantt.Model deeplyFulfilledGanttModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.sequence.Model deeplyFulfilledSequenceModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.classes.Model deeplyFulfilledClassesModel_2;
    public static com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship deeplyFulfilledArchitectureRelationship;
    public static com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship deeplyFulfilledArchitectureRelationship_1;
    public static com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship deeplyFulfilledArchitectureRelationship_2;
    public static CompositeElement<ElementKind> deeplyFulfilledFeature;
    public static CompositeElement<ElementKind> deeplyFulfilledFeature_1;
    public static CompositeElement<ElementKind> deeplyFulfilledFeature_2;
    public static Set<Class<? extends Taggable>> modelsClasses;
    public static Reflections diagramsReflections;
    private static Logger log = LoggerFactory.getLogger(Commons.class);
    public static final ExceptionHandler exceptionHandler = new ExceptionHandlerImpl(Exception.class) {
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

    static {
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

        String DIAGRAMS_PACKAGES = "com.araguacaima.open_archi.persistence.diagrams";
        diagramsReflections = new Reflections(DIAGRAMS_PACKAGES, Taggable.class.getClassLoader());
        modelsClasses = diagramsReflections.getSubTypesOf(Taggable.class);
        CollectionUtils.filter(modelsClasses, clazz -> clazz.getSuperclass().equals(ModelElement.class) && !Modifier.isAbstract(clazz.getModifiers()));

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

        JPAEntityManagerUtils.getEntityManager();
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return engine.render(buildModelAndView(model, templatePath));
    }

    public static void appendAccountInfoToContext(Request req, Response res, BeanBuilder bean) {
        final SparkWebContext ctx = new SparkWebContext(req, res);
        Account account = (Account) ctx.getSessionAttribute("account");
        if (account == null) {
            bean.avatar(null).name(null).email(null).authorized(false);
        } else {
            String name = account.getLogin();
            String email = account.getEmail();
            Avatar accountAvatar = account.getAvatar();
            if (accountAvatar != null) {
                String avatar = accountAvatar.getUrl();
                bean.avatar(avatar);
            }
            bean.name(name);
            bean.email(email);
            bean.authorized(true);
        }
    }

    public static List<CommonProfile> getProfiles(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
        return manager.getAll(true);
    }

    public static void store(Request req, Response res) throws IOException {
        List<CommonProfile> profiles = getProfiles(req, res);
        CommonProfile profile = IterableUtils.find(profiles, object -> clients.contains(object.getClientName()));
        if (profile != null) {
            Account account;
            String email = profile.getEmail();
            Map<String, Object> params = new HashMap<>();
            params.put(Account.PARAM_EMAIL, email);
            account = JPAEntityManagerUtils.findByQuery(Account.class, Account.FIND_BY_EMAIL, params);

            if (account == null) {
                account = AccountWrapper.toAccount(profile);
                JPAEntityManagerUtils.persist(account.getAvatar());
                JPAEntityManagerUtils.persist(account);
            }

            Set<Role> accountRoles = account.getRoles();
            final Set<Role> roles = accountRoles;

            Set<String> profileRoles = profile.getRoles();
            if (CollectionUtils.isNotEmpty(profileRoles)) {
                profileRoles.forEach(role -> {
                    Map<String, Object> roleParams = new HashMap<>();
                    roleParams.put(Role.PARAM_NAME, role);
                    Role role_ = JPAEntityManagerUtils.findByQuery(Role.class, Role.FIND_BY_NAME, roleParams);
                    if (role_ == null) {
                        Role newRole = RolesWrapper.buildRole(role);
                        JPAEntityManagerUtils.persist(newRole);
                        roles.add(newRole);
                    }
                });
            } else {
                fixRole(accountRoles, roles, "delete:model");
                fixRole(accountRoles, roles, "write:model");
                fixRole(accountRoles, roles, "read:models");
                fixRole(accountRoles, roles, "delete:catalog");
                fixRole(accountRoles, roles, "write:catalog");
                fixRole(accountRoles, roles, "read:catalogs");
                fixRole(accountRoles, roles, "delete:palette");
                fixRole(accountRoles, roles, "read:palette");
                fixRole(accountRoles, roles, "write:palettes");
            }

            profile.addRoles(RolesWrapper.fromRoles(accountRoles));

            JPAEntityManagerUtils.merge(account);

            Session session = req.session(true);
            session.attribute("account", account);
        }
    }

    private static void fixRole(Set<Role> accountRoles, Set<Role> roles, String roleName) {
        Map<String, Object> roleParams = new HashMap<>();
        Role roleWriteCatalog = RolesWrapper.buildRole(roleName);
        roleParams.put(Role.PARAM_NAME, roleWriteCatalog.getName());
        JPAEntityManagerUtils.begin();
        Role role_ = JPAEntityManagerUtils.findByQuery(Role.class, Role.FIND_BY_NAME, roleParams);
        if (role_ == null) {
            JPAEntityManagerUtils.persist(roleWriteCatalog, false);
            roles.add(roleWriteCatalog);
        } else {
            Role innerRole = IterableUtils.find(accountRoles, role -> role.getName().equals(roleWriteCatalog.getName()));
            if (innerRole == null) {
                roles.add(role_);
            }
        }
        JPAEntityManagerUtils.commit();
    }

    public static String throwError(Response response, Throwable ex) {
        response.status(HTTP_BAD_REQUEST);
        response.type(JSON_CONTENT_TYPE);
        return ex.getMessage();
    }

    public static String getContentType(Request request) {
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

    public static String renderContent(String htmlFile) {
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

    public static List getListIdName(String diagramNames) throws IOException {
        List diagramNamesList = jsonUtils.fromJSON(diagramNames, List.class);
        CollectionUtils.filter(diagramNamesList, (Predicate<Map<String, String>>) map -> {
            String className = map.get("clazz");
            Class<?> clazz;
            try {
                if (StringUtils.isNotBlank(className)) {
                    clazz = Class.forName(className);
                    boolean assignableFrom = DiagramableElement.class.isAssignableFrom(clazz);
                    if (assignableFrom) {
                        map.put("type", ((DiagramableElement) clazz.newInstance()).getKind().name());
                        map.remove("clazz");
                    }
                    return assignableFrom;
                } else {
                    return true;
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        });
        return diagramNamesList;
    }

    public static Map<HttpMethod, Map<InputOutput, Object>> setOptionsOutputStructure(
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

    public static String getOptions(Request request, Response response, Map<HttpMethod, Map<InputOutput, Object>> object) throws IOException {
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

    public static Object getList(Request request, Response response, String query, Map<String, Object> params, Class type) throws IOException, URISyntaxException {
        return getList(request, response, query, params, type, null);
    }

    public static Object getList(Request request, Response response, String query, Map<String, Object> params, Class type, String contentType) throws IOException, URISyntaxException {
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

    public static Object getList(Request request, Response response, Collection models) throws IOException, URISyntaxException {
        return getList(request, response, models, null);
    }

    public static Object getList(Request request, Response response, Collection models, String contentType) throws IOException, URISyntaxException {
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

    public static String getElement(Request request, Response response, String query, Map<String, Object> params, Class<MetaData> type) throws IOException, URISyntaxException {
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

    public static Object filter(String query, String json) throws IOException, URISyntaxException {

        if (query == null) {
            return json;
        } else {
            return RsqlJsonFilter.rsql(query, json);
        }
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public static TemplateViewRoute buildRoute(Object bean, String path) {
        return (request, response) -> buildModelAndView(bean, path);
    }

    public static ModelAndView buildModelAndView(Object bean, String path) {
        Map map;
        if (Map.class.isAssignableFrom(bean.getClass())) {
            map = (Map) bean;
        } else {
            map = new BeanMap(bean);
        }
        return new ModelAndView(map, path);
    }

    public enum InputOutput {
        input,
        output
    }
}
