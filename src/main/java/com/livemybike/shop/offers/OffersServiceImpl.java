package com.livemybike.shop.offers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ModelMapper modelMapper;

    @Override
    public Iterable<Offer> listOffers(String genderFilter) {
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

    @Override
    public Offer createOffer(Offer offer) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User authIdentity = (User) user.getPrincipal();
        authIdentity.getUsername();

        Offer result = offersRepo.save(offer);

        return result;
    }

}
