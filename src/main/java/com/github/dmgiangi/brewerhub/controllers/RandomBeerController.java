package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.entity.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class RandomBeerController {

    @GetMapping(path = "/beers/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Beer> getRandomBeer(HttpServletResponse response){

        SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
        Beer beer = new BeersDAO(connectionFactory.getConnection()).selectRandomBeer();
        connectionFactory.disconnect();

        HttpHeaders headers = new HttpHeaders();
        response.getHeaderNames()
                .forEach(header ->
                        headers.add(header, response.getHeader(header)));

        return ResponseEntity.ok()
                .headers(headers)
                .body(beer);
    }
}
