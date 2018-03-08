package com.araguacaima.open_archi.web.config.context;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Order(2)
public class LogbackConfig {

    @Value("${html.log.file.name}")
    private String htmlLogFileName;

    @Value("${file.log.name.pattern}")
    private String fileLogNamePattern;

    @Value("${temp.path}")
    private String tempPath;

    private final Map<String, String> loggersMap = new HashMap<>();

    private List<Logger> loggers = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        loggersMap.put("org.springframework", "INFO");
        loggersMap.put("net.sf.jxls", "ERROR");
        loggersMap.put("net.sf.jxls.reader", "ERROR");
        loggersMap.put("org.thymeleaf", "ERROR");
        loggersMap.put("com.bbva.templates.validation", "DEBUG");
        loggersMap.put("com.mchange.v2", "ERROR");
        loggersMap.put("org.apache.catalina", "ERROR");

        for (Map.Entry<String, String> logger : loggersMap.entrySet()) {
            loggers.add(createLogger(logger.getKey(), Level.toLevel(logger.getValue())));
        }

        setRootLoggingLevel(Level.INFO);
    }

    @Bean(name = "applicationContextHolder")
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean(name = "loggerContext")
    public LoggerContext loggerContext() {
        final LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerFactory.reset();
        return loggerFactory;
    }

    private void setRootLoggingLevel(Level level) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

    private Logger createLogger(String loggerName, Level level) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(level);
        return logger;
    }

    @Bean(name = "fileAppender", initMethod = "start", destroyMethod = "stop")
    public FileAppender<ILoggingEvent> fileAppender(LoggerContext ctx,
                                                    PatternLayoutEncoder encoder,
                                                    TriggeringPolicy<ILoggingEvent> triggeringPolicy) {
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(ctx);
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.setAppend(false);

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setContext(ctx);
        rollingPolicy.setFileNamePattern(getAbsolutePath(fileLogNamePattern));
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.setTriggeringPolicy(triggeringPolicy);

        fixLoggersWithAppender(rollingFileAppender);

        return rollingFileAppender;
    }

    @Bean(name = "sizeBasedTriggeringPolicy", initMethod = "start", destroyMethod = "stop")
    public TriggeringPolicy sizeBasedTriggeringPolicy(LoggerContext ctx) {
        SizeBasedTriggeringPolicy<ILoggingEvent> sizeBasedTriggeringPolicy =
                new SizeBasedTriggeringPolicy<ILoggingEvent>();
        sizeBasedTriggeringPolicy.setContext(ctx);
        return sizeBasedTriggeringPolicy;
    }

    @Bean(name = "encoder", initMethod = "start", destroyMethod = "stop")
    public PatternLayoutEncoder encoder(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger.%M[%L] - %xEx{full} %msg%n");
        encoder.setCharset(Charset.forName("UTF-8"));
        return encoder;
    }

    @Bean(name = "consoleAppender", initMethod = "start", destroyMethod = "stop")
    public ConsoleAppender<ILoggingEvent> consoleAppender(LoggerContext ctx, PatternLayoutEncoder encoder) {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(ctx);
        consoleAppender.setEncoder(encoder);
        consoleAppender.setWithJansi(true);

        fixLoggersWithAppender(consoleAppender);

        return consoleAppender;
    }

    private void fixLoggersWithAppender(Appender<ILoggingEvent> appender) {
        for (Logger logger : loggers) {
            logger.addAppender(appender);
        }
    }

    @Bean(name = "htmlLayout", initMethod = "start", destroyMethod = "stop")
    public HTMLLayout htmlLayout(LoggerContext ctx) {
        HTMLLayout htmlLayout = new HTMLLayout();
        htmlLayout.setContext(ctx);
        htmlLayout.setPattern("%date%-5level%logger%xEx{full}%M[%L]%msg%n");
        return htmlLayout;
    }

    @Bean(name = "layoutWrappingEncoder", initMethod = "start", destroyMethod = "stop")
    public LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder(LoggerContext ctx, HTMLLayout htmlLayout) {
        LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder = new LayoutWrappingEncoder<>();
        layoutWrappingEncoder.setContext(ctx);
        layoutWrappingEncoder.setLayout(htmlLayout);
        return layoutWrappingEncoder;
    }

    @Bean(name = "htmlAppender", initMethod = "start", destroyMethod = "stop")
    public FileAppender<ILoggingEvent> htmlAppender(LoggerContext ctx, LayoutWrappingEncoder<ILoggingEvent> encoder) {
        FileAppender<ILoggingEvent> htmlAppender = new FileAppender<>();
        htmlAppender.setContext(ctx);
        htmlAppender.setEncoder(encoder);
        htmlAppender.setFile(getAbsolutePath(htmlLogFileName));

        fixLoggersWithAppender(htmlAppender);

        return htmlAppender;
    }

    public String getAbsolutePath(String s) {
        try {
            return new File(tempPath + File.separator + s).getCanonicalPath();
        } catch (Throwable ignored) {
            return s;
        }
    }

}