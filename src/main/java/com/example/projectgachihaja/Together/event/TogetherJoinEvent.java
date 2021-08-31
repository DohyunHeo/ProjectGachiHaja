package com.example.projectgachihaja.Together.event;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherJoinEvent {
    private final Together together;

    private final Account account;

    private final String message;
}
