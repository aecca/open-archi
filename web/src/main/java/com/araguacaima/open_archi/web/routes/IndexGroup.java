package com.araguacaima.open_archi.web.routes;

import com.araguacaima.open_archi.persistence.commons.IdName;
import com.araguacaima.open_archi.persistence.diagrams.architectural.*;
import com.araguacaima.open_archi.persistence.diagrams.architectural.System;
import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.APIFilter;
import com.araguacaima.open_archi.web.ConfigFactory;
import com.araguacaima.open_archi.web.DBUtil;
import com.araguacaima.open_archi.web.Server;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.DefaultAuthorizers;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.LogoutRoute;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class IndexGroup {

    private TemplateEngine engine;
    private Map<String, Object> map = new HashMap<>();

    public IndexGroup(TemplateEngine engine_) {
        engine = engine_;
    }

    public About about = new About();
    public Root root = new Root();
    public Contact contact = new Contact();
    public Braas braas = new Braas();
    public CompositeSpecification compositeSpecification = new CompositeSpecification();

    private class Root implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Araguacaima | Solutions for the open source community");
            get("/", (req, res) -> new ModelAndView(map, "home"), engine);

        }
    }

    private class About implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "About araguacaima");
            get("/", (req, res) -> new ModelAndView(map, "/about"), engine);
        }
    }

    private class Contact implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Contact araguacaima");
            get("/", (req, res) -> new ModelAndView(map, "/contact"), engine);
        }
    }

    private class Braas implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Rules as a Service");
            get("/", (req, res) -> new ModelAndView(map, "/braas/home"), engine);
        }
    }

    private class CompositeSpecification implements RouteGroup {
        @Override
        public void addRoutes() {
            map.put("title", "Composite Specification");
            get("/", (req, res) -> new ModelAndView(map, "/composite-specification/home"), engine);
        }
    }
}
