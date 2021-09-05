package com.example.projectgachihaja.schedule.event;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.schedule.Schedule;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleJoinEvent {
    private final Together together;
    private final Account account;
    private final Schedule schedule;
    private final String message;
}
