package com.araguacaima.open_archi.web.config.context;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.araguacaima.open_archi", "com.araguacaima.specification", "com.araguacaima.commons.utils"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
        })
@DependsOn("propertyPlaceholderConfigurer")
@EnableAspectJAutoProxy
@Order(1)
public class CommonConfig {

    @Value("${mail.server.host}")
    private String host;

    @Value("${mail.server.port}")
    private int port;

    @Value("${mail.server.protocol}")
    private String protocol;

    @Value("${mail.server.username}")
    private String username;

    @Value("${mail.server.password}")
    private String password;

    public CommonConfig() {
    }

    @Bean(name = "reflectionsModel")
    public Reflections reflectionsModel() {
        Reflections reflections = new Reflections("com.bbva.model",
                new SubTypesScanner(false),
                new TypeAnnotationsScanner());
        return reflections;
    }


    @Bean(name = "reflections")
    public Reflections reflections() {
        Reflections reflections = new Reflections("com.bbva",
                new SubTypesScanner(false),
                new TypeAnnotationsScanner());
        return reflections;
    }

    @Bean(name = "mailSender")
    public JavaMailSender mailSender() throws IOException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Resource resource = new ClassPathResource("properties/commons.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setJavaMailProperties(props);

        return javaMailSender;
    }

    @Bean(name = "randomData")
    public RandomDataGenerator randomData() {
        return new RandomDataGenerator();
    }

}