package com.github.dmgiangi.brewerhub;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.exceptions.InsertException;
import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.models.BeersList;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;

@SpringBootApplication
public class BrewerHubApplication {
	final static Logger logger = LogManager.getLogger(BrewerHubApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BrewerHubApplication.class, args);
	}
}
