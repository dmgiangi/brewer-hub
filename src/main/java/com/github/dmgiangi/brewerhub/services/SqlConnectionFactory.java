package com.github.dmgiangi.brewerhub.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class SqlConnectionFactory
{
    final static Logger logger = LoggerFactory.getLogger(SqlConnectionFactory.class);

    @Autowired
    private final DatabasePropertyLoader propertyLoader;

    public SqlConnectionFactory(DatabasePropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    private String type;
    private String host;
    private String databaseName;
    private String port;

    public Connection getConnection(){
        Connection connection = null;

        type = propertyLoader.getType();
        databaseName = propertyLoader.getName();
        port = propertyLoader.getPort();

        try {
            host = propertyLoader.getHost();
            String username = propertyLoader.getUsername();
            String password = propertyLoader.getPassword();

            try
            {
                logger.debug("Connecting ... " + getConnInfo());
                Class.forName("org.mariadb.jdbc.Driver");
                connection = DriverManager.getConnection(getConnInfo(), username, password);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to connect to the database.");
        }

        if (connection != null) {
            logger.debug("Connection Established -> " + connection);
        } else {
            logger.error("Connection fail.");
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to connect to the database.");
        }
        return connection;
    }

    public void disconnect(Connection connection)
    {
        logger.debug("Disconnecting: " + connection);
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