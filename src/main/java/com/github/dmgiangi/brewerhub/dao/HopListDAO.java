package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            "INNER JOIN hop_pairings ON ( hop_attributes.id = hop_pairings.id  ) " +
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

    public void insertHopList(Beer beer) {
        HopDAO hopDao = new HopDAO(connection);
        for (Hop hop : beer.getIngredients().getHops()){
            int hop_id = hopDao.getHopIdByName(hop.getName());
            int attribute_id = hopDao.getAttributeIdByName(hop.getAttribute());
            if(hop_id == 0)
                hop_id = hopDao.insertHop(hop.getName());
            if(attribute_id == 0)
                attribute_id = hopDao.insertAttribute(hop.getAttribute());

            try (PreparedStatement statement = connection
                    .prepareStatement(insertHopPairing)) {
                statement.setString(1, hop.getAdd());
                statement.setFloat(2,
                        hop.getWeight().setUnit(WeightUnits.GRAMS).getValue());
                statement.setInt(3, beer.getId());
                statement.setInt(4, hop_id);
                statement.setInt(5, attribute_id);
                statement.execute();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
