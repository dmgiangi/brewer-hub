package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MaltListDAO {
    final static Logger logger = LogManager.getLogger(MaltListDAO.class);

    private final Connection connection;
    public MaltListDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String getMaltListByBeerId =
            "SELECT name, amount " +
                    "FROM malts " +
                    "INNER JOIN malt_pairings ON ( malts.id = malt_pairings.id_malt  ) " +
                    "WHERE malt_pairings.id_beer = ?";

    public MaltList getMaltListByBeerID(Integer id) {
        MaltList malts = null;

        try (PreparedStatement statement = connection.prepareStatement(getMaltListByBeerId)) {
            statement.setInt(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                malts = new MaltList();
                while(resultSet.next()) {
                    malts.add(
                        new Malt()
                            .setName(resultSet.getString("name"))
                            .setWeight(
                                new Weight()
                                    .setUnit(WeightUnits.KILOGRAMS)
                                    .setValue(resultSet.getFloat("amount"))
                            )
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return malts;
    }

    private static final String insertMaltPairing =
            "INSERT INTO malt_pairings (id_beer, amount, id_malt) VALUES (?, ?, ?);";
    public void insertMaltList(Beer beer) {
        if (beer != null) {
            for (Malt malt : beer.getIngredients().getMalts()) {
                try (PreparedStatement statement = connection
                        .prepareStatement(insertMaltPairing)) {
                    statement.setInt(1, beer.getId());
                    statement.setFloat(2, malt.getWeight().setUnit(WeightUnits.KILOGRAMS).getValue());
                    statement.setInt(1, new MaltDAO(connection)
                            .getMaltIdAndInsertIfNotExist(malt.getName()));

                    if (statement.execute())
                        logger.info("Malt Pairing successfully added.");
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }

            }
        }
    }
}
