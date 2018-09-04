package com.araguacaima.open_archi.web.routes;

import spark.*;

import java.util.*;

import static com.araguacaima.open_archi.web.Server.engine;
import static spark.Spark.*;

public class Index {

    private static Map<String, Object> map = new HashMap<>();

    public static About about = new About();
    public static Root root = new Root();
    public static Contact contact = new Contact();
    public static Braas braas = new Braas();
    public static CompositeSpecification compositeSpecification = new CompositeSpecification();

    private static class Root implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Araguacaima | Solutions for the open source community");
            get("/", (req, res) -> new ModelAndView(map, "home"), engine);

        }
    }

    private static class About implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "About araguacaima");
            get("/", (req, res) -> new ModelAndView(map, "/about"), engine);
        }
    }

    private static class Contact implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Contact araguacaima");
            get("/", (req, res) -> new ModelAndView(map, "/contact"), engine);
        }
    }

    private static class Braas implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Rules as a Service");
            get("/", (req, res) -> new ModelAndView(map, "/braas/home"), engine);
        }
    }

    private static class CompositeSpecification implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Composite Specification");
            get("/", (req, res) -> new ModelAndView(map, "/composite-specification/home"), engine);
        }
    }
}
