package org.aha.stock.util;

import org.aha.stock.SymbolPopulator;
import org.postgresql.ds.common.BaseDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;

public class ConfigUtils {

    /**
     * Check if given property is configured/set
     *
     * @param property
     * @return
     * @throws IOException
     */
    public static boolean isConfigured(final String property) throws IOException {
        return loadAppProperties().contains(property);
    }

    public static String getProperty(final String property) throws IOException {
        return loadAppProperties().getProperty(property);
    }

    /**
     * Load application configuration properties
     *
     * @return properties
     * @throws IOException in case of error
     */
    public static Properties loadAppProperties() throws IOException {
        return loadProperties("app.properties");
    }

    /**
     * Load given properties file
     *
     * @param propFile properties file
     * @return properties
     * @throws IOException in case of errors
     */
    public static Properties loadProperties(final String propFile) throws IOException {
        final Properties properties = new Properties();
        final InputStream is = SymbolPopulator.class.getClassLoader().getResourceAsStream(propFile);
        if (is != null) {
            properties.load(is);
        }
        return properties;
    }


    /**
     *
     * @param symbol
     * @param duration
     * @param format
     * @return
     * @throws IOException
     */
    public static URL getStockUrl(final String symbol, final String duration, final String format) throws IOException {

        final String url  = ConfigUtils.getProperty("stocks.history.url");
        final String stockPH = ConfigUtils.getProperty("stocks.history.url.placeholder.stock");
        final String formatPH = ConfigUtils.getProperty("stocks.history.url.placeholder.format");
        final String durationPH = ConfigUtils.getProperty("stocks.history.url.placeholder.duration");
        final String replaced = url.replace(stockPH,symbol).replace(formatPH, format).replace(durationPH,duration);
        return new URL(replaced);
    }

    /**
     * Create configured DataSource
     *
     * @return
     * @throws IOException
     */
    public static BaseDataSource getDataSource() throws IOException {

        final Properties properties = ConfigUtils.loadAppProperties();
        final BaseDataSource ds = new org.postgresql.ds.PGConnectionPoolDataSource();

        ds.setSslMode(properties.getProperty("datasource.sslMode", "disable"));
        ds.setServerName(properties.getProperty("datasource.server", "localhost"));

        final int port = Integer.valueOf(properties.getProperty("datasource.port", "5432"));
        ds.setPortNumber(port);

        ds.setDatabaseName(properties.getProperty("datasource.database", "stocks"));
        ds.setUser(properties.getProperty("datasource.user", "aha"));
        ds.setPassword(properties.getProperty("datasource.password", "aDm1n"));
        return ds;
    }
}
