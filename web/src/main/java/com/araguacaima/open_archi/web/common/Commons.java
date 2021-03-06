package com.araguacaima.open_archi.web.common;

import com.araguacaima.commons.utils.EnumsUtils;
import com.araguacaima.commons.utils.JsonUtils;
import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.controller.ModelsController;
import com.araguacaima.open_archi.persistence.diagrams.architectural.GroupStaticElement;
import com.araguacaima.open_archi.persistence.diagrams.architectural.LeafStaticElement;
import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.Role;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.MessagesWrapper;
import com.araguacaima.open_archi.web.Server;
import com.araguacaima.open_archi.web.filter.SessionFilter;
import com.araguacaima.open_archi.web.wrapper.AccountWrapper;
import com.araguacaima.open_archi.web.wrapper.JsonPathRsqlVisitor;
import com.araguacaima.open_archi.web.wrapper.RolesWrapper;
import com.araguacaima.open_archi.web.wrapper.RsqlJsonFilter;
import com.araguacaima.orpheusdb.utils.OrpheusDbJPAEntityManagerUtils;
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
import java.lang.reflect.Field;
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
import static java.net.HttpURLConnection.*;
import static org.pac4j.core.context.HttpConstants.HTML_CONTENT_TYPE;

public class Commons {

    public static final String DELETE_MODEL_ROLE = "delete:model";
    public static final String WRITE_MODEL_ROLE = "write:model";
    public static final String READ_MODELS_ROLE = "read:models";
    public static final String DELETE_CATALOG_ROLE = "delete:catalog";
    public static final String WRITE_CATALOG_ROLE = "write:catalog";
    public static final String READ_CATALOGS_ROLE = "read:catalogs";
    public static final String DELETE_PALETTE_ROLE = "delete:palette";
    public static final String WRITE_PALETTE_ROLE = "write:palette";
    public static final String READ_PALETTES_ROLE = "read:palettes";
    public static final String ADMIN_ROLE = "admin";
    public static final List<String> ALL_ROLES = Arrays.asList(
            DELETE_MODEL_ROLE, WRITE_MODEL_ROLE, READ_MODELS_ROLE,
            DELETE_CATALOG_ROLE, WRITE_CATALOG_ROLE, READ_CATALOGS_ROLE,
            DELETE_PALETTE_ROLE, WRITE_PALETTE_ROLE, READ_PALETTES_ROLE, ADMIN_ROLE);
    public static final String clients = "Google2Client";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String EMPTY_RESPONSE = StringUtils.EMPTY;
    public static final JsonUtils jsonUtils = new JsonUtils();
    public static final String OPEN_ARCHI = "Open-Archi";
    public static final String OPEN_ARCHI_DESKTOP = "Open-Archi-Desktop";
    public static final EnumsUtils enumsUtils = new EnumsUtils();
    public static final String DEFAULT_PATH = "/";
    public static final String EMPTY_PATH = "";
    public static final String SEPARATOR_PATH = "/";
    public static final String REJECTED_SCOPES = "rejected_scopes";
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
    public static Set<Class<? extends Taggable>> modelsClasses;
    public static Set<Class<? extends Taggable>> innerGroupElementClasses;
    public static Set<Class<? extends Taggable>> innerSingleElementClasses;
    public static Reflections diagramsReflections;
    private static Logger log = LoggerFactory.getLogger(Commons.class);
    public static Filter genericFilter = new SessionFilter();
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
        innerGroupElementClasses = diagramsReflections.getSubTypesOf(Taggable.class);
        innerSingleElementClasses = diagramsReflections.getSubTypesOf(Taggable.class);
        CollectionUtils.filter(modelsClasses, clazz -> clazz.getSuperclass().equals(ModelElement.class) && !Modifier.isAbstract(clazz.getModifiers()));
        CollectionUtils.filter(innerGroupElementClasses, clazz -> clazz.getSuperclass().equals(GroupStaticElement.class) && !Modifier.isAbstract(clazz.getModifiers()));
        CollectionUtils.filter(innerSingleElementClasses, clazz -> clazz.getSuperclass().equals(LeafStaticElement.class) && !Modifier.isAbstract(clazz.getModifiers()));

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

    }

    public static String render(Map<String, Object> model, String templatePath) {
        return engine.render(buildModelAndView(model, templatePath));
    }

    public static List<CommonProfile> getProfiles(SparkWebContext context) {
        final ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
        return manager.getAll(true);
    }

    public static List<CommonProfile> getProfiles(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
        return manager.getAll(true);
    }

    public static CommonProfile findAndFulfillProfile(SparkWebContext context) {
        CommonProfile profile = IterableUtils.find(getProfiles(context), object -> clients.contains(object.getClientName()));
        if (profile != null) {
            Account account = (Account) context.getSessionAttribute("account");
            if (account != null) {
                account.getRoles().forEach(role -> profile.addRole(role.getName()));
            } else {
                String email = profile.getEmail();
                Map<String, Object> params = new HashMap<>();
                params.put(Account.PARAM_EMAIL, email);
                account = OrpheusDbJPAEntityManagerUtils.findByQuery(Account.class, Account.FIND_BY_EMAIL_AND_ENABLED, params);
                if (account != null) {
                    Set<Role> accountRoles = account.getRoles();
                    profile.addRoles(RolesWrapper.fromRoles(accountRoles));
                }
            }
        }
        return profile;
    }

    public static CommonProfile findProfile(SparkWebContext context) {
        return findProfile(getProfiles(context));
    }

    public static CommonProfile findProfile(Request req, Response res) {
        return findProfile(new SparkWebContext(req, res));
    }

    public static CommonProfile findProfile(List<CommonProfile> profiles) {
        return IterableUtils.find(profiles, object -> clients.contains(object.getClientName()));
    }

    public static void store(Request req, Response res) throws IOException {
        SparkWebContext context = new SparkWebContext(req, res);
        CommonProfile profile = findAndFulfillProfile(context);
        if (profile != null) {
            Account account;
            String email = profile.getEmail();
            Map<String, Object> params = new HashMap<>();
            params.put(Account.PARAM_EMAIL, email);
            account = OrpheusDbJPAEntityManagerUtils.findByQuery(Account.class, Account.FIND_BY_EMAIL, params);

            if (account == null) {
                account = AccountWrapper.toAccount(profile);
                OrpheusDbJPAEntityManagerUtils.persist(account.getAvatar());
                OrpheusDbJPAEntityManagerUtils.persist(account);
            }
            SessionFilter.SessionMap map = SessionFilter.map.get(email);
            if (map == null) {
                map = new SessionFilter.SessionMap(req.session(), true);
                SessionFilter.map.put(email, map);
            }
            if (account.isEnabled()) {

                Set<Role> accountRoles = account.getRoles();
                final Set<Role> roles = accountRoles;

                Set<String> profileRoles = profile.getRoles();
                if (CollectionUtils.isNotEmpty(profileRoles)) {
                    profileRoles.forEach(role -> {
                        Map<String, Object> roleParams = new HashMap<>();
                        roleParams.put(Role.PARAM_NAME, role);
                        Role role_ = OrpheusDbJPAEntityManagerUtils.findByQuery(Role.class, Role.FIND_BY_NAME, roleParams);
                        if (role_ == null) {
                            Role newRole = RolesWrapper.buildRole(role);
                            OrpheusDbJPAEntityManagerUtils.persist(newRole);
                            roles.add(newRole);
                        }
                    });
                } else {
                    Commons.ALL_ROLES.forEach(role -> fixRole(accountRoles, roles, role));
                }

                profile.addRoles(RolesWrapper.fromRoles(accountRoles));

                OrpheusDbJPAEntityManagerUtils.merge(account);

                context.setSessionAttribute("account", account);
            }
        }
    }

    private static void fixRole(Set<Role> accountRoles, Set<Role> roles, String roleName) {
        Map<String, Object> roleParams = new HashMap<>();
        Role roleWriteCatalog = RolesWrapper.buildRole(roleName);
        roleParams.put(Role.PARAM_NAME, roleWriteCatalog.getName());
        OrpheusDbJPAEntityManagerUtils.begin();
        Role role_ = OrpheusDbJPAEntityManagerUtils.findByQuery(Role.class, Role.FIND_BY_NAME, roleParams);
        if (role_ == null) {
            OrpheusDbJPAEntityManagerUtils.persist(roleWriteCatalog, false);
            roles.add(roleWriteCatalog);
        } else {
            Role innerRole = IterableUtils.find(accountRoles, role -> role.getName().equals(roleWriteCatalog.getName()));
            if (innerRole == null) {
                roles.add(role_);
            }
        }
        OrpheusDbJPAEntityManagerUtils.commit();
    }

    public static String throwError(Response response, Throwable ex) {
        response.status(HTTP_INTERNAL_ERROR);
        response.type(JSON_CONTENT_TYPE);
        try {
            return jsonUtils.toJSON(MessagesWrapper.fromExceptionToMessages(ex, HTTP_INTERNAL_ERROR));
        } catch (IOException e) {
            return ex.getMessage();
        }
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
        try {
            models = OrpheusDbJPAEntityManagerUtils.executeQuery(type == null ? Taggable.class : type, query, params);
        } catch (IllegalArgumentException ignored) {
            models = OrpheusDbJPAEntityManagerUtils.executeQuery(Object[].class, query, params);
        }
        return getList(request, response, models, contentType);

    }

    public static Object getList(Request request, Response response, Collection models) throws IOException, URISyntaxException {
        return getList(request, response, models, null);
    }

    public static Object getList(Request request, Response response, Collection objects, String contentType) throws IOException, URISyntaxException {
        response.status(HTTP_OK);
        contentType = StringUtils.defaultString(contentType, getContentType(request));
        response.header("Content-Type", contentType);
        String fieldsToInclude = request.queryParams("fieldsToInclude");
        String fieldsToExclude = request.queryParams("fieldsToExclude");
        String name = request.queryParams("usagesByName");
        if (StringUtils.isNotBlank(name)) {
            objects = ModelsController.findUsagesOf(name);
        }
        StringBuilder fields = new StringBuilder();
        if (StringUtils.isNotBlank(fieldsToInclude)) {
            fields.append(fieldsToInclude);
        }
        if (StringUtils.isNotBlank(fieldsToExclude)) {
            if (StringUtils.isNotBlank(fieldsToInclude)) {
                fields.append(",");
            }
            String[] splittedFieldsToExclude = fieldsToExclude.split(",");
            List<String> fieldsArray = new ArrayList<>();
            for (String aSplittedFieldsToExclude : splittedFieldsToExclude) {
                String fieldToExclude = aSplittedFieldsToExclude.trim();
                if (!fieldToExclude.startsWith("-")) {
                    fieldsArray.add("-" + fieldToExclude);
                } else {
                    fieldsArray.add(fieldToExclude);
                }
            }
            fields.append(StringUtils.join(fieldsArray, ","));
        }
        String filter = request.queryParams("query");
        return filter(filter, objects, fields.toString());

    }

    public static String getElement(Request request, Response response, String query, Map<String, Object> params, Class<MetaData> type) throws IOException, URISyntaxException {
        response.status(HTTP_OK);

        List<MetaData> element = OrpheusDbJPAEntityManagerUtils.executeQuery(type, query, params);
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

    public static Object filter(String query, Collection json, String filter) throws IOException, URISyntaxException {
        if (query == null) {
            return RsqlJsonFilter.rsql(JsonPathRsqlVisitor.GET_ALL_RESULTS, json, filter);
        } else {
            return RsqlJsonFilter.rsql(query, json, filter);
        }
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }


    public static TemplateViewRoute buildRoute(String path) {
        return (request, response) -> buildModelAndView(request, response, null, path);
    }

    public static TemplateViewRoute buildRoute(Object bean, String path) {
        return (request, response) -> buildModelAndView(request, response, bean, path);
    }

    public static ModelAndView buildModelAndView(Object bean, String path) {
        return buildModelAndView(null, null, bean, path);
    }

    public static ModelAndView buildModelAndView(Request request, Response response, Object bean, String path) {
        Map<Object, Object> map = new HashMap<>();
        if (bean != null) {
            if (Map.class.isAssignableFrom(bean.getClass())) {
                map.putAll((Map<Object, Object>) bean);
            } else {
                if (BeanBuilder.class.isAssignableFrom(bean.getClass())) {
                    ((BeanBuilder) bean).fixAccountInfo(request, response);
                }
                map.putAll(new BeanMap(bean));
                map.remove("class");
            }
        }
        final Map<String, Object> newMap = new HashMap<>();
        map.forEach((o, o2) -> {
            String key = o.toString();
            Object value = o2;
            try {
                if (value != null) {
                    Class<?> clazz = value.getClass();
                    if (reflectionUtils.isCollectionImplementation(clazz)) {
                        if (Collection.class.isAssignableFrom(clazz)) {
                            newMap.put(key, value);
                            newMap.put(key + "_", jsonUtils.toJSON(value));

                        } else if (Object[].class.isAssignableFrom(clazz)
                                || clazz.isArray()) {
                            newMap.put(key, value);
                            newMap.put(key + "_", jsonUtils.toJSON(value));
                        }
                    } else {
                        if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(clazz) == null) {
                            newMap.put(key, value);
                            newMap.put(key + "_", jsonUtils.toJSON(value));
                        } else {
                            newMap.put(key, value);
                        }
                    }
                } else {
                    newMap.put(key, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        while (newMap.values().remove(null)) ;
        return new ModelAndView(newMap, path);
    }

    public enum InputOutput {
        input,
        output
    }


    public static List<BeanBuilder> getDefaultNodeDataArray() {
        return new ArrayList<BeanBuilder>() {{
            add(new BeanBuilder().key("1").text("Alpha").color("lightblue"));
            add(new BeanBuilder().key("2").text("Beta").color("orange"));
            add(new BeanBuilder().key("3").text("Gamma").color("lightgreen").group("5"));
            add(new BeanBuilder().key("4").text("Delta").color("pink").group("5"));
            add(new BeanBuilder().key("5").text("Epsilon").color("green").isGroup(true));
        }};
    }


    public static List<BeanBuilder> getDefaultLinkDataArray() {
        return new ArrayList<BeanBuilder>() {{
            add(new BeanBuilder().from("1").to("2").color("blue"));
            add(new BeanBuilder().from("2").to("2"));
            add(new BeanBuilder().from("3").to("4").color("green"));
            add(new BeanBuilder().from("3").to("1").color("purple"));
        }};
    }

    public static <T> T extractTaggable(String body, Object kind) throws Exception {
        T model = null;
        for (Class<? extends Taggable> modelClass : CollectionUtils.union(CollectionUtils.union(modelsClasses, innerGroupElementClasses), innerSingleElementClasses)) {
            Field field = reflectionUtils.getField(modelClass, "kind");
            if (field != null) {
                field.setAccessible(true);
                Object obj = modelClass.newInstance();
                Object thisKind = field.get(obj);
                thisKind = enumsUtils.getStringValue((Enum) thisKind);
                if (kind.equals(thisKind)) {
                    model = jsonUtils.fromJSON(body, (Class<T>) modelClass);
                    break;
                }
            }
        }
        if (model == null) {
            throw new Exception("Invalid kind of model '" + kind + "'");
        }
        return model;
    }
}
