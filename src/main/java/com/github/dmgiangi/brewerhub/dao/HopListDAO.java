package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.exceptions.InsertException;
import com.github.dmgiangi.brewerhub.models.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class HopListDAO {
    final static Logger logger = LogManager.getLogger(HopListDAO.class);

    private final Connection connection;
    public HopListDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }

    private static final String getHopListByBeerId =
            "SELECT attribute, name, added, quantity " +
            "FROM hop_attributes " +
            "INNER JOIN hop_pairings ON ( hop_attributes.id = hop_pairings.attribute_id ) " +
            "INNER JOIN hops ON ( hop_pairings.id_hops = hops.id ) " +
            "WHERE hop_pairings.id_beers = ?";

    public HopList getHopListByBeerID(Integer id) throws IllegalArgumentException{
        HopList hopList = null;

        try (PreparedStatement statement = connection.prepareStatement(getHopListByBeerId)) {
            statement.setInt(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                hopList = new HopList();
                while(resultSet.next()) {
                    hopList.add(
                            new Hop()
                                    .setAdd(resultSet.getString("added"))
                                    .setAttribute(resultSet.getString("attribute"))
                                    .setName(resultSet.getString("name"))
                                    .setWeight(
                                        new Weight()
                                            .setUnit(WeightUnits.GRAMS)
                                            .setValue(resultSet.getFloat("quantity"))
                                    )
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return hopList;
    }

    private static final String insertHopPairing =
        "INSERT INTO hop_pairings" +
                "( added, quantity, id_beers, id_hops, attribute_id ) VALUES ( ?, ?, ?, ?, ? );";

    public void insertHopList(Beer beer) throws InsertException {
        HopDAO hopDao = new HopDAO(connection);
        HopAttributeDAO hopAttributeDAO = new HopAttributeDAO(connection);

        for (Hop hop : beer.getIngredients().getHops()){
            Integer hop_id = hopDao.getHopIdAndInsertIfNotExist(hop.getName());
                if(hop_id == null) throw new InsertException("Cannot assign hop with null value to beer");

            Integer attribute_id = hopAttributeDAO.getHopAttributeIdAndInsertIfNotExist(hop.getAttribute());

            try (PreparedStatement statement = connection
                    .prepareStatement(insertHopPairing, Statement.RETURN_GENERATED_KEYS)){

                statement.setString(1, hop.getAdd());
                statement.setObject(
                        2,
                        hop.getWeight().setUnit(WeightUnits.GRAMS).getValue(),
                        Types.FLOAT
                );
                statement.setInt(3, beer.getId());
                statement.setInt(4, hop_id);
                statement.setObject(
                        5,
                        attribute_id,
                        Types.INTEGER
                );

                if(statement.executeUpdate() == 0)
                    throw new InsertException("Cannot assign hop to beer, no id returned.");

            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new InsertException("Cannot assign hop to beer.", e);
            }
        }
    }
}
