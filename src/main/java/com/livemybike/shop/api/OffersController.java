package com.livemybike.shop.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livemybike.shop.service.OfferDTO;

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
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setAddress("Strasse 1, 13404 Berlin");
        offerDTO.setConstructionDate(LocalDate.of(2009, Month.SEPTEMBER, 12));
        offerDTO.setPrice(BigDecimal.valueOf(55.99));
        offerDTO.setModel("Drag X3");
        return Arrays.asList(offerDTO);
    }
}
