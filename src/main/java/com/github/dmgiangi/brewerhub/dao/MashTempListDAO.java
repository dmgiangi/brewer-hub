package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.exceptions.InsertException;
import com.github.dmgiangi.brewerhub.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MashTempListDAO {
    final static Logger logger = LogManager.getLogger(MashTempListDAO.class);

    private final Connection connection;
    public MashTempListDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String getMashTempListByBeerId = "SELECT temperature, duration " +
            "FROM mash_sequences " +
            "INNER JOIN mash_sequences ON ( mash_subsequences.id = mash_sequences.id_mash " +
            "WHERE mash_sequences.id_beer = ?";

    public MashTempList getMashTempListByBeerId(Integer id) throws IllegalArgumentException{
        if(id == null) return null;

        MashTempList mashTempList = null;

        try (PreparedStatement statement = connection.prepareStatement(getMashTempListByBeerId)) {
            statement.setInt(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                mashTempList = new MashTempList();
                while(resultSet.next()) {
                    mashTempList.add(
                        new MashTemp()
                                .setTemperature(
                                    new Temperature()
                                        .setUnit(TemperatureUnits.CELSIUS)
                                        .setValue(resultSet.getFloat("temperature"))
                                ).setDuration(resultSet.getInt("duration"))
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return mashTempList;
    }

    private static final String insertMashPairing =
            "INSERT INTO mash_sequences (id_beer, id_mash) VALUES (?, ?);";
    public void insertMashTempList(Beer beer) throws InsertException {
        if (beer != null) {
            for (MashTemp mash : beer.getMethod().getMashTempsList()) {
                try (PreparedStatement statement = connection
                        .prepareStatement(insertMashPairing)) {
                    statement.setInt(1, beer.getId());
                    statement.setInt(2, new MashTempDAO(connection)
                            .insertMash(mash));

                    if (statement.executeUpdate() == 0){
                        logger.error("MASH PAIRING fail to add, no ID obtained from database.");
                        throw new InsertException("MASH PAIRING fail to add, no ID obtained from database.");
                    }
                } catch (SQLException e) {
                    logger.error("MASH PAIRING fail to add.");
                    throw new InsertException("MASH PAIRING fail to add.", e);
                }
            }
        }
    }
}
