package com.livemybike.shop.api;

import com.livemybike.shop.entities.Offer;
import com.livemybike.shop.repos.OffersRepo;
import com.livemybike.shop.service.OfferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Rest controller which exposes offers endpoints.
 *
 * @author Diyan Yordanov
 */
@RestController
@RequestMapping("/offers")
public class OffersController {

    private static final Map<String, String> okResponseBody = new HashMap<>();

    static {
        okResponseBody.put("status", "ok");
    }

    @Autowired
    private OffersRepo offersRepo;

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public Iterable<Offer> read() {
        return offersRepo.findAll();
    }


}
