package com.github.dmgiangi.brewerhub.dao;

import com.github.dmgiangi.brewerhub.exceptions.InsertException;
import com.github.dmgiangi.brewerhub.models.proto_orm.JdbcBooleanOperator;
import com.github.dmgiangi.brewerhub.models.proto_orm.JdbcConditionBuilder;
import com.github.dmgiangi.brewerhub.models.entity.*;
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
    public Beer getBeerById(int id){
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

    private final static String getBeerByName = "SELECT * FROM beers WHERE beers.name = ?";
    public Beer getBeerByName(String name){
        BeersList beersList = null;

        try (PreparedStatement statement = connection.prepareStatement(getBeerByName)) {
            statement.setString(1, name);
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

    public BeersList selectBeersList(Integer page, Integer per_page, Float abv_gt,
                                     Float abv_lt, Float ibu_gt, Float ibu_lt, Float ebc_gt,
                                     Float ebc_lt, String beer_name, String yeast, String brewed_before,
                                     String brewed_after, String hop, String malt, String food, String ids
    ) throws IllegalArgumentException{
        BeersList beersList = new BeersList();
        String sqlQuery = "SELECT * FROM beers b ";

        JdbcConditionBuilder idConditionBuilder = new JdbcConditionBuilder(JdbcBooleanOperator.OR);
        if (ids != null && !"".equals(ids)) {
            for (String id : ids.split("_")) {
                idConditionBuilder.addIntCondition("b.id = ?", id);
            }
        }

        JdbcConditionBuilder conditionBuilder = new JdbcConditionBuilder(JdbcBooleanOperator.AND);
        conditionBuilder.addCondition(idConditionBuilder.getConditionWithParenthesis())
                .addCondition("b.abv > ?", abv_gt)
                .addCondition("b.abv < ?", abv_lt)
                .addCondition("b.ibu > ?", ibu_gt)
                .addCondition("b.ibu < ?", ibu_lt)
                .addCondition("b.ebc > ?", ebc_gt)
                .addCondition("b.ebc < ?", ebc_lt)
                .addCondition("name LIKE '%?%'", beer_name)
                .addDateCondition(
                    "b.first_brewed >= '?'",
                    brewed_after,
                    "MM-yyyy")
                .addDateCondition(
                    "b.first_brewed <= '?'",
                    brewed_before,
                    "MM-yyyy")
                .addCondition(
                        "EXISTS ( SELECT FROM yeasts y " +
                        "WHERE y.id = b.yeast_id AND y.name LIKE '%?%')", yeast)
                .addCondition(
                        "EXISTS ( " +
                        "SELECT mp.id_beer, mp.id_malt " +
                        "FROM malt_pairings mp " +
                        "INNER JOIN malts m ON mp.id_malt = m.id " +
                        "WHERE mp.id_beer = b.id AND m.name LIKE '%?%')", malt)
                .addCondition(
                    "EXISTS ( " +
                    "SELECT hp.id_hops, hp.id_beers " +
                    "FROM hop_pairings hp " +
                    "INNER JOIN hops h ON h.id = hp.id_hops " +
                    "WHERE hp.id_beers = b.id AND h.name LIKE '%?%')", hop)
                .addCondition(
                    "EXISTS ( " +
                    "SELECT * " +
                    "FROM food_pairings fp " +
                    "INNER JOIN foods f ON fp.food_id = f.id " +
                    "WHERE fp.beer_id = b.id AND f.name LIKE '%?%')", food);

        if(!"".equals(conditionBuilder.getCondition()))
            sqlQuery += "WHERE " + conditionBuilder.getCondition();

        sqlQuery += (" LIMIT " + per_page + " OFFSET " + (per_page * (page - 1)) + ";");
        logger.info(sqlQuery);

        try (Statement statement = connection.createStatement()) {
            if (statement.execute(sqlQuery)) {
                ResultSet resultSet = statement.getResultSet();
                beersList = getBeersFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        //TODO filter beer list per hops malt yeast food
        return beersList;
    }

    private StringBuilder and(StringBuilder stringBuilder) {
        if(!stringBuilder.toString().endsWith("WHERE "))
            stringBuilder.append("AND ");
        return stringBuilder;
    }
    private StringBuilder or(StringBuilder stringBuilder, String suffix) {
        if(stringBuilder.toString().endsWith(suffix))
            stringBuilder.append("OR ");
        return stringBuilder;
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
                            .setFirsBrewed(resultSet.getDate("first_brewed"))
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
    public void insertBeer(Beer beer) throws InsertException {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection
                    .prepareStatement(insertBeer, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, beer.getName());
                statement.setString(2, beer.getTagline());
                statement.setDate(3, beer.getFirsBrewed());
                statement.setString(4, beer.getDescription());
                statement.setString(5, beer.getImageUrl());
                statement.setObject(6, beer.getAbv(), Types.FLOAT);
                statement.setObject(7, beer.getIbu(), Types.FLOAT);
                statement.setObject(8, beer.getTargetFg(), Types.FLOAT);
                statement.setObject(9, beer.getTargetOg(), Types.FLOAT);
                statement.setObject(10, beer.getEbc(), Types.FLOAT);
                statement.setObject(11, beer.getSrm(), Types.FLOAT);
                statement.setObject(12, beer.getPh(), Types.FLOAT);
                statement.setObject(13, beer.getAttenuationLevel(), Types.FLOAT);
                statement.setObject(
                        14,
                        beer.getMethod()
                                .getFermentation()
                                .getTemperature()
                                .setUnit(TemperatureUnits.CELSIUS)
                                .getValue(),
                        Types.FLOAT
                );
                statement.setString(15, beer.getMethod().getTwist());
                statement.setObject(16, beer.getVolume().setUnit(VolumeUnits.LITRES).getValue());
                statement.setObject(17, beer.getBoilVolume().setUnit(VolumeUnits.LITRES).getValue());
                statement.setString(18, beer.getMethod().getTwist());
                statement.setString(19, beer.getContributor());
                statement.setObject(
                        20,
                        beer.getIngredients().getYeast() != null
                                ? new YeastDAO(connection)
                                .getYeastIdAndInsertIfNotExist(beer.getIngredients().getYeast())
                                : null
                        , Types.INTEGER);

                if(statement.executeUpdate() != 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        beer.setId(generatedKeys.getInt(1));

                        if (beer.getId() != 0) {
                            new HopListDAO(connection).insertHopList(beer);
                            new MaltListDAO(connection).insertMaltList(beer);
                            new MashTempListDAO(connection).insertMashTempList(beer);
                            new FoodPairingsDAO(connection).insertFoodPairings(beer);
                        }
                        connection.commit();
                        logger.info("Beer with id: " + beer.getId() + " inserted. Transaction committed.");
                    }
                    else {
                        connection.rollback();
                        logger.error("Creating new BEER failed, no ID obtained.");
                        throw new InsertException("Creating new BEER failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Creating new BEER failed");
                throw new InsertException("Creating new BEER failed", e);
            }
        } catch (SQLException e) {
            logger.error("unable to set auto-commit = false");
            throw new InsertException("Cannot connect to the database", e);
        }
    }

    private static final String selectRandomBeer =
            "SELECT * FROM beers ORDER BY RAND() LIMIT 1;";
    public Beer selectRandomBeer() {
        BeersList beersList = null;

        try (PreparedStatement statement = connection.prepareStatement(selectRandomBeer)) {
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
}
