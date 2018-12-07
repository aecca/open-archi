package com.araguacaima.open_archi.web;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.open_archi.web.common.Commons;
import com.araguacaima.orpheusdb.utils.OrpheusDbJPAEntityManagerUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.route.HttpMethod;
import spark.template.jade.JadeTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Commons.deeplyFulfilledParentModel;
import static com.araguacaima.open_archi.web.common.Commons.getOptions;
import static com.araguacaima.open_archi.web.common.Security.setCORS;
import static spark.Spark.*;

public class Server {
    public static JadeConfiguration config = new JadeConfiguration();
    public static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static TemplateLoader templateLoader = new Loader("web/views");
    public static int assignedPort;
    private static Logger log = LoggerFactory.getLogger(Server.class);
    private static Map<String, String> environment;
    public static String deployedServer;
    private static ProcessBuilder processBuilder = new ProcessBuilder();

    static {
        environment = new HashMap<>(processBuilder.environment());
        URL url = OrpheusDbJPAEntityManagerUtils.class.getResource("/config/config.properties");
        Properties properties = new Properties();
        try {
            properties.load(url.openStream());
            Map<String, String> map = MapUtils.fromProperties(properties);
            if (!map.isEmpty()) {
                String jdbcDbUrl = StringUtils.defaultIfBlank(map.get("JDBC_DATABASE_URL"), environment.get("JDBC_DATABASE_URL"));
                String jdbcDbUsername = StringUtils.defaultIfBlank(map.get("JDBC_DATABASE_USERNAME"), environment.get("JDBC_DATABASE_USERNAME"));
                String jdbcDbPassword = StringUtils.defaultIfBlank(map.get("JDBC_DATABASE_PASSWORD"), environment.get("JDBC_DATABASE_PASSWORD"));
                log.info("JDBC_DATABASE_URL=" + jdbcDbUrl);
                log.trace("JDBC_DATABASE_USERNAME=" + jdbcDbUsername);
                log.trace("JDBC_DATABASE_PASSWORD=" + jdbcDbPassword);
                map.put("hibernate.connection.url", jdbcDbUrl);
                map.put("hibernate.connection.username", jdbcDbUsername);
                map.put("hibernate.connection.password", jdbcDbPassword);
                map.put("hibernate.archive.autodetection", "class");
                map.put("hibernate.default_schema", "Diagrams");
                map.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
                map.put("hibernate.connection.driver_class", "org.postgresql.Driver");
                map.put("hibernate.show_sql", log.isDebugEnabled() ? "true" : "false");
                //map.put("hibernate.show_sql", "true");
                map.put("hibernate.flushMode", "FLUSH_AUTO");
                map.put("hibernate.hbm2ddl.auto", "update");
                map.put("packagesToScan", "com.araguacaima.open_archi.persistence");
                map.put("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
                map.put("hibernate.c3p0.min_size", "8");
                map.put("hibernate.c3p0.max_size", "20");
                map.put("hibernate.c3p0.timeout", "300");
                map.put("hibernate.c3p0.max_statements", "50");
                map.put("hibernate.c3p0.idle_test_period", "3000");
                //map.put("orpheus.db.versionable.packages", "com.araguacaima.open_archi.persistence.diagrams.architectural");
                //map.put("orpheus.db.versionable.classes", "{fill with comma separated fully qualified classes names}");
                environment.putAll(map);
                log.info("Properties taken from config file '" + url.getFile().replace("file:" + File.separator, "") + "'");
            } else {
                log.info("Properties taken from system map...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Properties: " + environment);
        OrpheusDbJPAEntityManagerUtils.init("open-archi", environment);
        config.setTemplateLoader(templateLoader);
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        assignedPort = getAssignedPort();
        deployedServer = environment.get("DEPLOYED_SERVER");
        String basePath = "http://" + (deployedServer.contains(":") ? deployedServer : deployedServer + (assignedPort == 80 ? "" : ":" + assignedPort));
        config.setBasePath(basePath);
        config.getSharedVariables().put("basePath", basePath);
        config.setPrettyPrint(true);
    }

    private static int getAssignedPort() {
        if (environment.get("PORT") != null) {
            return Integer.parseInt(environment.get("PORT"));
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
            //log.info("Request for (relative): " + request.uri());
        });
        options(Commons.DEFAULT_PATH + "*", (request, response) -> {
            setCORS(request, response);
            Map<HttpMethod, Map<Commons.InputOutput, Object>> output = setOptionsOutputStructure(deeplyFulfilledParentModelCollection, deeplyFulfilledParentModel, HttpMethod.get, HttpMethod.post);
            return getOptions(request, response, output);
        });
        path(OpenArchi.PATH, OpenArchi.root);
    }
}

