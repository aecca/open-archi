package com.araguacaima.open_archi.web.routes;

import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.buildRoute;
import static spark.Spark.get;

public class Index {

    public static About about = new About();
    public static Root root = new Root();
    public static Contact contact = new Contact();
    public static Braas braas = new Braas();
    public static CompositeSpecification compositeSpecification = new CompositeSpecification();

    private static class Root implements RouteGroup {
        @Override
        public void addRoutes() {
            get(Commons.DEFAULT_PATH, buildRoute(new BeanBuilder().title("Araguacaima | Solutions for the open source community"), "home"), engine);
        }
    }

    private static class About implements RouteGroup {
        @Override
        public void addRoutes() {
            get(Commons.DEFAULT_PATH, buildRoute(new BeanBuilder().title("About araguacaima"), "/about"), engine);
        }
    }

    private static class Contact implements RouteGroup {
        @Override
        public void addRoutes() {
            get(Commons.DEFAULT_PATH, buildRoute(new BeanBuilder().title("Contact araguacaima"), "/contact"), engine);
        }
    }

    private static class Braas implements RouteGroup {
        @Override
        public void addRoutes() {
            get(Commons.DEFAULT_PATH, buildRoute(new BeanBuilder().title("Rules as a Service"), "/braas/home"), engine);
        }
    }

    private static class CompositeSpecification implements RouteGroup {
        @Override
        public void addRoutes() {
            get(Commons.DEFAULT_PATH, buildRoute(new BeanBuilder().title("Composite Specification"), "/composite-specification/home"), engine);
        }
    }
}
