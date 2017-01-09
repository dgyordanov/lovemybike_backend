package com.livemybike.shop.offers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller which exposes offers endpoints.
 *
 * @author Diyan Yordanov
 */
@RestController
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    private OffersService offerService;

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<OfferDto> read(@RequestParam(value = "gender", required = false) String genderFilter) {
        return offerService.listOffers(genderFilter);
    }

    @RequestMapping(value = "", method = POST, produces = APPLICATION_JSON_VALUE)
    public OfferDto postOffer(@RequestBody Offer offer) {
        return offerService.createOffer(offer);
    }

}
