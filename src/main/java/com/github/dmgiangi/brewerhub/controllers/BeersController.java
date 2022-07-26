package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.dao.BeersDAO;
import com.github.dmgiangi.brewerhub.models.entity.BeerReference;
import com.github.dmgiangi.brewerhub.models.entity.BeersList;
import com.github.dmgiangi.brewerhub.services.SqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

@RestController
@CrossOrigin
public class BeersController {

    @GetMapping(path = "/beers-name")
    public ResponseEntity<List<BeerReference>> getBeersName(){
        Connection connection = connectionFactory.getConnection();
        List<BeerReference> resultPage;

        resultPage = new BeersDAO(connection).getBeerName();

        connectionFactory.disconnect(connection);
        return ResponseEntity.ok()
                .body(resultPage);
    }

    @GetMapping(path = "/beers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BeersList> findPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "80") int per_page,
            @RequestParam(required = false) Float abv_gt,
            @RequestParam(required = false) Float abv_lt,
            @RequestParam(required = false) Float ibu_gt,
            @RequestParam(required = false) Float ibu_lt,
            @RequestParam(required = false) Float ebc_gt,
            @RequestParam(required = false) Float ebc_lt,
            @RequestParam(required = false) String beer_name,
            @RequestParam(required = false) String yeast,
            @RequestParam(required = false) String brewed_before,
            @RequestParam(required = false) String brewed_after,
            @RequestParam(required = false) String hops,
            @RequestParam(required = false) String malt,
            @RequestParam(required = false) String food,
            @RequestParam(required = false) String ids) {


        if(page < 1) page = 1;
        if(per_page < 1 || per_page > 80) per_page = 80;

        Connection connection = connectionFactory.getConnection();

        BeersList resultPage;

        resultPage = new BeersDAO(connection).selectBeersList(
                page,
                per_page,
                abv_gt,
                abv_lt,
                ibu_gt,
                ibu_lt,
                ebc_gt,
                ebc_lt,
                beer_name == null ? null : beer_name.trim().replace('_', ' '),
                yeast == null ? null : yeast.trim().replace('_', ' '),
                brewed_before,
                brewed_after,
                hops == null ? null : hops.trim().replace('_', ' '),
                malt == null ? null : malt.trim().replace('_', ' '),
                food == null ? null : food.trim().replace('_', ' '),
                ids
        );
        connectionFactory.disconnect(connection);

        return ResponseEntity.ok()
                .body(resultPage);
    }

    @Autowired
    private SqlConnectionFactory connectionFactory;
}

