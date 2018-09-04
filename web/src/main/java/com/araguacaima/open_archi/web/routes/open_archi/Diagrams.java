package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.diagrams.architectural.*;
import com.araguacaima.open_archi.persistence.diagrams.architectural.System;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.DBUtil;
import com.araguacaima.open_archi.web.common.Commons;
import org.pac4j.sparkjava.SparkWebContext;
import spark.RouteGroup;
import spark.route.HttpMethod;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Diagrams implements RouteGroup {
    private Map<String, Object> map = new HashMap<>();

    @Override
    public void addRoutes() {
        options("/diagrams/architectures", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<Commons.InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledArchitectureModelCollection, deeplyFulfilledArchitectureModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/diagrams/architectures", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Taggable.GET_MODELS_BY_TYPE, params, null);
        });
        post("/diagrams/architectures", (request, response) -> {
            try {
                Model model = jsonUtils.fromJSON(request.body(), Model.class);
                if (model == null) {
                    throw new Exception("Invalid model");
                }
                if (model.getKind() != ElementKind.ARCHITECTURE_MODEL) {
                    throw new Exception("Invalid kind of model '" + model.getKind() + "'. It should be '" + ElementKind.ARCHITECTURE_MODEL + "'");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
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
                Model model = jsonUtils.fromJSON(request.body(), Model.class);
                if (model == null) {
                    throw new Exception("Invalid kind of model");
                }
                String id = request.params(":uuid");
                model.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateModification(map);
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
            return getList(request, response, Model.GET_ALL_RELATIONSHIPS, params, Collection.class);
        });
        put("/diagrams/architectures/:uuid/relationships", (request, response) -> {
            try {
                com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship feature = jsonUtils.fromJSON(request.body(), com.araguacaima.open_archi.persistence.diagrams.architectural.Relationship.class);
                if (feature == null) {
                    throw new Exception("Invalid kind of relationship");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                feature.validateReplacement(map);
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
            return getList(request, response, Model.GET_ALL_CONSUMERS_FOR_MODEL, params, Collection.class);
        });
        post("/diagrams/architectures/:uuid/consumers", (request, response) -> {
            try {
                Consumer feature = jsonUtils.fromJSON(request.body(), Consumer.class);
                if (feature == null) {
                    throw new Exception("Invalid kind of relationship");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                feature.validateCreation(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateReplacement(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                consumer.validateModification(map);
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
        get("/diagrams/architectures/:uuid/systems", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Model.GET_ALL_SYSTEMS_FROM_MODEL, params, Collection.class);
        });
        get("/diagrams/architectures/systems", (request, response) -> getList(request, response, System.GET_ALL_SYSTEMS, null, null));
        post("/diagrams/architectures/systems", (request, response) -> {
            try {
                System system = jsonUtils.fromJSON(request.body(), System.class);
                if (system == null) {
                    throw new Exception("Invalid kind of system");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                system.validateCreation(map);
                DBUtil.populate(system);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/systems/:suuid", (request, response) -> {
            try {
                String id = request.params(":suuid");
                System system = JPAEntityManagerUtils.find(System.class, id);
                if (system != null) {
                    system.validateRequest();
                }
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(system);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        patch("/diagrams/architectures/systems/:suuid", (request, response) -> {
            try {
                System system = jsonUtils.fromJSON(request.body(), System.class);
                if (system == null) {
                    throw new Exception("Invalid kind of system");
                }
                String id = request.params(":suuid");
                system.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                system.validateModification(map);
                DBUtil.update(system);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        put("/diagrams/architectures/systems/:suuid", (request, response) -> {
            try {
                System system = jsonUtils.fromJSON(request.body(), System.class);
                if (system == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":suuid");
                system.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                system.validateReplacement(map);
                DBUtil.replace(system);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/systems/:suuid/systems", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_SYSTEMS_FROM_SYSTEM, params, Collection.class);
        });
        post("/diagrams/architectures/systems/:suuid/systems", (request, response) -> {
            try {
                System system = jsonUtils.fromJSON(request.body(), System.class);
                if (system == null) {
                    throw new Exception("Invalid kind of system");
                }
                String id = request.params(":suuid");
                String systemId = system.getId();
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                system.validateCreation(map);
                System system_ = JPAEntityManagerUtils.find(System.class, id);
                DBUtil.populate(system, systemId == null);
                system_.getSystems().add(system);
                DBUtil.update(system);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/systems/:suuid/containers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_CONTAINERS_FROM_SYSTEM, params, Collection.class);
        });
        post("/diagrams/architectures/systems/:suuid/containers", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":suuid");
                String containerId = container.getId();
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateCreation(map);
                System system = JPAEntityManagerUtils.find(System.class, id);
                DBUtil.populate(container, containerId == null);
                system.getContainers().add(container);
                DBUtil.update(system);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/systems/:suuid/components", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_COMPONENTS_FROM_SYSTEM, params, Collection.class);
        });
        post("/diagrams/architectures/systems/:suuid/components", (request, response) -> {
            try {
                Component component = jsonUtils.fromJSON(request.body(), Component.class);
                if (component == null) {
                    throw new Exception("Invalid kind of component");
                }
                String id = request.params(":suuid");
                String componentId = component.getId();
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                component.validateCreation(map);
                System system = JPAEntityManagerUtils.find(System.class, id);
                DBUtil.populate(component, componentId == null);
                system.getComponents().add(component);
                DBUtil.update(system);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/containers", (request, response) -> getList(request, response, Container.GET_ALL_CONTAINERS, null, null));
        post("/diagrams/architectures/containers", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateCreation(map);
                DBUtil.populate(container);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/containers/:cuuid", (request, response) -> {
            try {
                String id = request.params(":cuuid");
                Container container = JPAEntityManagerUtils.find(Container.class, id);
                if (container != null) {
                    container.validateRequest();
                }
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(container);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        patch("/diagrams/architectures/containers/:cuuid", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                container.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateModification(map);
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
        put("/diagrams/architectures/containers/:cuuid", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                container.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateReplacement(map);
                DBUtil.replace(container);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/containers/:cuuid/components", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":cuuid"));
            return getList(request, response, Container.GET_ALL_COMPONENTS_FROM_CONTAINER, params, Collection.class);
        });
        post("/diagrams/architectures/containers/:cuuid/components", (request, response) -> {
            try {
                Component component = jsonUtils.fromJSON(request.body(), Component.class);
                if (component == null) {
                    throw new Exception("Invalid kind of component");
                }
                String id = request.params(":cuuid");

                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                component.validateCreation(map);
                Container container = JPAEntityManagerUtils.find(Container.class, id);
                DBUtil.populate(component, true);
                container.getComponents().add(component);
                DBUtil.update(container);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/components", (request, response) -> getList(request, response, Component.GET_ALL_COMPONENTS, null, null));
        post("/diagrams/architectures/components", (request, response) -> {
            try {
                Component component = jsonUtils.fromJSON(request.body(), Component.class);
                if (component == null) {
                    throw new Exception("Invalid kind of relationship");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                component.validateCreation(map);
                DBUtil.populate(component);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/components/:cuuid", (request, response) -> {
            try {
                String id = request.params(":cuuid");
                Component component = JPAEntityManagerUtils.find(Component.class, id);
                if (component != null) {
                    component.validateRequest();
                }
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(component);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        patch("/diagrams/architectures/components/:cuuid", (request, response) -> {
            try {
                Component component = jsonUtils.fromJSON(request.body(), Component.class);
                if (component == null) {
                    throw new Exception("Invalid kind of component");
                }
                String id = request.params(":cuuid");
                component.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                component.validateModification(map);
                DBUtil.update(component);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        put("/diagrams/architectures/components/:cuuid", (request, response) -> {
            try {
                Component component = jsonUtils.fromJSON(request.body(), Component.class);
                if (component == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                component.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                component.validateReplacement(map);
                DBUtil.replace(component);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        post("/diagrams/architectures/:uuid/systems", (request, response) -> {
            try {
                System system = jsonUtils.fromJSON(request.body(), System.class);
                if (system == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":uuid");
                String systemId = system.getId();
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                system.validateCreation(map);
                Model model = JPAEntityManagerUtils.find(Model.class, id);
                DBUtil.populate(system, systemId == null);
                model.getSystems().add(system);
                DBUtil.update(model);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/:uuid/systems/:suuid", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            String id = request.params(":uuid");
            String sid = request.params(":suuid");
            params.put("id", id);
            params.put("sid", sid);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Model.GET_SYSTEM, params, null);
        });
        put("/diagrams/architectures/:uuid/systems/:suuid", (request, response) -> {
            try {
                Container model = jsonUtils.fromJSON(request.body(), Container.class);
                if (model == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                model.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateReplacement(map);
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
        patch("/diagrams/architectures/:uuid/systems/:suuid", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                container.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateModification(map);
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
        get("/diagrams/architectures/:uuid/systems/:suuid/containers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_CONTAINERS_FROM_SYSTEM, params, Collection.class);
        });
        post("/diagrams/architectures/:uuid/systems/:suuid/containers", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":suuid");
                String containerId = container.getId();
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateCreation(map);
                System system = JPAEntityManagerUtils.find(System.class, id);
                DBUtil.populate(container, containerId == null);
                system.getContainers().add(container);
                DBUtil.update(system);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/diagrams/architectures/:uuid/systems/:suuid/containers/:cuuid", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            String id = request.params(":suuid");
            String cid = request.params(":cuuid");
            params.put("id", id);
            params.put("cid", cid);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, System.GET_CONTAINER, params, null);
        });
        put("/diagrams/architectures/:uuid/systems/:suuid/containers/:cuuid", (request, response) -> {
            try {
                Container model = jsonUtils.fromJSON(request.body(), Container.class);
                if (model == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                model.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateReplacement(map);
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
        patch("/diagrams/architectures/:uuid/systems/:suuid/containers/:cuuid", (request, response) -> {
            try {
                Container container = jsonUtils.fromJSON(request.body(), Container.class);
                if (container == null) {
                    throw new Exception("Invalid kind of container");
                }
                String id = request.params(":cuuid");
                container.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                container.validateModification(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
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
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateCreation(map);
                DBUtil.populate(model);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
    }
}
