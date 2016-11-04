package com.livemybike.shop.api;

import com.livemybike.shop.entities.Offer;
import com.livemybike.shop.repos.OffersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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
    public Iterable<Offer> read(@RequestParam(value = "gender", required = false) String genderFilter) {
        if (StringUtils.isEmpty(genderFilter)) {
            // Not filter passed. Return everything.
            return offersRepo.findAll();
        } else {
            List<String> filters = genderFilter.chars()
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.toList());
            return offersRepo.findByGenderIn(filters);
        }
    }


}
