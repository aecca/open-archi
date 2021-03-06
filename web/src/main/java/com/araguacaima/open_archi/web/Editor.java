package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.orpheusdb.utils.OrpheusDbJPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import spark.RouteGroup;
import spark.route.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.Samples.getExamples;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static spark.Spark.get;
import static spark.Spark.options;

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
        bean.diagramTypes(diagramTypesMap);
        get(Commons.EMPTY_PATH, (req, res) -> {
            bean.title("Editor");
            bean.palette(OpenArchi.getArchitecturePalette(Item.GET_ALL_MODELS));
            bean.elementTypes(OpenArchi.getElementTypes());
            bean.source("basic");
            bean.examples(getExamples());
            bean.model(new Object());
            String type = req.queryParams("type");
            if (type != null) {
                for (String diagramType : deeplyFulfilledDiagramTypesCollection) {
                    diagramTypesMap.put(diagramType, diagramType.equals(type));
                }
                bean.diagramTypes(diagramTypesMap);
            }
            if (req.queryParams("fullView") != null) {
                bean.fullView(true);
            } else {
                bean.fullView(null);
            }
            bean.prototyper(false);
            String kind = req.queryParams("kind");
            String name = req.queryParams("name");
            Map<String, Object> map = new HashMap<>();
            Enum anEnum = (Enum) enumsUtils.getEnum(ElementKind.class, kind);
            map.put("kind", anEnum);
            map.put("name", name);
            Taggable model = OrpheusDbJPAEntityManagerUtils.findByQuery(Item.class, Item.GET_MODELS_BY_NAME_AND_KIND, map);
            if (model != null) {
                model.validateRequest();
                bean.model(model);
            }
            return buildModelAndView(req, res, bean, PATH);
        }, engine);
        get("/:uuid", (req, res) -> {
            try {
                String id = req.params(":uuid");
                Taggable model = OrpheusDbJPAEntityManagerUtils.find(Taggable.class, id);
                if (model != null) {
                    model.validateRequest();
                    bean.model(model);
                }
                bean.title("Editor");
                bean.palette(OpenArchi.getArchitecturePalette(Item.GET_ALL_MODELS));
                bean.elementTypes(OpenArchi.getElementTypes());
                bean.source("basic");
                bean.examples(getExamples());
                String type = req.queryParams("type");
                if (type != null) {
                    for (String diagramType : deeplyFulfilledDiagramTypesCollection) {
                        diagramTypesMap.put(diagramType, diagramType.equals(type));
                    }
                    bean.diagramTypes(diagramTypesMap);
                }
                if (req.queryParams("fullView") != null) {
                    bean.fullView(true);
                } else {
                    bean.fullView(null);
                }
                bean.prototyper(false);
                return buildModelAndView(req, res, bean, PATH);
            } catch (Exception ex) {
                bean.title("Error");
                bean.message(ex.getMessage());
                bean.stack(ex.getStackTrace());
                return buildModelAndView(bean, "/error");
            }
        }, engine);
    }
}
