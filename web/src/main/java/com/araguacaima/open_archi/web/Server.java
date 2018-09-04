package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import com.araguacaima.open_archi.web.routes.Index;
import com.araguacaima.open_archi.web.routes.open_archi.OpenArchi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.template.jade.JadeTemplateEngine;

import java.security.GeneralSecurityException;

import static com.araguacaima.open_archi.web.common.Commons.exceptionHandler;
import static com.araguacaima.open_archi.web.common.Commons.jsonUtils;
import static spark.Spark.*;

public class Server {
    public static JadeConfiguration config = new JadeConfiguration();
    public static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static TemplateLoader templateLoader = new Loader("web/views");
    private static ProcessBuilder processBuilder = new ProcessBuilder();
    public static String serverName = getServerName();
    public static int assignedPort = getAssignedPort();
    private static Logger log = LoggerFactory.getLogger(Server.class);

    static {
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setTemplateLoader(templateLoader);
        //noinspection ResultOfMethodCallIgnored
        JPAEntityManagerUtils.getEntityManager();
    }

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
        //secure("deploy/keystore.jks", "password", null, null);
        log.info("Server listen on port '" + assignedPort + "'");
        staticFiles.location("/web/public");
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
        path(Commons.DEFAULT_PATH, Index.root);
        path(Index.About.PATH, Index.about);
        path(Index.About.PATH + Commons.SEPARATOR_PATH, Index.about);
        path(Index.Contact.PATH, Index.contact);
        path(Index.Contact.PATH + Commons.SEPARATOR_PATH, Index.contact);
        path(Index.Braas.PATH, Index.braas);
        path(Index.Braas.PATH + Commons.SEPARATOR_PATH, Index.braas);
        path(Index.CompositeSpecification.PATH, Index.compositeSpecification);
        path(Index.CompositeSpecification.PATH + Commons.SEPARATOR_PATH, Index.compositeSpecification);
        path(OpenArchi.PATH, OpenArchi.root);
        path(OpenArchi.PATH + Commons.SEPARATOR_PATH, OpenArchi.root);
    }
}

