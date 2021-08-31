package com.example.projectgachihaja.schedule;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleParticipationEvent {
    private final Schedule schedule;
    private final Account account;
}
