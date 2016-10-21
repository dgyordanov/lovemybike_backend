package com.livemybike.shop.api;

import com.livemybike.shop.service.OfferDTO;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public Collection<OfferDTO> read() {
        List<OfferDTO> offers = new ArrayList<>();
        offers.add(new OfferDTO(1, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_1.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "m"));

        offers.add(new OfferDTO(2, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_2.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "w"));

        offers.add(new OfferDTO(3, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_3.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "w"));

        offers.add(new OfferDTO(4, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_4.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "m"));

        offers.add(new OfferDTO(5, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_5.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "m"));

        offers.add(new OfferDTO(6, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_6.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "m"));

        offers.add(new OfferDTO(7, BigDecimal.valueOf(24.99), "Haibaike big curve", "./img/offer_7.jpg"
                , "Short description of this bike which should be at most this size 3-4 rows", "w"));

        return offers;
    }


}
