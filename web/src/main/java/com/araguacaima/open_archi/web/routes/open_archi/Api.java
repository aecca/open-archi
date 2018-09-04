package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.web.BeanBuilder;
import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.appendAccountInfoToContext;
import static com.araguacaima.open_archi.web.common.Commons.buildModelAndView;
import static spark.Spark.*;

public class Api implements RouteGroup {

    private Diagrams diagrams = new Diagrams();
    private Models models = new Models();
    private Catalogs catalogs = new Catalogs();
    private Consumers consumers = new Consumers();
    private Palettes palettes = new Palettes();

    @Override
    public void addRoutes() {

        before("/api/diagrams/*", OpenArchi.apiFilter);
        before("/api/diagrams", OpenArchi.apiFilter);
        before("/api/models/*", OpenArchi.apiFilter);
        before("/api/models", OpenArchi.apiFilter);
        final BeanBuilder bean = new BeanBuilder().title("Api");
        get("/api", (req, res) -> {
            appendAccountInfoToContext(req, res, bean);
            return buildModelAndView(bean, "/open-archi/apis");
        }, engine);
        path("/api", () -> {
            path("/diagrams", diagrams);
            path("/models", models);
            path("/catalogs", catalogs);
            path("/consumers", consumers);
            path("/palettes", palettes);
        });
    }

}
