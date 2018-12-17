package com.araguacaima.open_archi.web;

import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.OPEN_ARCHI_DESKTOP;
import static com.araguacaima.open_archi.web.common.Commons.buildRoute;
import static spark.Spark.get;

public class Desktop implements RouteGroup {

    public static final String PATH = "/desktop";

    @Override
    public void addRoutes() {
        get("/home", buildRoute(new BeanBuilder().title(OPEN_ARCHI_DESKTOP), "/desktop/home"), engine);
        get("/apis", buildRoute("/desktop/apis"), engine);

    }

}
