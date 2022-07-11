package com.github.dmgiangi.brewerhub.contrrollers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomBeerController {
   @GetMapping("/beers/random")
   public Beer getRandomBeer(){
      SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
      Beer beer = new BeersDAO(connectionFactory.getConnection()).selectRandomBeer();
      connectionFactory.disconnect();
      return beer;
   }
}
