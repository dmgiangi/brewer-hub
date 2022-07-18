package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.exceptions.InsertException;
import com.github.dmgiangi.brewerhub.models.entity.MashTemp;
import com.github.dmgiangi.brewerhub.models.entity.TemperatureUnits;
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
    public Integer insertMash(MashTemp mash) throws InsertException {
        if(mash == null) return null;

        int id;
        try (PreparedStatement statement = connection
                .prepareStatement(insertMash, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, mash.getTemperature().setUnit(TemperatureUnits.CELSIUS).getValue(), Types.FLOAT);
            statement.setObject(2, mash.getDuration(),Types.FLOAT);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                logger.error("Inserting new MASH SUB-SEQUENCES fail.");
                throw new InsertException("Inserting new MASH SUB-SEQUENCES fail.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    logger.error("Creating new MASH SUB-SEQUENCES failed, no ID obtained.");
                    throw new InsertException("Creating new MASH SUB-SEQUENCES failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Inserting new MASH SUB-SEQUENCES fail.");
            throw new InsertException("Inserting new MASH SUB-SEQUENCES fail.", e);
        }
        return id;
    }
}
