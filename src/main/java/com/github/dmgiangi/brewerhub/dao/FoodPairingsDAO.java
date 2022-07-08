package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.models.FoodPairings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class FoodPairingsDAO {
    final static Logger logger = LogManager.getLogger(FoodPairingsDAO.class);

    private final Connection connection;
    public FoodPairingsDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String getFoodsByBeerId =
            "SELECT foods.name " +
            "FROM foods " +
            "INNER JOIN food_pairings ON ( foods.id = food_pairings.food_id  ) " +
            "WHERE food_pairings.beer_id = ?";

    public FoodPairings getFoodPairingsByBeerId(Integer id) throws IllegalArgumentException{
        FoodPairings foodPairings = null;

        try (PreparedStatement statement = connection.prepareStatement(getFoodsByBeerId)) {
            statement.setInt(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                foodPairings = new FoodPairings();
                while(resultSet.next()) {
                    foodPairings.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return foodPairings;
    }

    public void insertFoodPairings(Beer beer) throws IllegalArgumentException{
        if(beer != null){
            for (String food_name : beer.getFoodPairings()) {
                int food_id = this.getFoodIdByName(food_name);
                if(food_id == 0){
                    food_id = this.insertFood(food_name);
                }
                this.insertFoodPairing(beer.getId(), food_id);
            }
        } else throw new IllegalArgumentException("Beer must be not null");
    }


    private static final String insertFoodPairing =
            "INSERT INTO food_pairings (food_id, beer_id) VALUES (?, ?);";

    private void insertFoodPairing(Integer beer_id, int food_id) {
        try (PreparedStatement statement = connection
                .prepareStatement(insertFoodPairing)) {
            statement.setInt(1, food_id);
            statement.setInt(2, beer_id);
            if (statement.execute()) {
                logger.info("Food Pairing (food_id: " + food_id +
                        ", beer_id: " + beer_id + ")successfully added.");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }


    private static final String insertFood =
            "INSERT INTO foods (name) VALUES (?);";

    private int insertFood(String food_name) {
        try (PreparedStatement statement = connection
                .prepareStatement(insertFood, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, food_name);
            if (statement.execute()) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                logger.info("Food (" + food_name + ")successfully added.");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }


    private static final String getFoodIdByName =
            "SELECT id FROM foods WHERE name = ?";

    public int getFoodIdByName(String foodName){
        try (PreparedStatement statement = connection
                .prepareStatement(getFoodIdByName)) {
            statement.setString(1, foodName);
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
}
