package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.commons.IdName;
import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.diagrams.architectural.Model;
import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.pac4j.sparkjava.SparkWebContext;
import spark.RouteGroup;
import spark.route.HttpMethod;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Models implements RouteGroup {

    public static final String PATH = "/models";

    @Override
    public void addRoutes() {
        options(Commons.DEFAULT_PATH, (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<Commons.InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        post(Commons.EMPTY_PATH, (request, response) -> {
            try {
                String body = request.body();
                Map<String, Object> incomingModel = (Map<String, Object>) jsonUtils.fromJSON(body, Map.class);
                Object kind = incomingModel.get("kind");
                Object id = incomingModel.get("id");
                Taggable model = extractTaggable(body, kind);
                final SparkWebContext ctx = new SparkWebContext(request, response);
                Map<String, Object> map = new HashMap<>();
                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                try {
                    model.validateCreation(map);
                } catch (EntityError e) {
                    response.status(HTTP_CONFLICT);
                    response.type(JSON_CONTENT_TYPE);
                    return jsonUtils.toJSON(MessagesWrapper.fromSpecificationMapToMessages(map));
                }
                DBUtil.populate(model, id == null);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return throwError(response, ex);
            }
        });
        get(Commons.EMPTY_PATH, (request, response) -> getList(request, response, Item.GET_ALL_MODELS, null, null));
        options("/:uuid", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModel, null, HttpMethod.get, null);
            return getOptions(request, response, output);
        });
        get("/:uuid", (request, response) -> {
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
        put("/:uuid", (request, response) -> {
            try {
                String body = request.body();
                Map<String, Object> incomingModel = (Map<String, Object>) jsonUtils.fromJSON(body, Map.class);
                Object kind = incomingModel.get("kind");
                String id = request.params(":uuid");
                Model model = extractTaggable(body, kind);
                model.setId(id);
                Map<String, Object> params = new HashMap<>();
                params.put("name", model.getName());
                params.put("kind", model.getKind());
                Map<String, Object> map = new HashMap<>();

                final SparkWebContext ctx = new SparkWebContext(request, response);
                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);


                Item storedModel = JPAEntityManagerUtils.findByQuery(Item.class, Item.GET_ITEMS_BY_NAME_AND_KIND, params);
                if (storedModel == null) {
                    map.put("Parent", model);
                    model.validateCreation(map);
                    DBUtil.populate(model);
                    response.status(HTTP_CREATED);
                    return EMPTY_RESPONSE;
                } else {
                    storedModel.override(model, true, null, null);
                    map.put("Parent", storedModel);
                    storedModel.validateReplacement(map);
                    DBUtil.replace(storedModel);
                    response.status(HTTP_OK);
                    return EMPTY_RESPONSE;
                }
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return throwError(response, ex);
            }
        });
        delete("/:uuid", (request, response) -> {
            try {
                String id = request.params(":uuid");
                DBUtil.delete(Taggable.class, id);
                response.status(HTTP_OK);
                return EMPTY_RESPONSE;
            } catch (EntityNotFoundException ex) {
                response.status(HTTP_NOT_FOUND);
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/:uuid/clone", (request, response) -> {
            try {
                String id = request.params(":uuid");
                String suffix = request.queryParams("suffix");
                Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                if (model != null) {
                    model.validateRequest();
                } else {
                    throw new Exception("Model with id of '" + id + "' not found");
                }
                Object clonedModel = model.getClass().newInstance();
                CompositeElement clonedFrom = ((Item) model).buildCompositeElement();
                if (DiagramableElement.class.isAssignableFrom(clonedModel.getClass()) || BaseEntity.class.isAssignableFrom(clonedModel.getClass())) {
                    Object[] overrideArgs = new Object[4];
                    overrideArgs[0] = model;
                    overrideArgs[1] = false;
                    overrideArgs[2] = suffix;
                    overrideArgs[3] = clonedFrom;
                    reflectionUtils.invokeMethod(clonedModel, "override", overrideArgs);
                } else {
                    throw new Exception("Invalid model");
                }
                Item clonedModelItem = (Item) clonedModel;
                clonedModelItem.setClonedFrom(clonedFrom);
                OpenArchi.fixCompositeFromItem(clonedModelItem);
                String name = clonedModelItem.getName();
                Map<String, Object> map = new HashMap<>();
                map.put("type", model.getClass());
                map.put("name", name);
                List<IdName> modelNames = JPAEntityManagerUtils.executeQuery(IdName.class, Item.GET_MODEL_NAMES_BY_NAME_AND_TYPE, map);
                if (CollectionUtils.isNotEmpty(modelNames)) {
                    Collections.sort(modelNames);
                    IdName lastFoundName = IterableUtils.get(modelNames, modelNames.size() - 1);
                    name = lastFoundName.getName() + " (1)";
                }
                clonedModelItem.setName(name);
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(clonedModel);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        put("/:uuid/image", (request, response) -> {
            try {
                Image image = jsonUtils.fromJSON(request.body(), Image.class);
                if (image == null) {
                    throw new Exception("Invalid kind of relationship");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);
                Map<String, Object> map = new HashMap<>();
                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                image.validateReplacement(map);
                DBUtil.populate(image);
                response.status(HTTP_CREATED);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + image.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        patch("/:uuid/status/:sid", (request, response) -> {
            try {
                Taggable model = jsonUtils.fromJSON(request.body(), Taggable.class);
                if (model == null) {
                    throw new Exception("Invalid kind of model");
                }
                String id = request.params(":uuid");
                model.setId(id);
                String sid = request.params(":sid");
                model.setStatus((Status) enumsUtils.getEnum(Status.class, sid));
                final SparkWebContext ctx = new SparkWebContext(request, response);
                Map<String, Object> map = new HashMap<>();
                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                model.validateReplacement(map);
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
        put("/:uuid/children", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            response.type(JSON_CONTENT_TYPE);
            return EMPTY_RESPONSE;
        });
        options("/:uuid/children", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.put);
            return getOptions(request, response, output);
        });
        get("/:uuid/children", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Item.GET_ALL_CHILDREN, params, Collection.class);
        });
        put("/:uuid/children", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            response.type(JSON_CONTENT_TYPE);
            return EMPTY_RESPONSE;
        });
        options("/:uuid/parent", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModel, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/:uuid/parent", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            response.type(JSON_CONTENT_TYPE);
            return EMPTY_RESPONSE;
        });
        post("/:uuid/parent", (request, response) -> {
            try {
                CompositeElement model;
                try {
                    model = jsonUtils.fromJSON(request.body(), CompositeElement.class);
                } catch (Throwable t) {
                    throw new Exception("Invalid kind of parent model info due: '" + t.getMessage() + "'");
                }
                DBUtil.persist(model);
                response.status(HTTP_CREATED);
                response.header("Location", request.pathInfo() + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/:uuid/meta-data", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(null, deeplyFulfilledMetaData, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        get("/:uuid/meta-data", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getElement(request, response, Item.GET_META_DATA, params, MetaData.class);
        });
        post("/:uuid/meta-data", (request, response) -> {
            try {
                MetaData metaData = jsonUtils.fromJSON(request.body(), MetaData.class);
                if (metaData == null) {
                    throw new Exception("Invalid metadata");
                }
                final SparkWebContext ctx = new SparkWebContext(request, response);
                Map<String, Object> map = new HashMap<>();
                Account account = (Account) ctx.getSessionAttribute("account");
                map.put("account", account);
                metaData.validateCreation(map);
                DBUtil.populate(metaData);
                response.status(HTTP_CREATED);
                response.header("Location", request.pathInfo() + request.params(":uuid") + "/meta-data");
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        options("/:uuid/features", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledFeaturesCollection, deeplyFulfilledFeature, HttpMethod.get, HttpMethod.put);
            return getOptions(request, response, output);
        });
        get("/:uuid/features", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            params.put("id", request.params(":uuid"));
            return getList(request, response, Element.GET_ALL_FEATURES, params, Collection.class);
        });
        put("/:uuid/features", (request, response) -> {
            try {

                CompositeElement feature = jsonUtils.fromJSON(request.body(), CompositeElement.class);
                if (feature == null) {
                    throw new Exception("Invalid kind of feature");
                }
                DBUtil.persist(feature);
                response.status(HTTP_CREATED);
                response.header("Location", request.pathInfo() + feature.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
    }

}
