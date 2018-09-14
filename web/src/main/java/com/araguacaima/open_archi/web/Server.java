package com.araguacaima.open_archi.web;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.template.jade.JadeTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.araguacaima.open_archi.web.common.Commons.exceptionHandler;
import static com.araguacaima.open_archi.web.common.Commons.jsonUtils;
import static spark.Spark.*;

public class Server {
    public static JadeConfiguration config = new JadeConfiguration();
    public static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static TemplateLoader templateLoader = new Loader("web/views");
    private static ProcessBuilder processBuilder = new ProcessBuilder();
    public static int assignedPort;
    private static Logger log = LoggerFactory.getLogger(Server.class);
    private static Map<String, String> environment;
    public static String deployedServer;

    static {
        environment = processBuilder.environment();
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setTemplateLoader(templateLoader);
        Map<String, String> jdbcUrlSettings = new HashMap<>();
        String jdbcDbUrl = environment.get("JDBC_DATABASE_URL");
        String jdbcDbUsername;
        String jdbcDbPassword;
        URL url = Server.class.getResource("/config/config.properties");
        Properties properties = new Properties();
        try {
            properties.load(url.openStream());
            environment.putAll(MapUtils.fromProperties(properties));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != jdbcDbUrl) {
            log.info("Properties found on system environment...");
            log.info("JDBC_DATABASE_URL=" + jdbcDbUrl);
            jdbcDbUsername = environment.get("JDBC_DATABASE_USERNAME");
            log.info("JDBC_DATABASE_USERNAME=" + jdbcDbUsername);
            jdbcDbPassword = environment.get("JDBC_DATABASE_PASSWORD");
            log.info("JDBC_DATABASE_PASSWORD=" + jdbcDbPassword);
        } else {
            log.info("Properties found on config file '" + url.getFile().replace("file:" + File.separator, "") + "'");
            jdbcDbUrl = properties.getProperty("JDBC_DATABASE_URL");
            log.info("JDBC_DATABASE_URL=" + jdbcDbUrl);
            jdbcDbUsername = properties.getProperty("JDBC_DATABASE_USERNAME");
            log.info("JDBC_DATABASE_USERNAME=" + jdbcDbUsername);
            jdbcDbPassword = properties.getProperty("JDBC_DATABASE_PASSWORD");
            log.info("JDBC_DATABASE_PASSWORD=" + jdbcDbPassword);
        }
        jdbcUrlSettings.put("hibernate.connection.url", jdbcDbUrl);
        jdbcUrlSettings.put("hibernate.connection.username", jdbcDbUsername);
        jdbcUrlSettings.put("hibernate.connection.password", jdbcDbPassword);
        jdbcUrlSettings.put("hibernate.archive.autodetection", "class");
        jdbcUrlSettings.put("hibernate.default_schema", "Diagrams");
        jdbcUrlSettings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        jdbcUrlSettings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        jdbcUrlSettings.put("hibernate.show_sql", log.isDebugEnabled() ? "true" : "false");
        jdbcUrlSettings.put("hibernate.flushMode", "FLUSH_AUTO");
        jdbcUrlSettings.put("hibernate.hbm2ddl.auto", "update");
        jdbcUrlSettings.put("packagesToScan", "com.araguacaima.open_archi.persistence");
        jdbcUrlSettings.put("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
        jdbcUrlSettings.put("hibernate.c3p0.min_size", "8");
        jdbcUrlSettings.put("hibernate.c3p0.max_size", "30");
        jdbcUrlSettings.put("hibernate.c3p0.timeout", "300");
        jdbcUrlSettings.put("hibernate.c3p0.max_statements", "50");
        jdbcUrlSettings.put("hibernate.c3p0.idle_test_period", "3000");
        assignedPort = getAssignedPort();
        deployedServer = environment.get("DEPLOYED_SERVER");
        JPAEntityManagerUtils.init(jdbcUrlSettings);
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
        path(OpenArchi.PATH, OpenArchi.root);
    }
}

