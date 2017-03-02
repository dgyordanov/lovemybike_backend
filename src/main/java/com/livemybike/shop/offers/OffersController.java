package com.livemybike.shop.offers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "/@my", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<OfferDto> readMyOffers() {
        return offerService.listMyOffers();
    }

//    @RequestMapping(value = "", method = POST, produces = APPLICATION_JSON_VALUE)
//    public OfferDto postOffer(@RequestBody Offer offer) {
//        return offerService.createOffer(offer);
//    }

    @RequestMapping(value="", method=POST)
    public @ResponseBody String postOffer(
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

        if (!image0.isEmpty()) {
            try {
                byte[] bytes = image0.getBytes();
                // TODO: resize the image - small and medium images
                // TODO: upload to AWS - S3
                // TODO: save the offer in the DB
                // TODO: return the created offer with the ID
                return "You successfully uploaded " + title + "!";
            } catch (IOException e) {
                return "You failed to upload " + title + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + title + " because the file was empty.";
        }
    }

}
