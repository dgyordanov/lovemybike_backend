package com.livemybike.shop.security;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Override
    public boolean equals(Object secondAccount) {
        if (secondAccount instanceof Account) {
            return getId() != null && getId().equals(((Account) secondAccount).getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.intValue() : 0;
    }

}
