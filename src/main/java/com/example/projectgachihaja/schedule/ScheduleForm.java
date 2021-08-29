package com.example.projectgachihaja.schedule;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ScheduleForm {
    Long id;

    String title;

    String introduce;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end;

    int maxMember;
}
