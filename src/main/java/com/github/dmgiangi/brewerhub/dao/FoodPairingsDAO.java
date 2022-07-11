package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.exceptions.InsertException;
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
        if(id == null) return null;

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

    public void insertFoodPairings(Beer beer) throws IllegalArgumentException, InsertException {
        if(beer != null){
            FoodsDAO foodsDAO = new FoodsDAO(connection);
            for (String food_name : beer.getFoodPairings()) {
                int food_id = foodsDAO.getFoodIdByNameInsertIfNotExists(food_name);
                this.insertFoodPairing(beer.getId(), food_id);
            }
        } else throw new IllegalArgumentException("Beer must be not null");
    }


    private static final String insertFoodPairing =
            "INSERT INTO food_pairings (food_id, beer_id) VALUES (?, ?);";

    private void insertFoodPairing(int beer_id, int food_id) throws InsertException {
        try (PreparedStatement statement = connection
                .prepareStatement(insertFoodPairing)) {
            statement.setInt(1, food_id);
            statement.setInt(2, beer_id);
            if(statement.executeUpdate() == 0){
                logger.error("Insert FOOD PAIRING fail.");
                throw  new InsertException("Insert FOOD PAIRING fail.");
            }
        } catch (SQLException e) {
            logger.error("Insert FOOD PAIRING fail.");
            throw  new InsertException("Insert FOOD PAIRING fail.", e);
        }
    }
}
