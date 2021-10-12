package com.example.projectgachihaja.domain.schedule.event;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.schedule.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleUpdateEvent {
    private final Together together;
    private final Schedule schedule;
    private final String message;
}
