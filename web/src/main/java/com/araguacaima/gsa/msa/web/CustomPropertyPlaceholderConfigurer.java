package com.araguacaima.gsa.msa.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Reads property from the default location unless a system property is set.
 * The name of the system property is set by the parameter {@literal systemPropertyName}.
 */
public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final String DEFAULT_DATASOURCENAME = "dataSource";
    private static final String DEFAULT_DBTABLENAME = "TV_CONFIG";
    private static final String DEFAULT_DBSCHEMA = "VALIDATORASODB";
    private static final String DEFAULT_DBKEYCOLUMNNAME = "PROPERTYNAME";
    private static final String DEFAULT_DBVALUECOLUMNNAME = "PROPERTYVALUE";
    private static final String DEFAULT_DBCATEGORYTABLENAME = "TV_CATEGORY";
    private static final String DEFAULT_DBCATEGORYKEYNAME = "CATEGORY";
    private String dataSourceName;
    private String dbTableName;
    private String dbSchema;
    private String dbKeyColumnName;
    private String dbValueColumnName;
    private String dbCategoryTableName;
    private String dbCategoryKeyName;
    private static Map<String, String> propertiesMap = new HashMap<String, String>();

    public static String getProperty(String name) {
        return propertiesMap.get(name);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) {
        properties.putAll(getPropertiesFromDb(properties));
        super.processProperties(beanFactory, properties);
        for (Object propertyKey : properties.keySet()) {
            propertiesMap.put(propertyKey.toString(), resolvePlaceholder(propertyKey.toString(), properties));
        }
    }

    private Properties getPropertiesFromDb(Properties properties) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        DataSource dataSource;

        PoolConfiguration poolConfiguration = new PoolProperties();

        String driverClass = properties.getProperty("jpa.dataSource.driver");
        String jdbcUrl = properties.getProperty("jpa.dataSource.url");
        String user = properties.getProperty("jpa.dataSource.username");
        String password = properties.getProperty("jpa.dataSource.password");
        poolConfiguration.setDriverClassName(driverClass);
        poolConfiguration.setUrl(jdbcUrl);
        poolConfiguration.setUsername(user);
        poolConfiguration.setPassword(password);

        dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolConfiguration);

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String dbKeyColumnName = getDbKeyColumnName();
            String dbValueColumnName = getDbValueColumnName();
            String dbTableName = getDbTableName();
            String dbSchema = getDbSchema();
            String sql = "SELECT g." + dbKeyColumnName + ", g."
                    + dbValueColumnName + " from " + dbSchema + "." + dbTableName + " g";
            resultSet = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    String key = resultSet.getString(dbKeyColumnName);
                    String value = StringUtils.defaultString(resultSet.getString(dbValueColumnName), StringUtils.EMPTY);
                    if (StringUtils.isNotBlank(key)) {
                        properties.put(key, value);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (NullPointerException | SQLException ignored) {

            }
        }
        return properties;
    }

    public String getDataSourceName() {
        return dataSourceName == null ? DEFAULT_DATASOURCENAME : this.dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDbTableName() {
        return dbTableName == null ? DEFAULT_DBTABLENAME : this.dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public String getDbKeyColumnName() {
        return dbKeyColumnName == null ? DEFAULT_DBKEYCOLUMNNAME : this.dbKeyColumnName;
    }

    public void setDbKeyColumnName(String dbKeyColumnName) {
        this.dbKeyColumnName = dbKeyColumnName;
    }

    public String getDbValueColumnName() {
        return dbValueColumnName == null ? DEFAULT_DBVALUECOLUMNNAME : this.dbValueColumnName;
    }

    public void setDbValueColumnName(String dbValueColumnName) {
        this.dbValueColumnName = dbValueColumnName;
    }

    public String getDbCategoryTableName() {
        return dbCategoryTableName == null ? DEFAULT_DBCATEGORYTABLENAME : this.dbCategoryTableName;
    }

    public void setDbCategoryTableName(String dbCategoryTableName) {
        this.dbCategoryTableName = dbCategoryTableName;
    }

    public String getDbCategoryKeyName() {
        return dbCategoryKeyName == null ? DEFAULT_DBCATEGORYKEYNAME : this.dbCategoryKeyName;
    }

    public void setDbCategoryKeyName(String dbCategoryKeyName) {
        this.dbCategoryKeyName = dbCategoryKeyName;
    }

    public String getDbSchema() {
        return dbSchema == null ? DEFAULT_DBSCHEMA : this.dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public static Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }

    public static void setPropertiesMap(Map<String, String> propertiesMap) {
        CustomPropertyPlaceholderConfigurer.propertiesMap = propertiesMap;
    }

}

