package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.BeersList;
import com.github.dmgiangi.brewerhub.utilities.SqlConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BeersController {

    @GetMapping(path = "/beers",
            params = {"page" , "per_page"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BeersList findPaginated(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "80") int per_page) {
        if(page < 1) page = 1;
        if(per_page < 1 || per_page > 80) per_page = 80;

        BeersList resultPage;

        SqlConnectionFactory connectionFactory = new SqlConnectionFactory();
        resultPage = new BeersDAO(connectionFactory.getConnection()).selectBeersList(page, per_page);
        connectionFactory.disconnect();

        return resultPage;
    }
}

