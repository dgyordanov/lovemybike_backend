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

//    @RequestMapping(value = "", method = POST, produces = APPLICATION_JSON_VALUE)
//    public OfferDto postOffer(@RequestBody Offer offer) {
//        return offerService.createOffer(offer);
//    }

    @RequestMapping(value="", method=POST)
    public @ResponseBody String postOffer(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                // TODO: resize the image
                // TODO: upload to AWS - S3
                // TODO: save the offer in the DB
                // TODO: return the created offer with the ID
                return "You successfully uploaded " + name + "!";
            } catch (IOException e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

}
