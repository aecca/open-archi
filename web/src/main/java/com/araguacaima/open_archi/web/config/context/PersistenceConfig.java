package com.araguacaima.open_archi.web.config.context;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@DependsOn("propertyPlaceholderConfigurer")
@Order(3)
public class PersistenceConfig {

    @Value("${jpa.dataSource.driver}")
    private String driverClass;
    @Value("${jpa.dataSource.url}")
    private String jdbcUrl;
    @Value("${jpa.dataSource.username}")
    private String user;
    @Value("${jpa.dataSource.password}")
    private String password;
    @Value("${jpa.jdbc.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jpa.jdbc.minPoolSize}")
    private int minPoolSize;
    @Value("${jpa.jdbc.maxStatements}")
    private int maxStatements;
    @Value("${jpa.jdbc.testConnection}")
    private boolean testConnectionOnCheckout;
    @Value("${jpa.jdbc.unreturnedConnectionTimeout}")
    private int unreturnedConnectionTimeout;
    @Value("${jpa.jdbc.debugUnreturnedConnectionStackTraces}")
    private boolean debugUnreturnedConnectionStackTraces;
    @Value("${jpa.jpaVendorAdapter.databasePlatform}")
    private String databasePlatform;
    @Value("${jpa.jpaVendorAdapter.database}")
    private String database;
    @Value("${jpa.jpaVendorAdapter.showSql}")
    private boolean showSql;
    @Value("${jpa.Log}")
    private String jpaLog;
    @Value("${jpa.RuntimeUnenhancedClasses}")
    private String jpaRuntimeUnenhancedClasses;
    @Value("${jpa.jdbc.MappingDefaults}")
    private String jpaJdbcMappingDefaults;
    @Value("${jpa.jdbc.SynchronizeMappings}")
    private String jpaJdbcSynchronizeMappings;
    @Value("${jpa.jpaDialect}")
    private String jpaDialect;
    @Value("${jpa.persistence.unit.name}")
    private String jpaPersistenceUnitName;
    @Value("${jdbc.initLocation}")
    private String jdbcInitLocation;
    @Value("${jdbc.dataLocation}")
    private String jdbcDataLocation;
    @Value("${jpa.jpaVendorAdapter.properties.eclipselink.characterEncoding}")
    private String eclipselinkCharacterEncoding;
    @Value("${jpa.javax.persistence.schema-generation.database.action}")
    private String schemaGenerationDatabaseAction;
    @Value("${jpa.javax.persistence.schema-generation.create-source}")
    private String schemaGenerationCreateSource;
    @Value("${jpa.jpaVendorAdapter.properties.eclipselink.logging.level}")
    private String eclipselinkLoggingLevel;
    @Value("${jpa.jpaVendorAdapter.properties.eclipselink.weaving}")
    private String eclipselinkWeaving;

    public PersistenceConfig() {
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory)
            throws PropertyVetoException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
/*

AMM: Este interceptor es únicamente necesario cuando se utiliza Hibernate como persistence provider. Para el caso de
Eclipselink este interceptor (configurado de ésta forma) presenta problemas para persistir loas entidades

    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        Properties attributes = new Properties();
        attributes.put("save", "PROPAGATION_REQUIRED");
        transactionInterceptor.setTransactionAttributes(attributes);
        return transactionInterceptor;
    }*/

    @Bean(name = "jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        jpaVendorAdapter.setDatabasePlatform(databasePlatform);
        jpaVendorAdapter.setDatabase(Database.valueOf(database));
        jpaVendorAdapter.setShowSql(showSql);
        return jpaVendorAdapter;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() throws PropertyVetoException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setPersistenceUnitName(jpaPersistenceUnitName);
        entityManagerFactoryBean.setPackagesToScan("com.bbva.templates.validation.persistence");
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setPersistenceProvider(persistenceProvider());
        Map<String, String> map = new HashMap<>();
        map.put("jpa.Log", jpaLog);
        map.put("jpa.RuntimeUnenhancedClasses", jpaRuntimeUnenhancedClasses);
        map.put("jpa.jdbc.SynchronizeMappings", jpaJdbcSynchronizeMappings);
        map.put("jpa.jdbc.MappingDefaults", jpaJdbcMappingDefaults);
        map.put("eclipselink.weaving", eclipselinkWeaving);
        map.put("eclipselink.characterEncoding", eclipselinkCharacterEncoding);
        map.put("javax.persistence.schema-generation.database.action", schemaGenerationDatabaseAction);
        map.put("javax.persistence.schema-generation.create-source", schemaGenerationCreateSource);
        map.put("eclipselink.logging.level", eclipselinkLoggingLevel);
        entityManagerFactoryBean.setJpaPropertyMap(map);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean(name = "persistenceProvider")
    public PersistenceProvider persistenceProvider() {
        return new HibernatePersistenceProvider();
    }

    @Bean(name = "dataSource", destroyMethod = "close")
    public DataSource dataSource() throws PropertyVetoException {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        //TODO AMM: Habilitar cuando se tenga lista la estructura de BD y los queries de creación, poblado y modificación
/*        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.valueOf(database))
                .addScript(jdbcInitLocation)
                .addScript(jdbcDataLocation)
                .build();*/
        return dataSource;
    }

}