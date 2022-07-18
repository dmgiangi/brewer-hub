package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.exceptions.InsertException;
import com.github.dmgiangi.brewerhub.models.entity.*;
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
        if (id == null) return null;

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
    public void insertMaltList(Beer beer) throws InsertException {
        if (beer != null) {
            for (Malt malt : beer.getIngredients().getMalts()) {
                try (PreparedStatement statement = connection
                        .prepareStatement(insertMaltPairing)) {
                    statement.setInt(1, beer.getId());
                    statement.setObject(
                            2,
                            malt.getWeight().setUnit(WeightUnits.KILOGRAMS).getValue(),
                            Types.FLOAT);
                    statement.setInt(3, new MaltDAO(connection)
                            .getMaltIdAndInsertIfNotExist(malt.getName()));


                    int affectedRows = statement.executeUpdate();

                    if (affectedRows == 0){
                        logger.error("Inserting new MALT PAIRING fail.");
                        throw new InsertException("Inserting new MALT PAIRING fail.");
                    }
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                    throw new InsertException("Inserting new MALT PAIRING fail.");
                }
            }
        }
    }
}
