package com.livemybike.shop.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepo accountRepo;

    @RequestMapping(value = "", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account savedAccount = accountRepo.save(account);
        return ResponseEntity.ok(savedAccount);
    }

}
