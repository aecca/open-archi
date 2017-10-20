package com.araguacaima.gsa.msa.web;

import com.araguacaima.gsa.msa.web.wrapper.RsqlJsonFilter;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.TemplateLoader;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.template.jade.JadeTemplateEngine;
import spark.template.jade.loader.SparkClasspathTemplateLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Server {
    private static final String DB_PATH = "./db/cards.json";
    private static JadeConfiguration config = new JadeConfiguration();
    private static JadeTemplateEngine engine = new JadeTemplateEngine(config);
    private static Logger log = LoggerFactory.getLogger(Server.class);
    private static final ExceptionHandler exceptionHandler = new ExceptionHandlerImpl(Exception.class) {
        @Override
        public void handle(Exception exception, Request request, Response response) {
            String message = exception.getMessage();
            StackTraceElement[] stackTrace = exception.getStackTrace();
            if (message == null) {
                try {
                    message = exception.getCause().getMessage();
                } catch (Throwable ignored) {
                }
                if (message == null) {
                    List ts = Arrays.asList(stackTrace);
                    message = StringUtils.join(ts);
                }
            }
            log.error("Error '" + message + "'");
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("title", "Error");
            errorMap.put("message", message);
            errorMap.put("stack", stackTrace);
            response.body(render(errorMap, "error"));
        }
    };
    private static TemplateLoader templateLoader = new SparkClasspathTemplateLoader("web/views");

    static {
        config.setTemplateLoader(templateLoader);
    }

    public static void main(String[] args)
            throws GeneralSecurityException {

        //Java package name where POJO's are present
        String baseScanPackage = "com.araguacaima.apis.model";

        //Optionally a Cipher object if you need Encryption
        ICipher cipher = new DefaultAESCBCCipher("1r8+24pibarAWgS85/Heeg==");

        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(DB_PATH, baseScanPackage, cipher);
        String jxQuery = String.format("/.[id>'%s']", "04");
//        IList<Cards> cards = jsonDBTemplate.findAll(jxQuery, Cards.class);
        int assignedPort = getAssignedPort();
        port(assignedPort);
        log.info("Server listen on port '" + assignedPort + "'");
        String root = "cards";
        String jsonPath = root + ".json";
        Map<String, Object> map = new HashMap<>();
        map.put("title", "GRAPI FIQL Test");

        staticFiles.location("/web/public");
        get("/", (req, res) -> new ModelAndView(map, "home"), engine);
        path("/api", () -> {
            before("/*", (req, res) -> log.info("Received api call to '" + req.pathInfo() + "'"));
            get("/" + root, (req, res) -> {
                String filter_ = filter(req.queryParams("$filter"), jsonPath);
                String json = req.pathInfo().replaceFirst("/api/", "");
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("title", StringUtils.capitalize(json));
                jsonMap.put("json", filter_);
                return render(jsonMap, "json");
            });

            exception(Exception.class, exceptionHandler);
        });

        path("/db", () -> {
            get("/" + root, (req, res) -> {
                String filter_ = filter(req.queryParams("$filter"), jsonPath);
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("title", "DB");
                jsonMap.put("json", filter_);
                jsonMap.put("card_db_relative_path", DB_PATH);
                jsonMap.put("rows", "");
                jsonMap.put("cards", "");
                jsonMap.put("fields", "");
                return render(jsonMap, "db");
            });
            exception(Exception.class, exceptionHandler);
        });

    }

    private static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    private static String filter(String query, String jsonPath)
            throws IOException, URISyntaxException {

        String name = "/db/" + jsonPath;
        log.info("Finding path: " + name);
        InputStream resource = Server.class.getResourceAsStream(name);
        log.info("Resource: " + resource);
        assert resource != null;
        String jsonStr = read(resource);
        log.info(jsonPath + " file loaded!!!");
        if (query == null) {
            return jsonStr;
        } else {
            return RsqlJsonFilter.rsql(query, jsonStr);
        }
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return engine.render(new ModelAndView(model, templatePath));
    }

    public static String read(InputStream input)
            throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}

