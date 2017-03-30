package com.livemybike.shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    public Account getCurrentLoggedIn() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user instanceof AnonymousAuthenticationToken) {
            throw new AnonymousAuthNotAllowedException(
                    "Anonymous user detected");
        }

        User authIdentity = (User) user.getPrincipal();
        return accountRepo.findByEmail(authIdentity.getUsername());
    }
}
