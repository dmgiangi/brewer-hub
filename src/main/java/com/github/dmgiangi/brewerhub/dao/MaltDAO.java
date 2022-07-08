package com.github.dmgiangi.brewerhub.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MaltDAO {
    final static Logger logger = LogManager.getLogger(MaltDAO.class);

    Connection connection;
    public MaltDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }
    
    public int getMaltIdAndInsertIfNotExist(String MaltName){
        int id = getMaltIdByName(MaltName);
        return id != 0
                ? id
                : insertMalt(MaltName);
    }


    private static final String insertMalt =
            "INSERT INTO malts (name) VALUES (?);";

    private int insertMalt(String maltName) {
        try (PreparedStatement statement = connection
                .prepareStatement(insertMalt, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, maltName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                logger.info("Food (" + maltName + ")successfully added.");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }


    private static final String getMaltIdByName = "SELECT id FROM malts WHERE name = ?;";

    private int getMaltIdByName(String maltName) {
        try (PreparedStatement statement = connection.prepareStatement(getMaltIdByName)) {
            statement.setString(1, maltName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next())
                    return resultSet.getInt(1);
                else
                    logger.info("No yeast with name: " + maltName + " found");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }
}
