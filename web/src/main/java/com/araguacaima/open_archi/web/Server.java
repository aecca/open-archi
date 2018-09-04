package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.routes.Index;
import com.araguacaima.open_archi.web.routes.open_archi.OpenArchi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Redirect;
import spark.template.jade.JadeTemplateEngine;

import java.security.GeneralSecurityException;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static spark.Spark.*;

public class Server {
    private static ProcessBuilder processBuilder = new ProcessBuilder();
    public static JadeConfiguration config = new JadeConfiguration();
    public static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static Logger log = LoggerFactory.getLogger(Server.class);

    public static String serverName = getServerName();
    public static int assignedPort = getAssignedPort();

    public static TemplateLoader templateLoader = new Loader("web/views");

    private enum HttpMethod {
        get,
        post,
        put,
        patch,
        delete,
        header
    }

    static {
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setTemplateLoader(templateLoader);
        //noinspection ResultOfMethodCallIgnored
        JPAEntityManagerUtils.getEntityManager();
    }

/*    private static ModelAndView index(final Request request, final Response response) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("profiles", getProfiles(request, response));
        final SparkWebContext ctx = new SparkWebContext(request, response);
        map.put("sessionId", ctx.getSessionIdentifier());
        return new ModelAndView(map, "index.mustache");
    }*/




    private static String getServerName() {
        if (processBuilder.environment().get("SERVER_NAME") != null) {
            return processBuilder.environment().get("SERVER_NAME");
        } else {
            //return "open-archi.herokuapp.com";
            return "localhost";
        }
    }

    private static int getAssignedPort() {
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    public static void main(String[] args) throws GeneralSecurityException {

        exception(Exception.class, exceptionHandler);
        port(assignedPort);
        secure("deploy/keystore.jks", "password", null, null);

        log.info("Server listen on port '" + assignedPort + "'");
        staticFiles.location("/web/public");
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
        path("/", Index.root);
        redirect.get("/about", "/about/", Redirect.Status.TEMPORARY_REDIRECT);
        path("/about", Index.about);
        redirect.get("/contact", "/contact/", Redirect.Status.TEMPORARY_REDIRECT);
        path("/contact", Index.contact);
        redirect.get("/braas", "/braas/", Redirect.Status.TEMPORARY_REDIRECT);
        path("/braas", Index.braas);
        redirect.get("/composite-specification", "/composite-specification/", Redirect.Status.TEMPORARY_REDIRECT);
        path("/composite-specification", Index.compositeSpecification);
        redirect.get("/open-archi", "/open-archi/", Redirect.Status.TEMPORARY_REDIRECT);
        path("/open-archi", OpenArchi.root);
    }
}

