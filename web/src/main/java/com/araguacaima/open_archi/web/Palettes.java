package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.diagrams.architectural.Palette;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.web.common.Commons;
import spark.RouteGroup;
import spark.route.HttpMethod;

import java.util.Map;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static java.net.HttpURLConnection.HTTP_NOT_IMPLEMENTED;
import static java.net.HttpURLConnection.HTTP_OK;
import static spark.Spark.*;

public class Palettes implements RouteGroup {

    public static final String PATH = "/palette";

    @Override
    public void addRoutes() {
        /*        options(Commons.DEFAULT_PATH + "*", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<Commons.InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });*/
        get("/architectures", (request, response) -> {
            try {
                Palette palette = OpenArchi.getArchitecturePalette(Item.GET_ALL_PROTOTYPES);
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(palette);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        post("/architectures", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            return EMPTY_RESPONSE;
        });
        get("/bpms", (request, response) -> {
            try {
                com.araguacaima.open_archi.persistence.diagrams.bpm.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.bpm.Palette();
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(palette);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        post("/bpms", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            return EMPTY_RESPONSE;
        });
        get("/flowcharts", (request, response) -> {
            try {
                com.araguacaima.open_archi.persistence.diagrams.flowchart.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.flowchart.Palette();
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(palette);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        post("/flowcharts", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            return EMPTY_RESPONSE;
        });
        get("/gantts", (request, response) -> {
            try {
                com.araguacaima.open_archi.persistence.diagrams.gantt.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.gantt.Palette();
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(palette);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        post("/gantts", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            return EMPTY_RESPONSE;
        });
        get("/sequences", (request, response) -> {
            try {
                com.araguacaima.open_archi.persistence.diagrams.sequence.Palette palette = new com.araguacaima.open_archi.persistence.diagrams.sequence.Palette();
                response.status(HTTP_OK);
                response.type(JSON_CONTENT_TYPE);
                return jsonUtils.toJSON(palette);
            } catch (Exception ex) {
                return throwError(response, ex);
            }
        });
        post("/sequences", (request, response) -> {
            response.status(HTTP_NOT_IMPLEMENTED);
            return EMPTY_RESPONSE;
        });

    }
}
