package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.entity.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.RejectedExecutionException;

@RestController
@CrossOrigin
public class SingleBeerController {
   @GetMapping(path = "/beers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Beer> getBeer(@PathVariable int id){

      SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
      Beer beer = new BeersDAO(connectionFactory.getConnection()).getBeerById(id);
      connectionFactory.disconnect();

      HttpHeaders headers = new HttpHeaders();
      headers.add("1", "uno");

      return ResponseEntity.ok()
              .headers(headers)
              .body(beer);
   }
}
