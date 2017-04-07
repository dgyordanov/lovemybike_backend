package com.livemybike.shop.offers;

import com.livemybike.shop.images.Image;
import com.livemybike.shop.images.ImageRepo;
import com.livemybike.shop.images.ImageStoringException;
import com.livemybike.shop.offers.booking.Booking;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountService;
import com.livemybike.shop.security.AuthorizationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffersServiceImpl implements OffersService {

    private static final int PAGE_SIZE = 12;

    @Autowired
    private OffersRepo offersRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private AccountService accountService;

    @Override
    public Page<OfferDto> listOffers(String genderFilter, String location, int pageNumber) {
        // TODO: find a way to build a dynamic criteria
        if (StringUtils.isEmpty(genderFilter) && StringUtils.isEmpty(location)) {
            // Not filter passed. Return everything.
            return convertToDtoList(offersRepo.findAll(getPage(pageNumber)));
        } else if (!StringUtils.isEmpty(genderFilter) && StringUtils.isEmpty(location)) {
            // only gender filter passed
            List<String> filters = getGenderFiltersList(genderFilter);
            return convertToDtoList(offersRepo.findByGenderIn(filters, getPage(pageNumber)));
        } else if (StringUtils.isEmpty(genderFilter) && !StringUtils.isEmpty(location)) {
            // only location filter passed
            return convertToDtoList(offersRepo.findByCityIgnoreCaseOrPostcodeIgnoreCase(location, location, getPage(pageNumber)));
        } else {
            // location and gender filter passed
            List<String> filters = getGenderFiltersList(genderFilter);
            return convertToDtoList(offersRepo.findByGenderAndLocation(filters, location, getPage(pageNumber)));
        }
    }

    private List<String> getGenderFiltersList(String genderFilter) {
        return genderFilter.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OfferDto createOffer(String title, String price, String gender, String description, String street,
                                String number, String postcode, String city, MultipartFile... images) {
        Offer offer = new Offer();

        if (images == null || images.length == 0 || images.length > 6) {
            throw new IllegalArgumentException("Images should be between 1 and 6");
        }

        Account loggedInAccount = accountService.getCurrentLoggedIn();

        offer.setOwner(loggedInAccount);
        offer.setTitle(title);
        offer.setPrice(new BigDecimal(price));
        offer.setGender(gender);
        offer.setDescription(description);
        offer.setStreet(street);
        offer.setNumber(number);
        offer.setPostcode(postcode);
        offer.setCity(city);

        offer.setImage0(images[0].getOriginalFilename());

        if (images[1] != null) {
            offer.setImage1(images[1].getOriginalFilename());
        }
        if (images[2] != null) {
            offer.setImage2(images[2].getOriginalFilename());
        }
        if (images[3] != null) {
            offer.setImage3(images[3].getOriginalFilename());
        }
        if (images[4] != null) {
            offer.setImage4(images[4].getOriginalFilename());
        }
        if (images[5] != null) {
            offer.setImage5(images[5].getOriginalFilename());
        }

        Offer result = offersRepo.save(offer);

        storeImages(result.getId(), images);

        return convertToDto(result);
    }

    @Override
    public Page<OfferDto> listMyOffers(int pageNumber) {
        Account loggedInAccount = accountService.getCurrentLoggedIn();
        Page<Offer> offers = offersRepo.findByOwner(loggedInAccount, getPage(pageNumber));
        return convertToDtoList(offers);
    }

    @Override
    public List<Booking> getOfferBookings(long offerId) {
        Offer offer = offersRepo.findOne(offerId);
        if (offer == null) {
            throw new IllegalArgumentException(String.format("Offer with ID: %d not found", offerId));
        }
        Account currentUser = accountService.getCurrentLoggedIn();
        if (!offer.getOwner().equals(currentUser) ) {
            throw new AuthorizationException("The offer is not owned by the user");
        }
        return offer.getBookings();
    }

    private OfferDto convertToDto(Offer offer) {
        OfferDto result = modelMapper.map(offer, OfferDto.class);
        result.setOwner_id(offer.getOwner().getId());
        return result;
    }

    private void storeImages(Long offerId, MultipartFile... images) {
        Arrays.stream(images).forEach(image -> {
            if (image != null) {
                try {
                    resizeAndStoreToS3(offerId, image);
                } catch (IOException e) {
                    throw new ImageStoringException("Can not upload the images", e);
                }
            }
        });
    }

    private void resizeAndStoreToS3(Long offerId, MultipartFile imageFile) throws IOException {
        Image image = new Image(imageFile.getInputStream(), imageFile.getOriginalFilename(),
                imageFile.getSize(), imageFile.getContentType());

        imageRepo.save(image.resizeToSmall(), offerId);
        imageRepo.save(image.resizeToMedium(), offerId);
    }

    private PageRequest getPage(int pageNumber) {
        // TODO: think about cache
        return new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "id");
    }

    private Page<OfferDto> convertToDtoList(Page<Offer> offerPage) {
        return offerPage.map(this::convertToDto);
    }

}
