package com.github.dmgiangi.brewerhub;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.models.BeersList;
import com.github.dmgiangi.brewerhub.utilities.DatabasePropertyLoader;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStreamReader;
import java.io.Reader;

@SpringBootApplication
public class BrewerHubApplication {
	final static Logger logger = LogManager.getLogger(BrewerHubApplication.class);

	/*public static void main(String[] args) {
		SpringApplication.run(BrewerHubApplication.class, args);
		System.out.println(new SqlConnectionFactory().getConnection());
	}*/

	public static void main(String[] args) {
        Gson gson = new Gson();
        Reader reader = new BrewerHubApplication().getJsonReaderFromResources("/beers.json");
        BeersList beersList = gson.fromJson(reader, new TypeToken<BeersList>(){}.getType());
		System.out.println(beersList);
		BeersDAO beersDAO = new BeersDAO(new SqlConnectionFactory().getConnection());
		for (Beer beer: beersList) {
			logger.info("START OF INSERTING OF BEER WITH ID: " + beer.getId());
			beersDAO.insertBeer(beer);
			logger.info("END OF INSERTING OF BEER WITH ID: " + beer.getId());
		}
	}

	private Reader getJsonReaderFromResources(String filePath) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(this.getClass().getResourceAsStream(filePath));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return reader;
	}
}
