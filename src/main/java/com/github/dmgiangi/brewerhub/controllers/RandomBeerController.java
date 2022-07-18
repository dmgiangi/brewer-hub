package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.entity.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RandomBeerController {
    @GetMapping(path = "/beers/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Beer> getRandomBeer(){
        SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
        Beer beer = new BeersDAO(connectionFactory.getConnection()).selectRandomBeer();
        connectionFactory.disconnect();

        HttpHeaders headers = new HttpHeaders();
        headers.add("1", "uno");

        return ResponseEntity.ok()
                .headers(headers)
                .body(beer);
    }
}
