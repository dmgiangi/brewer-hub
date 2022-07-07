package com.github.dmgiangi.brewerhub.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnectionFactory
{
    final static Logger logger = LoggerFactory.getLogger(SqlConnectionFactory.class);

    private final DatabasePropertyLoader databasePropertyLoader = new DatabasePropertyLoader();
    private Connection connection;
    private String type;
    private String host;
    private String databaseName;
    private String username;
    private String password;
    private String port;

    public Connection getConnection()
    {
        type = databasePropertyLoader.getProperty("database.type");
        databaseName = databasePropertyLoader.getProperty("database.name");
        port = databasePropertyLoader.getProperty("database.port");

        logger.info("Connection To: " + type);

        switch (type) {
            case "mariadb" -> connectMariaDb();
            case "sqlite" -> connectSqlite();
            case "mysql" -> connectMySQL();
        }

        if (connection != null) {
            logger.info("Connection Established -> " + connection);
        } else {
            logger.error("Connection fail.");
        }

        return connection;
    }

    private void connectSqlite()
    {
        try
        {
            String sb = "jdbc:sqlite:" + databaseName;

            logger.info("Connecting ... "+ sb);

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage());
        }
    }

    private void connectMySQL()
    {
        host = databasePropertyLoader.getProperty("database.host");
        username = databasePropertyLoader.getProperty("database.username");
        password = databasePropertyLoader.getProperty("database.password");

        try
        {
            logger.debug("Connecting ... "+getConnInfo());
            Class.forName("com.mysql.jdbc.Driver");
            connection =DriverManager.getConnection(getConnInfo(),username, password);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage());
        }
    }

    private void connectMariaDb()
    {
        host = databasePropertyLoader.getProperty("database.host");
        username = databasePropertyLoader.getProperty("database.username");
        password = databasePropertyLoader.getProperty("database.password");

        try
        {
            logger.debug("Connecting ... " + getConnInfo());
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(getConnInfo(),username, password);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void disconnect()
    {
        logger.debug("Disconnecting ...");
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    private String getConnInfo()
    {
        return "jdbc:" +
                type + "://" +
                host + ":" +
                port + "/" +
                databaseName;
    }
}