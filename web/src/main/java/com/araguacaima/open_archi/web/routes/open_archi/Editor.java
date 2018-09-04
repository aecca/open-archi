package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import spark.Redirect;
import spark.RouteGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.routes.open_archi.Samples.getExamples;
import static spark.Spark.*;

public class Editor implements RouteGroup {


    public static final String PATH = "/editor";
    @Override
    public void addRoutes() {
        redirect.get(Editor.PATH, Editor.PATH + Commons.SEPARATOR_PATH, Redirect.Status.PERMANENT_REDIRECT);

        BeanBuilder bean = new BeanBuilder();
        before("/editor", OpenArchi.strongSecurityFilter);
        before("/editor/*", OpenArchi.strongSecurityFilter);
        path("/editor", () -> {
            Map<String, Object> diagramTypesMap = new HashMap<>();
            for (String diagramType : deeplyFulfilledDiagramTypesCollection) {
                diagramTypesMap.put(diagramType, diagramType.equals(ElementKind.ARCHITECTURE_MODEL.name()));
            }
            try {
                bean.diagramTypes(jsonUtils.toJSON(diagramTypesMap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            get(Commons.DEFAULT_PATH, (req, res) -> {
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
                appendAccountInfoToContext(req, res, bean);
                return buildModelAndView(bean, "/open-archi/editor");
            }, engine);
            get("/:uuid", (request, response) -> {
                try {
                    String id = request.params(":uuid");
                    Taggable model = JPAEntityManagerUtils.find(Taggable.class, id);
                    if (model != null) {
                        model.validateRequest();
                    }
                    if (model != null) {
                        List nodeDataArray = jsonUtils.fromJSON("[\n" +
                                "   {\n" +
                                "      \"key\":1,\n" +
                                "      \"text\":\"Alpha\",\n" +
                                "      \"color\":\"lightblue\"\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"key\":2,\n" +
                                "      \"text\":\"Beta\",\n" +
                                "      \"color\":\"orange\"\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"key\":3,\n" +
                                "      \"text\":\"Gamma\",\n" +
                                "      \"color\":\"lightgreen\",\n" +
                                "      \"group\":5\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"key\":4,\n" +
                                "      \"text\":\"Delta\",\n" +
                                "      \"color\":\"pink\",\n" +
                                "      \"group\":5\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"key\":5,\n" +
                                "      \"text\":\"Epsilon\",\n" +
                                "      \"color\":\"green\",\n" +
                                "      \"isGroup\":true\n" +
                                "   }\n" +
                                "]", List.class);
                        List linkDataArray = jsonUtils.fromJSON("[\n" +
                                "   {\n" +
                                "      \"from\":1,\n" +
                                "      \"to\":2,\n" +
                                "      \"color\":\"blue\"\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"from\":2,\n" +
                                "      \"to\":2\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"from\":3,\n" +
                                "      \"to\":4,\n" +
                                "      \"color\":\"green\"\n" +
                                "   },\n" +
                                "   {\n" +
                                "      \"from\":3,\n" +
                                "      \"to\":1,\n" +
                                "      \"color\":\"purple\"\n" +
                                "   }\n" +
                                "]", List.class);
                        bean.nodeDataArray(nodeDataArray);
                        bean.linkDataArray(linkDataArray);
                    }
                    bean.palette(jsonUtils.toJSON(OpenArchi.getArchitecturePalette()));
                    bean.source("basic");
                    return buildModelAndView(bean, "/open-archi/editor");
                } catch (Exception ex) {
                    bean.title("Error");
                    bean.message(ex.getMessage());
                    bean.stack(ex.getStackTrace());
                    return buildModelAndView(bean, "/error");
                }
            }, engine);

        });

    }

}
