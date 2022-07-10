package com.github.dmgiangi.brewerhub.contrrollers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.Beer;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Controller {
   @GetMapping("/beers/{id}")
   public Beer getBeer(@PathVariable int id){
      return new BeersDAO(new SqlConnectionFactory().getConnection()).selectBeerById(id);
   }
}
