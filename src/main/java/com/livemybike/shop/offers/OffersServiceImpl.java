package com.livemybike.shop.offers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;
import com.livemybike.shop.security.AnonymousAuthNotAllowedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OffersServiceImpl implements OffersService {

    @Autowired
    private OffersRepo offersRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OfferDto> listOffers(String genderFilter) {
        if (StringUtils.isEmpty(genderFilter)) {
            // Not filter passed. Return everything.
            return convertToDtoList(offersRepo.findAll());
        } else {
            List<String> filters = genderFilter.chars()
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.toList());
            return convertToDtoList(offersRepo.findByGenderIn(filters));
        }
    }

    @Override
    public OfferDto createOffer(Offer offer) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user instanceof AnonymousAuthenticationToken) {
            throw new AnonymousAuthNotAllowedException(
                    "User should be logged in before to post an offer");
        }

        User authIdentity = (User) user.getPrincipal();
        authIdentity.getUsername();
        Account loggedInAccount = accountRepo.findByEmail(authIdentity.getUsername());
        offer.setOwner(loggedInAccount);

        Offer result = offersRepo.save(offer);

        return convertToDto(result);
    }

    private List<OfferDto> convertToDtoList(Iterable<Offer> offers) {
        return StreamSupport.stream(offers.spliterator(), false)
                .map(offer -> convertToDto(offer))
                .collect(Collectors.toList());
    }

    private OfferDto convertToDto(Offer offer) {
        OfferDto result = modelMapper.map(offer, OfferDto.class);
        result.setOwner_id(offer.getOwner().getId());
        return result;
    }

}
