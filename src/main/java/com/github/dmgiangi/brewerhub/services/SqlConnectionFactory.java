package com.github.dmgiangi.brewerhub.services;

import com.github.dmgiangi.brewerhub.models.exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class SqlConnectionFactory
{
    final static Logger logger = LoggerFactory.getLogger(SqlConnectionFactory.class);

    @Value("${database.type}")
    private String type;
    @Value("${database.host}")
    private String host;
    @Value("${database.name}")
    private String name;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.port:3306}")
    private String port;

    public Connection getConnection() throws ConnectionException{
        Connection connection;

        try {
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
                name;
    }
}