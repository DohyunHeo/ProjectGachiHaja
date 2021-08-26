package com.example.projectgachihaja.account;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {
    private Account account;

    public UserAccount(Account account, List<SimpleGrantedAuthority> role_user) {
        super(account.getNickname(), account.getPassword(), role_user);
        this.account = account;
    }
}