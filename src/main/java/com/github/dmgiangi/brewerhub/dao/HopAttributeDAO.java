package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.exceptions.InsertException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class HopAttributeDAO {
    final static Logger logger = LogManager.getLogger(HopAttributeDAO.class);

    private final Connection connection;

    public HopAttributeDAO(Connection connection) throws IllegalArgumentException {
        if (connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String getAttributeIdByName = "SELECT id FROM hop_attributes WHERE attribute = ?;";
    public Integer getAttributeIdByName(String attribute) {
        if(attribute == null) return null;

        int id = 0;
        try (PreparedStatement statement = connection
                .prepareStatement(getAttributeIdByName)) {
            statement.setString(1, attribute);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next())
                    id = resultSet.getInt(1);
                else
                    logger.info("No ATTRIBUTE with name: " + attribute + " found");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return id;
    }


    private static final String insertAttribute = "INSERT INTO hop_attributes (attribute) VALUES (?);";
    private Integer insertAttribute(String attribute) throws InsertException {
        if(attribute == null) return null;

        int id;
        try (PreparedStatement statement = connection
                .prepareStatement(insertAttribute, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, attribute);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Inserting new ATTRIBUTE fail.");
                throw new InsertException("Creating new ATTRIBUTE failed");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    logger.error("Creating new ATTRIBUTE failed, no ID obtained.");
                    throw new InsertException("Creating new ATTRIBUTE failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InsertException("Creating new ATTRIBUTE failed", e);
        }
        return id;
    }

    public Integer getHopAttributeIdAndInsertIfNotExist(String hopAttribute) throws InsertException {
        int id = getAttributeIdByName(hopAttribute);
        return id != 0
                ? id
                : insertAttribute(hopAttribute);
    }
}