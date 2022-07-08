package com.github.dmgiangi.brewerhub.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class YeastDAO {
    final static Logger logger = LogManager.getLogger(YeastDAO.class);

    Connection connection;
    public YeastDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String insertYeast = "INSERT INTO yeasts (name) VALUE ( ? )";

    private int insertYeast(String yeastName){
        int id = 0;

        try (PreparedStatement statement = connection
                .prepareStatement(insertYeast, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, yeastName);

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt("insert_id");
            logger.info("yeast id: = " + id);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return id;
    }

    private static final String getYeastIdByName = "SELECT id FROM yeasts WHERE name = ?;";

    public int getYeastIdByName(String yeastName)  throws IllegalArgumentException{
        int id = 0;

        try (PreparedStatement statement = connection.prepareStatement(getYeastIdByName)) {
            statement.setString(1, yeastName);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next())
                    id = resultSet.getInt(1);
                else
                    logger.info("No yeast with name: " + yeastName + " found");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return id;
    }

    private static final String getYeastNameById = "SELECT name FROM yeasts WHERE id = ?;";

    public String getYeastNameByID(int yeastId)  throws IllegalArgumentException{
        String yeast = null;

        if(connection != null){
            try (PreparedStatement statement = connection.prepareStatement(getYeastNameById)) {
                statement.setInt(1, yeastId);
                if (statement.execute()) {
                    ResultSet resultSet = statement.getResultSet();
                    resultSet.next();
                    yeast = resultSet.getString(1);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        } else throw new IllegalArgumentException("Connection must be not null");

        return yeast;
    }

    public int getYeastIdAndInsertIfNotExist(String yeastName){
        int id = getYeastIdByName(yeastName);
        return id != 0
                ? id
                : insertYeast(yeastName);
    }
}
