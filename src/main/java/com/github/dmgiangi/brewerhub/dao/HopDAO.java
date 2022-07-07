package com.github.dmgiangi.brewerhub.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class HopDAO {
    final static Logger logger = LogManager.getLogger(HopDAO.class);

    private final Connection connection;
    public HopDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String getHopIdByName = "SELECT id FROM hops WHERE name = ?;";
    public int getHopIdByName(String hopName) {
        try (PreparedStatement statement = connection
                .prepareStatement(getHopIdByName)) {
            statement.setString(1, hopName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    private static final String getAttributeIdByName = "SELECT id FROM hop_attributes WHERE attribute = ?;";
    public int getAttributeIdByName(String attribute) {
        try (PreparedStatement statement = connection
                .prepareStatement(getAttributeIdByName)) {
            statement.setString(1, attribute);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    private static final String insertHop = "INSERT INTO hops (name) VALUES (?);";
    public int insertHop(String hopName) {
        try (PreparedStatement statement = connection
                .prepareStatement(insertHop, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, hopName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    private static final String insertAttribute = "INSERT INTO hop_attributes (attribute) VALUES (?);";
    public int insertAttribute(String attribute) {
        try (PreparedStatement statement = connection
                .prepareStatement(insertAttribute, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, attribute);
            if (statement.execute()) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }
}
