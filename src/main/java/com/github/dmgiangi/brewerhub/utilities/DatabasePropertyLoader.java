package com.github.dmgiangi.brewerhub.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class DatabasePropertyLoader extends Properties{
    final static Logger logger = LoggerFactory.getLogger(DatabasePropertyLoader.class);

    public DatabasePropertyLoader() {
        try {
            this.load(
                    new BufferedReader(
                            new InputStreamReader(
                                    this.getClass()
                                            .getResourceAsStream("/database.properties")
                            )));
            logger.info("Database property file loaded.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
