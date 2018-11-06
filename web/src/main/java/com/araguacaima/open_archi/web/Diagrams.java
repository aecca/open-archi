package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.diagrams.architectural.*;
import com.araguacaima.open_archi.persistence.diagrams.architectural.System;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import org.pac4j.sparkjava.SparkWebContext;
import spark.RouteGroup;
import spark.route.HttpMethod;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Diagrams implements RouteGroup {

    public static final String PATH = "/diagrams";

    private Map<String, Object> map = new HashMap<>();

    @Override
    public void addRoutes() {
        options("/architectures", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<Commons.InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledArchitectureModelCollection, deeplyFulfilledArchitectureModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/architectures", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/architectures", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return throwError(response, ex);
            }
        });
        patch("/architectures/:uuid", (request, response) -> {
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
        options("/architectures/:uuid/relationships", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledArchitectureRelationshipCollection, deeplyFulfilledArchitectureRelationship, HttpMethod.get, HttpMethod.put);
            return getOptions(request, response, output);
        });
        get("/architectures/:uuid/relationships", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Model.GET_ALL_RELATIONSHIPS, params, Collection.class);
        });
        put("/architectures/:uuid/relationships", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + feature.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/architectures/:uuid/consumers", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.put);
            return getOptions(request, response, output);
        });
        get("/architectures/:uuid/consumers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Model.GET_ALL_CONSUMERS_FOR_MODEL, params, Collection.class);
        });
        post("/architectures/:uuid/consumers", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + feature.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/:uuid/consumers/:cuuid", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            String id = request.params(":uuid");
            String cid = request.params(":cuuid");
            params.put("id", id);
            params.put("cid", cid);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Model.GET_CONSUMER_FOR_MODEL, params, null);
        });
        put("/architectures/:uuid/consumers/:cuuid", (request, response) -> {
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
        patch("/architectures/:uuid/consumers/:cuuid", (request, response) -> {
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
        get("/architectures/:uuid/layers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Model.GET_ALL_LAYERS_FROM_MODEL, params, Collection.class);
        });
        post("/architectures/:uuid/layers", (request, response) -> {
            try {
                String body = request.body();
                Map<String, Object> incomingModel = (Map<String, Object>) jsonUtils.fromJSON(body, Map.class);
                Object kind = incomingModel.get("kind");
                Layer layer;
                try {
                    layer = extractTaggable(body, kind);
                } catch (Throwable t) {
                    throw new Exception("Invalid kind of layer");
                }
                String modelId = request.params(":uuid");
                Model model = JPAEntityManagerUtils.find(Model.class, modelId);
                if (model == null) {
                    throw new EntityNotFoundException("There is no model with identifer of '" + modelId + "' to which associate the incoming layer");
                } else {
                    map.put("OriginType", model.getKind());
                    map.put("DestinationType", layer.getKind());
                    layer.validateAsociation(map);
                }

                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);

                Map<String, Object> params = new HashMap<>();
                params.put("name", layer.getName());
                params.put("kind", layer.getKind());
                Item storedLayer = JPAEntityManagerUtils.findByQuery(Item.class, Item.GET_ITEMS_BY_NAME_AND_KIND, params);

                Set<Layer> modelLayers = model.getLayers();
                if (storedLayer == null) {
                    map.put("Parent", model);
                    layer.validateCreation(map);
                    DBUtil.populate(layer);
                    modelLayers.add(layer);
                    JPAEntityManagerUtils.merge(model);
                } else {
                    if (!Layer.class.isAssignableFrom(storedLayer.getClass())) {
                        throw new EntityError("Provided body does not corresponds with a layer");
                    } else {
                        ((Layer) storedLayer).override(layer, true, null, null);
                        layer.validateReplacement(map);
                        DBUtil.replace(layer);
                    }
                }
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + layer.getId());
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException enfe) {
                response.status(HTTP_NOT_FOUND);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(MessagesWrapper.fromExceptionToMessages(enfe, HTTP_NOT_FOUND));
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/layers", (request, response) -> getList(request, response, Layer.GET_ALL_LAYERS, null, null));
        post("/architectures/layers", (request, response) -> {
            try {
                String body = request.body();
                Map<String, Object> incomingModel = (Map<String, Object>) jsonUtils.fromJSON(body, Map.class);
                Object kind = incomingModel.get("kind");
                Layer layer;
                try {
                    layer = extractTaggable(body, kind);
                } catch (Throwable t) {
                    throw new Exception("Invalid kind of layer");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                layer.validateCreation(map);
                DBUtil.populate(layer);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + layer.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/layers/:luuid", (request, response) -> {
            try {
                String id = request.params(":luuid");
                Layer layer = JPAEntityManagerUtils.find(Layer.class, id);
                if (layer != null) {
                    layer.validateRequest();
                }
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(layer);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        patch("/architectures/layers/:luuid", (request, response) -> {
            try {
                Layer layer = jsonUtils.fromJSON(request.body(), Layer.class);
                if (layer == null) {
                    throw new Exception("Invalid kind of layer");
                }
                String id = request.params(":luuid");
                layer.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                layer.validateModification(map);
                DBUtil.update(layer);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        put("/architectures/layers/:luuid", (request, response) -> {
            try {
                Layer layer = jsonUtils.fromJSON(request.body(), Layer.class);
                if (layer == null) {
                    throw new Exception("Invalid kind of layer");
                }
                String id = request.params(":luuid");
                layer.setId(id);
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                layer.validateReplacement(map);
                DBUtil.replace(layer);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/layers/:luuid/systems", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":luuid"));
            return getList(request, response, Layer.GET_ALL_SYSTEMS_FROM_LAYER, params, Collection.class);
        });
        post("/architectures/layers/:luuid/systems", (request, response) -> {
            try {
                System system = jsonUtils.fromJSON(request.body(), System.class);
                if (system == null) {
                    throw new Exception("Invalid kind of system");
                }
                String id = request.params(":luuid");
                String systemId = system.getId();
                final SparkWebContext ctx = new SparkWebContext(request, response);

                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                system.validateCreation(map);
                Layer layer = JPAEntityManagerUtils.find(Layer.class, id);
                DBUtil.populate(system, systemId == null);
                layer.getSystems().add(system);
                DBUtil.update(layer);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        delete("/architectures/layers/:luuid/systems/:suuid", (request, response) -> {
            try {
                String luuid = request.params(":luuid");
                String suuid = request.params(":suuid");
                Layer layer = JPAEntityManagerUtils.find(Layer.class, luuid);
                if (layer == null) {
                    throw new EntityNotFoundException("Layer with id of '" + luuid + "' does nos exists");
                }
                Set<System> systems = layer.getSystems();
                System system = null;
                for (System system_ : systems) {
                    if (system_.getId().equals(suuid)) {
                        system = system_;
                    }
                }
                if (system == null) {
                    throw new EntityNotFoundException("System with id of '" + suuid + "' is not found on Layer[" + luuid + "]");
                }
                systems.remove(system);
                DBUtil.update(layer);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                int status = HTTP_NOT_FOUND;
                response.status(status);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(MessagesWrapper.fromExceptionToMessages(ex, status));
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/layers/:suuid/containers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, Layer.GET_ALL_CONTAINERS_FROM_LAYER, params, Collection.class);
        });
        post("/architectures/layers/:suuid/containers", (request, response) -> {
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
                Layer layer = JPAEntityManagerUtils.find(Layer.class, id);
                DBUtil.populate(container, containerId == null);
                layer.getContainers().add(container);
                DBUtil.update(layer);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        delete("/architectures/layers/:luuid/containers/:cuuid", (request, response) -> {
            try {
                String luuid = request.params(":luuid");
                String cuuid = request.params(":cuuid");
                Layer layer = JPAEntityManagerUtils.find(Layer.class, luuid);
                if (layer == null) {
                    throw new EntityNotFoundException("Layer with id of '" + luuid + "' does nos exists");
                }
                Set<Container> containers = layer.getContainers();
                Container container = null;
                for (Container container_ : containers) {
                    if (container_.getId().equals(cuuid)) {
                        container = container_;
                        break;
                    }
                }
                if (container == null) {
                    throw new EntityNotFoundException("Container with id of '" + cuuid + "' is not found on Layer[" + luuid + "]");
                }
                containers.remove(container);
                JPAEntityManagerUtils.merge(layer);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                int status = HTTP_NOT_FOUND;
                response.status(status);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(MessagesWrapper.fromExceptionToMessages(ex, status));
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/layers/:suuid/components", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, Layer.GET_ALL_COMPONENTS_FROM_LAYER, params, Collection.class);
        });
        post("/architectures/layers/:suuid/components", (request, response) -> {
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
                Layer layer = JPAEntityManagerUtils.find(Layer.class, id);
                DBUtil.populate(component, componentId == null);
                layer.getComponents().add(component);
                DBUtil.update(layer);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        delete("/architectures/layers/:luuid/components/:suuid", (request, response) -> {
            try {
                String luuid = request.params(":luuid");
                String suuid = request.params(":suuid");
                Layer layer = JPAEntityManagerUtils.find(Layer.class, luuid);
                if (layer == null) {
                    throw new EntityNotFoundException("Layer with id of '" + luuid + "' does nos exists");
                }
                Set<Component> components = layer.getComponents();
                Component component = null;
                for (Component component_ : components) {
                    if (component_.getId().equals(suuid)) {
                        component = component_;
                    }
                }
                if (component == null) {
                    throw new EntityNotFoundException("Component with id of '" + suuid + "' is not found on Layer[" + luuid + "]");
                }
                components.remove(component);
                DBUtil.update(layer);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                int status = HTTP_NOT_FOUND;
                response.status(status);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(MessagesWrapper.fromExceptionToMessages(ex, status));
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/:uuid/systems", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Model.GET_ALL_SYSTEMS_FROM_MODEL, params, Collection.class);
        });
        get("/architectures/systems", (request, response) -> getList(request, response, System.GET_ALL_SYSTEMS, null, null));
        post("/architectures/systems", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/systems/:suuid", (request, response) -> {
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
        patch("/architectures/systems/:suuid", (request, response) -> {
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
        put("/architectures/systems/:suuid", (request, response) -> {
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
        get("/architectures/systems/:suuid/systems", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_SYSTEMS_FROM_SYSTEM, params, Collection.class);
        });
        post("/architectures/systems/:suuid/systems", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/systems/:suuid/containers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_CONTAINERS_FROM_SYSTEM, params, Collection.class);
        });
        post("/architectures/systems/:suuid/containers", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/systems/:suuid/components", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_COMPONENTS_FROM_SYSTEM, params, Collection.class);
        });
        post("/architectures/systems/:suuid/components", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/containers", (request, response) -> getList(request, response, Container.GET_ALL_CONTAINERS, null, null));
        post("/architectures/containers", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/containers/:cuuid", (request, response) -> {
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
        patch("/architectures/containers/:cuuid", (request, response) -> {
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
        put("/architectures/containers/:cuuid", (request, response) -> {
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
        get("/architectures/containers/:cuuid/components", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":cuuid"));
            return getList(request, response, Container.GET_ALL_COMPONENTS_FROM_CONTAINER, params, Collection.class);
        });
        post("/architectures/containers/:cuuid/components", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/components", (request, response) -> getList(request, response, Component.GET_ALL_COMPONENTS, null, null));
        post("/architectures/components", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + component.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/components/:cuuid", (request, response) -> {
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
        patch("/architectures/components/:cuuid", (request, response) -> {
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
        put("/architectures/components/:cuuid", (request, response) -> {
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
        post("/architectures/:uuid/systems", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + system.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/:uuid/systems/:suuid", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            String id = request.params(":uuid");
            String sid = request.params(":suuid");
            params.put("id", id);
            params.put("sid", sid);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Model.GET_SYSTEM, params, null);
        });
        put("/architectures/:uuid/systems/:suuid", (request, response) -> {
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
        patch("/architectures/:uuid/systems/:suuid", (request, response) -> {
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
        get("/architectures/:uuid/systems/:suuid/containers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":suuid"));
            return getList(request, response, System.GET_ALL_CONTAINERS_FROM_SYSTEM, params, Collection.class);
        });
        post("/architectures/:uuid/systems/:suuid/containers", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + container.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/architectures/:uuid/systems/:suuid/containers/:cuuid", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            String id = request.params(":suuid");
            String cid = request.params(":cuuid");
            params.put("id", id);
            params.put("cid", cid);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, System.GET_CONTAINER, params, null);
        });
        put("/architectures/:uuid/systems/:suuid/containers/:cuuid", (request, response) -> {
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
        patch("/architectures/:uuid/systems/:suuid/containers/:cuuid", (request, response) -> {
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
        options("/bpms", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledBpmModelCollection, deeplyFulfilledBpmModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/bpms", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/bpms", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/ers", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledERModelCollection, deeplyFulfilledERModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/ers", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/ers", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/flowcharts", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledFlowchartModelCollection, deeplyFulfilledFlowchartModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/flowcharts", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/flowcharts", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/gantts", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledGanttModelCollection, deeplyFulfilledGanttModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/gantts", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/gantts", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/sequences", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledSequenceModelCollection, deeplyFulfilledSequenceModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/sequences", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/sequences", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/classes", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledClassesModelCollection, deeplyFulfilledClassesModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/classes", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("modelType", com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
            response.type(JSON_CONTENT_TYPE);
            return getList(request, response, Item.GET_MODELS_BY_TYPE, params, null);
        });
        post("/classes", (request, response) -> {
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
                response.header("Location", request.pathInfo() + Commons.SEPARATOR_PATH + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
    }
}
