package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.exceptions.InsertException;
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
    public Integer getHopIdByName(String hopName) throws InsertException {
        if(hopName == null) return null;

        int id = 0;
        try (PreparedStatement statement = connection
                .prepareStatement(getHopIdByName)) {
            statement.setString(1, hopName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next())
                    id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InsertException("Creating new YEAST failed.");

        }
        return id;
    }

    private static final String insertHop = "INSERT INTO hops (name) VALUES (?);";
    private Integer insertHop(String hopName) throws InsertException {
        if(hopName == null) return null;

        int id;

        try (PreparedStatement statement = connection
                .prepareStatement(insertHop, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, hopName);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new InsertException("Creating new Hop failed, no ROW affected in database.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    logger.error("Creating new HOP failed, no ID obtained.");
                    throw new InsertException("Creating new Hop failed, no ID obtained from database.");
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new InsertException("Creating new Hop failed");
        }
        return id;
    }

    public Integer getHopIdAndInsertIfNotExist(String hopName) throws InsertException {
        int id = getHopIdByName(hopName);
        return id != 0
                ? id
                : insertHop(hopName);
    }
}
