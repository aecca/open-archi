package com.araguacaima.open_archi.web.config.context;

import com.araguacaima.open_archi.web.CustomPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.SpringSecurityMessageSource;

@Configuration
@Order(0)
public class BundlesConfig {

    private static final String MESSAGE_SOURCE = "messages/messages";
    private static final String HTTP_STATUS_CODES_SOURCE = "messages/httpStatusCodes";

    public BundlesConfig() {
    }

    @Bean(name = "propertyPlaceholderConfigurer")
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new CustomPropertyPlaceholderConfigurer();

        Resource[] locations = new Resource[2];
        locations[1] = new ClassPathResource("properties/commons.properties");
        locations[2] = new ClassPathResource("specification.properties");

        propertyPlaceholderConfigurer.setLocations(locations);
        propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        propertyPlaceholderConfigurer.setIgnoreResourceNotFound(true);

        return propertyPlaceholderConfigurer;
    }

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource messageSource() {
        SpringSecurityMessageSource messageSource = new SpringSecurityMessageSource();
        messageSource.setBasename(MESSAGE_SOURCE);
        messageSource.setCacheSeconds(5);
        return messageSource;
    }


    @Bean(name = "httpStatusCodes")
    public ResourceBundleMessageSource httpStatusCodes() {
        SpringSecurityMessageSource messageSource = new SpringSecurityMessageSource();
        messageSource.setBasename(HTTP_STATUS_CODES_SOURCE);
        messageSource.setCacheSeconds(5);
        return messageSource;
    }
}