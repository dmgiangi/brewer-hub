package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.entity.Beer;
import com.github.dmgiangi.brewerhub.services.SqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

@RestController
@CrossOrigin
public class RandomBeerController {
    @GetMapping(path = "/beers/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Beer> getRandomBeer(HttpServletResponse response){
        Connection connection = connectionFactory.getConnection();
        Beer beer = new BeersDAO(connection).selectRandomBeer();
        connectionFactory.disconnect(connection);

        return ResponseEntity.ok()
                .body(beer);
    }

    @Autowired
    private final SqlConnectionFactory connectionFactory;
    public RandomBeerController(SqlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
