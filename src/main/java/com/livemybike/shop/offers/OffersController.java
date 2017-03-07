package com.livemybike.shop.offers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    public List<OfferDto> read(
            @RequestParam(value = "gender", required = false) String genderFilter,
            @RequestParam(value = "location", required = false) String location) {
        return offerService.listOffers(genderFilter, location);
    }

    @RequestMapping(value = "/@my", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<OfferDto> readMyOffers() {
        return offerService.listMyOffers();
    }

    @RequestMapping(value="", method=POST, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody OfferDto postOffer(
            @RequestParam("title") String title,
            @RequestParam("price") String price,
            @RequestParam(value = "gender") String gender,
            @RequestParam("description") String description,
            @RequestParam("street") String street,
            @RequestParam("number") String number,
            @RequestParam("postcode") String postcode,
            @RequestParam("city") String city,
            @RequestParam("image0") MultipartFile image0,
            @RequestParam(value = "image1", required = false) MultipartFile image1,
            @RequestParam(value = "image2", required = false) MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4,
            @RequestParam(value = "image5", required = false) MultipartFile image5) {

            OfferDto offerDto = offerService.createOffer(title, price, gender, description, street,
                    number, postcode, city, image0, image1, image2, image3, image4, image5);
            return offerDto;
    }
}
