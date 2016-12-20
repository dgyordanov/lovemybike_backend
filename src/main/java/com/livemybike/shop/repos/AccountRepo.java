package com.livemybike.shop.repos;

import com.livemybike.shop.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, String> {

    Account findByEmail(String email);

}