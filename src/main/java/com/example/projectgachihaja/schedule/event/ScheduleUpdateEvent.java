package com.example.projectgachihaja.schedule.event;

import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.schedule.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleUpdateEvent {
    private final Schedule schedule;
    private final Account account;
    private final String message;
}
