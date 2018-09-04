package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.commons.IdName;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementRole;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementShape;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.DBUtil;
import spark.RouteGroup;
import spark.route.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Catalogs implements RouteGroup {
    @Override
    public void addRoutes() {

        get("/catalogs/element-types/:elementTypeId/shape", (request, response) -> {
            Map<String, Object> params = new HashMap<>();
            ElementKind type = (ElementKind) enumsUtils.getEnum(ElementKind.class, request.params(":elementTypeId"));
            params.put("type", type);
            ElementShape elementShape = JPAEntityManagerUtils.findByQuery(ElementShape.class, ElementShape.GET_ELEMENT_SHAPE_BY_TYPE, params);
            return jsonUtils.toJSON(elementShape);
        });
        put("/catalogs/element-types/:elementTypeId/shape", (request, response) -> {
            try {
                ElementShape elementShape = jsonUtils.fromJSON(request.body(), ElementShape.class);
                if (elementShape == null) {
                    throw new Exception("Invalid kind of elementShape");
                }
                Map<String, Object> params = new HashMap<>();
                ElementKind type = (ElementKind) enumsUtils.getEnum(ElementKind.class, request.params(":elementTypeId"));
                params.put("type", type);
                ElementShape elementShape1 = JPAEntityManagerUtils.findByQuery(ElementShape.class, ElementShape.GET_ELEMENT_SHAPE_BY_TYPE, params);
                if (elementShape1 == null) {
                    elementShape.setType(type);
                    DBUtil.populate(elementShape);
                } else {
                    elementShape1.override(elementShape, false, "");
                    DBUtil.update(elementShape1);
                }
                response.status(HTTP_CREATED);
                response.header("Location", request.pathInfo() + "/" + elementShape.getType());
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
        get("/catalogs/diagram-names", (request, response) -> OpenArchi.getItemNames(request, response, Item.GET_ALL_DIAGRAM_NAMES));
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
        get("/catalogs/prototype-names", (request, response) -> OpenArchi.getItemNames(request, response, Item.GET_ALL_PROTOTYPE_NAMES));
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
        post("/catalogs/element-roles", (request, response) -> {
            try {
                String body = request.body();
                ElementRole model;
                try {
                    model = jsonUtils.fromJSON(body, ElementRole.class);
                } catch (Throwable ignored) {
                    throw new Exception("Invalid element role of '" + body + "'");
                }
                DBUtil.populate(model, false);
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                response.header("Location", request.pathInfo() + "/" + model.getId());
                return EMPTY_RESPONSE;
            } catch (Throwable ex) {
                return throwError(response, ex);
            }
        });
        get("/catalogs/element-roles", (request, response) -> {
            String roleNames = (String) getList(request, response, ElementRole.GET_ALL_ROLES, null, ElementRole.class);
            List diagramNamesList = getListIdName(roleNames);
            return getList(request, response, diagramNamesList);

        });
    }
}
