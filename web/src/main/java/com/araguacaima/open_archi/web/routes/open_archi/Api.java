package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import spark.Redirect;
import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.appendAccountInfoToContext;
import static com.araguacaima.open_archi.web.common.Commons.buildModelAndView;
import static spark.Spark.*;

public class Api implements RouteGroup {

    public static final String PATH = "/api";

    private Diagrams diagrams = new Diagrams();
    private Models models = new Models();
    private Catalogs catalogs = new Catalogs();
    private Consumers consumers = new Consumers();
    private Palettes palettes = new Palettes();

    @Override
    public void addRoutes() {
        redirect.get(Api.PATH, Api.PATH + Commons.SEPARATOR_PATH, Redirect.Status.PERMANENT_REDIRECT);

        final BeanBuilder bean = new BeanBuilder().title("Api");
        get(Commons.DEFAULT_PATH, (req, res) -> {
            appendAccountInfoToContext(req, res, bean);
            return buildModelAndView(bean, OpenArchi.PATH + "/apis");
        }, engine);
        before(Diagrams.PATH, OpenArchi.apiFilter);
        path("/diagrams", diagrams);
        before(Models.PATH, OpenArchi.apiFilter);
        path("/models", models);
        before(Catalogs.PATH, OpenArchi.apiFilter);
        path("/catalogs", catalogs);
        before(Consumers.PATH, OpenArchi.apiFilter);
        path("/consumers", consumers);
        before(Palettes.PATH, OpenArchi.apiFilter);
        path("/palettes", palettes);
    }

}
