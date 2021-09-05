package com.example.projectgachihaja.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduleFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ScheduleForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ScheduleForm schedule = (ScheduleForm) o;

        if(LocalDateTime.now().isAfter(schedule.getStart())){
            errors.rejectValue("start","invalid.datetime", new Object[]{schedule.getStart()},"모임 시작일이 모임 작성일 이전 일 수 없습니다.");
        }
        if(LocalDateTime.now().isAfter(schedule.getEnd())){
            errors.rejectValue("end","invalid.datetime", new Object[]{schedule.getEnd()},"모임 종료일이 모임 작성일 이전 일 수 없습니다.");
        }
        if(schedule.getStart().isAfter(schedule.getEnd())){
            errors.rejectValue("start","invalid.datetime", new Object[]{schedule.getStart()},"모임 시작일이 모임 종료일 이후 일 수 없습니다.");
        }
    }
    public void updateValidate(ScheduleForm scheduleForm,Schedule schedule, Errors errors){
        if(schedule.getMembers().size() > scheduleForm.maxMember){
            errors.rejectValue("maxMember","invalid.maxMember", new Object[]{scheduleForm.getMaxMember()},"현재 모집된 인원수 보다 더 적을 수 없습니다.");
        }
    }
}
