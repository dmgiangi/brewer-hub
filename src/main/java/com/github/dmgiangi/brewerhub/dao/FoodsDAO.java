package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.exceptions.InsertException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class FoodsDAO {
    final static Logger logger = LogManager.getLogger(FoodsDAO.class);

    private final Connection connection;
    public FoodsDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String insertFood =
            "INSERT INTO foods (name) VALUES (?);";

    private Integer insertFood(String food_name) throws InsertException {
        if(food_name == null) return null;

        int id;
        try (PreparedStatement statement = connection
                .prepareStatement(insertFood, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, food_name);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                logger.error("Inserting new FOOD fail.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    logger.error("Creating new FOOD failed, no ID obtained.");
                    throw new InsertException("Creating new FOOD failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Creating new FOOD failed.");
            throw new InsertException("Creating new FOOD failed.", e);
        }
        return id;
    }


    private static final String getFoodIdByName =
            "SELECT id FROM foods WHERE name = ?";

    public Integer getFoodIdByName(String foodName){
        if(foodName == null) return null;

        int id = 0;
        try (PreparedStatement statement = connection
                .prepareStatement(getFoodIdByName)) {
            statement.setString(1, foodName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next())
                    id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Selecting FOOD failed.");
        }
        return id;
    }

    public int getFoodIdByNameInsertIfNotExists(String food_name) throws InsertException {
        int id = getFoodIdByName(food_name);
        return id != 0
                ? id
                : insertFood(food_name);
    }
}
