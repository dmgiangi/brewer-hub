package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beers")
public class SingleBeerController {
   @GetMapping("/{id}")
   public Beer getBeer(@PathVariable int id){
      SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
      Beer beer = new BeersDAO(connectionFactory.getConnection()).selectBeerById(id);
      connectionFactory.disconnect();
      return beer;
   }

   @GetMapping("/random")
   public Beer getRandomBeer(){
      SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
      Beer beer = new BeersDAO(connectionFactory.getConnection()).selectRandomBeer();
      connectionFactory.disconnect();
      return beer;
   }
}
