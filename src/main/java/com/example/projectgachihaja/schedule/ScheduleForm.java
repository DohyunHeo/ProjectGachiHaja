package com.example.projectgachihaja.schedule;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
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

    @Min(value = 2, message = "참여인원이 2명 보다 더 적을 수 없습니다.")
    int maxMember;
}
