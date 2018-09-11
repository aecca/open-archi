package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.common.Commons;
import com.araguacaima.open_archi.web.routes.Index;
import com.araguacaima.open_archi.web.routes.open_archi.OpenArchi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import org.apache.commons.lang3.StringUtils;
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
    public static String serverName = getServerName();
    public static int assignedPort = getAssignedPort();
    private static Logger log = LoggerFactory.getLogger(Server.class);

    static {
        ObjectMapper mapper = jsonUtils.getMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        config.setTemplateLoader(templateLoader);
        Map<String, String> jdbcUrlSettings = new HashMap<>();
        String jdbcDbUrl = System.getenv("JDBC_DATABASE_URL");
        String jdbcDbUsername;
        String jdbcDbPassword;
        String logLevel = null;
        if (null != jdbcDbUrl) {
            log.debug("Properties found on system environment...");
            log.debug("JDBC_DATABASE_URL=" + jdbcDbUrl);
            jdbcDbUsername = System.getenv("JDBC_DATABASE_USERNAME");
            log.debug("JDBC_DATABASE_USERNAME=" + jdbcDbUsername);
            jdbcDbPassword = System.getenv("JDBC_DATABASE_PASSWORD");
            log.debug("JDBC_DATABASE_PASSWORD=" + jdbcDbPassword);
            logLevel = System.getenv("LOG_LEVEL");
        } else {
            URL url = Server.class.getResource("/config/config.properties");
            Properties properties = new Properties();
            try {
                properties.load(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.debug("Properties found on config file '" + url.getFile().replace("file:" + File.separator, "") + "'");
            jdbcDbUrl = properties.getProperty("JDBC_DATABASE_URL");
            log.debug("JDBC_DATABASE_URL=" + jdbcDbUrl);
            jdbcDbUsername = properties.getProperty("JDBC_DATABASE_USERNAME");
            log.debug("JDBC_DATABASE_USERNAME=" + jdbcDbUsername);
            jdbcDbPassword = properties.getProperty("JDBC_DATABASE_PASSWORD");
            log.debug("JDBC_DATABASE_PASSWORD=" + jdbcDbPassword);
            logLevel = properties.getProperty("LOG_LEVEL");
        }
        jdbcUrlSettings.put("hibernate.connection.url", jdbcDbUrl);
        jdbcUrlSettings.put("hibernate.connection.username", jdbcDbUsername);
        jdbcUrlSettings.put("hibernate.connection.password", jdbcDbPassword);
        jdbcUrlSettings.put("hibernate.archive.autodetection", "class");
        jdbcUrlSettings.put("hibernate.default_schema", "Diagrams");
        jdbcUrlSettings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        jdbcUrlSettings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        jdbcUrlSettings.put("hibernate.show_sql", ((log.isDebugEnabled() || log.isInfoEnabled()) ? "true" : "false"));
        jdbcUrlSettings.put("hibernate.flushMode", "FLUSH_AUTO");
        jdbcUrlSettings.put("hibernate.hbm2ddl.auto", "update");
        jdbcUrlSettings.put("packagesToScan", "com.araguacaima.open_archi.persistence");
        jdbcUrlSettings.put("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
        jdbcUrlSettings.put("hibernate.c3p0.min_size", "8");
        jdbcUrlSettings.put("hibernate.c3p0.max_size", "30");
        jdbcUrlSettings.put("hibernate.c3p0.timeout", "300");
        jdbcUrlSettings.put("hibernate.c3p0.max_statements", "50");
        jdbcUrlSettings.put("hibernate.c3p0.idle_test_period", "3000");

        JPAEntityManagerUtils.init(jdbcUrlSettings);
    }

    private static String getServerName() {
        String server_name = processBuilder.environment().get("BASE_URL");
        if (server_name != null) {
            if (server_name.startsWith("http://")) {
                server_name = server_name.replaceFirst("http://", StringUtils.EMPTY);
            } else if (server_name.startsWith("https://")) {
                server_name = server_name.replaceFirst("https://", StringUtils.EMPTY);
            }
            return server_name;
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
            log.debug("Request for (relative): " + request.uri());
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

