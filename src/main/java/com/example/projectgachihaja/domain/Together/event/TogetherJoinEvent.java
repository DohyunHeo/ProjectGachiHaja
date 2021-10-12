package com.example.projectgachihaja.domain.Together.event;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherJoinEvent {
    private final Together together;

    private final Account account;

    private final String message;
}
