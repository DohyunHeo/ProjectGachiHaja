package com.example.projectgachihaja.domain.schedule;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleToCalendar {
    Long id;
    String title;
    LocalDateTime start;
    LocalDateTime end;
}
