package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.exceptions.InsertException;
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
    
    public int getMaltIdAndInsertIfNotExist(String MaltName) throws InsertException {
        int id = getMaltIdByName(MaltName);
        return id != 0
                ? id
                : insertMalt(MaltName);
    }


    private static final String insertMalt =
            "INSERT INTO malts (name) VALUES (?);";

    private Integer insertMalt(String maltName) throws InsertException {
        if(maltName == null) return null;

        int id;
        try (PreparedStatement statement = connection
                .prepareStatement(insertMalt, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, maltName);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                logger.error("Inserting new MALT fail.");
                throw new InsertException("Inserting new MALT fail.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    logger.error("Creating new MALT failed, no ID obtained.");
                    throw new InsertException("Inserting new MALT fail, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Inserting new MALT fail.");
            throw new InsertException("Inserting new MALT fail.", e);
        }
        return id;
    }


    private static final String getMaltIdByName = "SELECT id FROM malts WHERE name = ?;";

    private Integer getMaltIdByName(String maltName) {
        if(maltName == null) return null;
        int id = 0;
        try (PreparedStatement statement = connection.prepareStatement(getMaltIdByName)) {
            statement.setString(1, maltName);

            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next())
                    id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return id;
    }
}
