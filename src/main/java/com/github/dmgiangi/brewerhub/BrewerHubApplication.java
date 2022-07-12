package com.github.dmgiangi.brewerhub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BrewerHubApplication extends SpringBootServletInitializer {
	final static Logger logger = LogManager.getLogger(BrewerHubApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BrewerHubApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BrewerHubApplication.class);
	}
}
