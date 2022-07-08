package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.MashTemp;
import com.github.dmgiangi.brewerhub.models.TemperatureUnits;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MashTempDAO {
    final static Logger logger = LogManager.getLogger(MashTempDAO.class);

    private final Connection connection;
    public MashTempDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String insertMash =
            "INSERT INTO mash_subsequences (temperature, duration) VALUES (?, ?);";
    public int insertMash(MashTemp mash) {
        int id = 0;
        try (PreparedStatement statement = connection
                .prepareStatement(insertMash, Statement.RETURN_GENERATED_KEYS)) {
            statement.setFloat(1, mash.getTemperature().setUnit(TemperatureUnits.CELSIUS).getValue());
            statement.setFloat(1, mash.getDuration());
            if (statement.execute()) {
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return id;
    }
}
