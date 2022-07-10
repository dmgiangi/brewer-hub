package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class BeersDAO {
    final static Logger logger = LogManager.getLogger(BeersDAO.class);

    private final Connection connection;
    public BeersDAO(Connection connection) throws IllegalArgumentException{
        if(connection != null)
            this.connection = connection;
        else throw new IllegalArgumentException("Connection must be not null");
    }


    private final static String getBeerById = "SELECT * FROM beers WHERE beers.id = ?";
    public Beer selectBeerById(int id){
        BeersList beersList = null;

        try (PreparedStatement statement = connection.prepareStatement(getBeerById)) {
            statement.setInt(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                beersList = getBeersFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return beersList == null
            ? null
            : beersList.isEmpty()
                ? null
                : beersList.get(0);
    }

    private final static String getBeersList = "SELECT * FROM beers LIMIT ? OFFSET ?";
    public BeersList selectBeersList(int per_page, int page_number) throws IllegalArgumentException{
        BeersList beersList = null;

        try (PreparedStatement statement = connection.prepareStatement(getBeersList)) {
            statement.setInt(1, per_page);
            statement.setInt(2, page_number * per_page);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                beersList = getBeersFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return beersList;
    }

    private BeersList getBeersFromResultSet(ResultSet resultSet){
        BeersList beersList = new BeersList();
        try {
            while(resultSet.next()) {
                if (resultSet.getInt("id") != 0){
                    beersList.add(
                        new Beer().setId(resultSet.getInt("id"))
                            .setName(resultSet.getString("name"))
                            .setTagline(resultSet.getString("tagline"))
                            .setContributor(resultSet.getString("contributed_by"))
                            .setFirsBrewed(resultSet.getString("first_brewed"))
                            .setDescription(resultSet.getString("description"))
                            .setImageUrl(resultSet.getString("image_url"))
                            .setAbv(resultSet.getFloat("abv"))
                            .setIbu(resultSet.getFloat("ibu"))
                            .setTargetFg(resultSet.getFloat("target_fg"))
                            .setTargetOg(resultSet.getFloat("target_og"))
                            .setEbc(resultSet.getFloat("ebc"))
                            .setSrm(resultSet.getFloat("srm"))
                            .setPh(resultSet.getFloat("ph"))
                            .setAttenuationLevel(resultSet.getFloat("attenuation_level"))
                            .setBrewersTips(resultSet.getString("brewers_tips"))
                            .setFoodPairings(
                                    new FoodPairingsDAO(connection)
                                            .getFoodPairingsByBeerId(resultSet.getInt("id"))
                            ).setVolume(
                                    new Volume()
                                            .setUnit(VolumeUnits.LITRES)
                                            .setValue(resultSet.getFloat("volume"))
                            ).setBoilVolume(
                                    new Volume()
                                            .setUnit(VolumeUnits.LITRES)
                                            .setValue(resultSet.getFloat("volume")
                                            )
                            ).setMethod(
                                    new Method()
                                            .setTwist(resultSet.getString("twist"))
                                            .setFermentation(
                                                    new Fermentation()
                                                            .setTemperature(
                                                                    new Temperature()
                                                                            .setUnit(TemperatureUnits.CELSIUS)
                                                                            .setValue(resultSet.getFloat(
                                                                                    "fermentation_temperature"))
                                                            )
                                            ).setMashTempsList(
                                                    new MashTempListDAO(connection)
                                                            .getMashTempListByBeerId(resultSet.getInt("id"))
                                            )
                            ).setIngredients(
                                    new Ingredients()
                                            .setYeast(new YeastDAO(connection)
                                                    .getYeastNameByID(resultSet.getInt("yeast_id")))
                                            .setHops(
                                                    new HopListDAO(connection)
                                                            .getHopListByBeerID(resultSet.getInt("id"))
                                            ).setMalts(
                                                    new MaltListDAO(connection)
                                                            .getMaltListByBeerID(resultSet.getInt("id"))
                                            )
                            )

                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return beersList;
    }

    private final static String insertBeer = "INSERT INTO beers(" +
            "name, " +
            "tagline, " +
            "first_brewed, " +
            "description, " +
            "image_url, " +
            "abv, " +
            "ibu, " +
            "target_fg, " +
            "target_og, " +
            "ebc, " +
            "srm, " +
            "ph, " +
            "attenuation_level, " +
            "fermentation_temperature, " +
            "twist, " +
            "volume, " +
            "boil_volume, " +
            "brewers_tips, " +
            "contributed_by, " +
            "yeast_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";
    public Boolean insertBeer(Beer beer){
        try (PreparedStatement statement = connection
                .prepareStatement(insertBeer, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, beer.getName());
            statement.setString(2, beer.getTagline());
            statement.setString(3, beer.getFirsBrewed());
            statement.setString(4, beer.getDescription());
            statement.setString(5, beer.getImageUrl());
            statement.setFloat(6, beer.getAbv());
            statement.setFloat(7, beer.getIbu());
            statement.setFloat(8, beer.getTargetFg());
            statement.setFloat(9, beer.getTargetOg());
            statement.setFloat(10, beer.getEbc());
            statement.setFloat(11, beer.getSrm());
            statement.setFloat(12, beer.getPh());
            statement.setFloat(13, beer.getAttenuationLevel());
            beer.getMethod().getFermentation().getTemperature().setUnit(TemperatureUnits.CELSIUS);
            statement.setFloat(14, beer
                    .getMethod()
                    .getFermentation()
                    .getTemperature()
                    .setUnit(TemperatureUnits.CELSIUS)
                    .getValue()
            );
            statement.setString(15, beer.getMethod().getTwist());
            statement.setFloat(16, beer.getVolume().setUnit(VolumeUnits.LITRES).getValue());
            statement.setFloat(17, beer.getBoilVolume().setUnit(VolumeUnits.LITRES).getValue());
            statement.setString(18, beer.getMethod().getTwist());
            statement.setString(19, beer.getContributor());
            statement.setInt(20, new YeastDAO(connection)
                    .getYeastIdAndInsertIfNotExist(beer.getIngredients().getYeast()));

            statement.execute();
            logger.info("Beer inserted.");
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            beer.setId(resultSet.getInt(1));

            if(beer.getId() != 0){
                new HopListDAO(connection).insertHopList(beer);
                logger.info("Hop List inserted.");
                new MaltListDAO(connection).insertMaltList(beer);
                logger.info("Malt List inserted.");
                new MashTempListDAO(connection).insertMashTempList(beer);
                logger.info("Mash List inserted.");
                new FoodPairingsDAO(connection).insertFoodPairings(beer);
                logger.info("Foods inserted.");
            } else return false;
        } catch (SQLException e) {
            logger.error("SQL state: " + e.getSQLState()
                    + " -> Error code: " + e.getErrorCode() +
                    " -> Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
