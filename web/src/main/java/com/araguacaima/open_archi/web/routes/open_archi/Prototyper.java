package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import spark.Redirect;
import spark.RouteGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static spark.Spark.*;

public class Prototyper implements RouteGroup {

    public static final String PATH = "/prototyper";

    @Override
    public void addRoutes() {
        redirect.get(Prototyper.PATH, Prototyper.PATH + Commons.SEPARATOR_PATH, Redirect.Status.PERMANENT_REDIRECT);

        before("/prototyper", OpenArchi.strongSecurityFilter);
        before("/prototyper/*", OpenArchi.strongSecurityFilter);

        path("/prototyper", () -> {
            Map<String, Object> mapEditor = new HashMap<>();
            exception(Exception.class, exceptionHandler);
            mapEditor.put("title", "Prototyper");
            Map<String, Boolean> diagramTypesMap = new HashMap<>();
            for (String diagramType : deeplyFulfilledDiagramTypesCollection) {
                diagramTypesMap.put(diagramType, diagramType.equals(ElementKind.ARCHITECTURE_MODEL.name()));
            }
            try {
                mapEditor.put("diagramTypes", jsonUtils.toJSON(diagramTypesMap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            get(Commons.DEFAULT_PATH, (req, res) -> {
                mapEditor.put("palette", jsonUtils.toJSON(OpenArchi.getArchitecturePalette()));
                mapEditor.put("elementTypes", jsonUtils.toJSON(OpenArchi.getElementTypes()));
                mapEditor.put("source", "basic");
                mapEditor.put("nodeDataArray", "[]");
                mapEditor.put("linkDataArray", "[]");
                return buildModelAndView(mapEditor, "/open-archi/editor");
            }, engine);
            get("/:uuid", (request, response) -> {
                try {
                    String id = request.params(":uuid");
                    Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                    if (model != null) {
                        model.validateRequest();
                    }
                    mapEditor.put("model", jsonUtils.toJSON(model));
                    mapEditor.put("palette", jsonUtils.toJSON(OpenArchi.getArchitecturePalette()));
                    mapEditor.put("source", "basic");
                    return buildModelAndView(mapEditor, "/open-archi/prototyper");
                } catch (Exception ex) {
                    mapEditor.put("title", "Error");
                    mapEditor.put("message", ex.getMessage());
                    mapEditor.put("stack", ex.getStackTrace());
                    return buildModelAndView(mapEditor, "/error");
                }
            }, engine);
        });
    }

}
