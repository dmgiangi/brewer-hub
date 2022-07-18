package com.github.dmgiangi.brewerhub.services;

import com.github.dmgiangi.brewerhub.models.exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Connection getConnection() throws ConnectionException{
        Connection connection;

        type = propertyLoader.getType();
        databaseName = propertyLoader.getName();
        port = propertyLoader.getPort();

        try {
            host = propertyLoader.getHost();
            String username = propertyLoader.getUsername();
            String password = propertyLoader.getPassword();
            logger.debug("Connecting ... " + getConnInfo());
            try
            {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
                throw new ConnectionException("Unable to find jdbc driver", e);
            }
            connection = DriverManager.getConnection(getConnInfo(), username, password);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new ConnectionException("Unable to connect to the database", e);
        }
        if(connection == null)
            throw new ConnectionException("Connection is null");
        return connection;
    }

    public void disconnect(Connection connection)
    {
        logger.debug("Disconnecting: " + connection);
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new ConnectionException(e.getMessage(), e);
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