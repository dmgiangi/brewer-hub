package com.github.dmgiangi.brewerhub.contrrollers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingleBeerController {
   @GetMapping("/beers/{id}")
   public Beer getBeer(@PathVariable int id){
      SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
      Beer beer = new BeersDAO(connectionFactory.getConnection()).selectBeerById(id);
      connectionFactory.disconnect();
      return beer;
   }
}
