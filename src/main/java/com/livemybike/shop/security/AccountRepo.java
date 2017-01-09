package com.livemybike.shop.security;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, String> {

    Account findByEmail(String email);

}