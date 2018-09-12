package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import spark.RouteGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.routes.open_archi.Samples.getExamples;
import static spark.Spark.before;
import static spark.Spark.get;

public class Editor implements RouteGroup {


    public static final String PATH = "/editor";

    @Override
    public void addRoutes() {
        //before(Commons.EMPTY_PATH, Commons.genericFilter);
        //before("/*", OpenArchi.strongSecurityFilter);
        BeanBuilder bean = new BeanBuilder();
        Map<String, Object> diagramTypesMap = new HashMap<>();
        for (String diagramType : deeplyFulfilledDiagramTypesCollection) {
            diagramTypesMap.put(diagramType, diagramType.equals(ElementKind.ARCHITECTURE_MODEL.name()));
        }
        try {
            bean.diagramTypes(jsonUtils.toJSON(diagramTypesMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        get(Commons.EMPTY_PATH, (req, res) -> {
            bean.title("Editor");
            bean.palette(jsonUtils.toJSON(OpenArchi.getArchitecturePalette()));
            bean.elementTypes(jsonUtils.toJSON(OpenArchi.getElementTypes()));
            bean.source("basic");
            bean.examples(getExamples());
            bean.nodeDataArray(new ArrayList());
            bean.linkDataArray(new ArrayList());
            String type = req.queryParams("type");
            if (type != null) {
                for (String diagramType : deeplyFulfilledDiagramTypesCollection) {
                    diagramTypesMap.put(diagramType, diagramType.equals(type));
                }
                bean.diagramTypes(jsonUtils.toJSON(diagramTypesMap));
            }
            if (req.queryParams("fullView") != null) {
                bean.fullView(true);
            } else {
                bean.fullView(null);
            }
            return buildModelAndView(req, res, bean, OpenArchi.PATH + "/editor");
        }, engine);
        get("/:uuid", (request, response) -> {
            try {
                String id = request.params(":uuid");
                Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                if (model != null) {
                    model.validateRequest();
                } else {
                    final List nodeDataArray = getDefaultNodeDataArray();
                    final List linkDataArray = getDefaultLinkDataArray();
                    bean.nodeDataArray(nodeDataArray);
                    bean.linkDataArray(linkDataArray);
                }
                bean.palette(jsonUtils.toJSON(OpenArchi.getArchitecturePalette()));
                bean.source("basic");
                return buildModelAndView(request, response, bean, OpenArchi.PATH + "/editor");
            } catch (Exception ex) {
                bean.title("Error");
                bean.message(ex.getMessage());
                bean.stack(ex.getStackTrace());
                return buildModelAndView(bean, "/error");
            }
        }, engine);
    }
}
