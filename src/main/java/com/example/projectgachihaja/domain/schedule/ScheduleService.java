package com.example.projectgachihaja.domain.schedule;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.schedule.event.ScheduleJoinEvent;
import com.example.projectgachihaja.domain.schedule.event.ScheduleUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ModelMapper modelMapper;
    private final ScheduleRepository scheduleRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Schedule createNewSchedule(ScheduleForm scheduleForm, Together together, Account account) {
        Schedule schedule = modelMapper.map(scheduleForm, Schedule.class);
        schedule.setManager(account);
        schedule.setTogether(together);
        eventPublisher.publishEvent(new ScheduleUpdateEvent(schedule.getTogether(),schedule,"새로운 일정이 등록되었습니다."));
        return scheduleRepository.save(schedule);
    }

    public void addScheduleCandidates(Account account, Schedule schedule) {
        schedule.getCandidates().add(account);
    }

    public void cancelScheduleCandidates(Account account, Schedule schedule) {
        schedule.getCandidates().remove(account);
    }

    public void scheduleRegistrationApproval(Account candidate, Schedule schedule) {
        schedule.getCandidates().remove(candidate);
        schedule.getMembers().add(candidate);
        eventPublisher.publishEvent(new ScheduleJoinEvent(schedule.getTogether(), candidate,schedule,"일정 참석이 승인되었습니다."));
    }

    public void editSchedule(Schedule schedule, ScheduleForm scheduleForm) {
        schedule.setIntroduce(scheduleForm.getIntroduce());
        schedule.setStart(scheduleForm.getStart());
        schedule.setEnd(scheduleForm.getEnd());
        schedule.setTitle(scheduleForm.getTitle());
        eventPublisher.publishEvent(new ScheduleUpdateEvent(schedule.getTogether(),schedule,"일정의 정보가 수정되었습니다."));
    }

    public void removeSchedule(Schedule schedule) {
        eventPublisher.publishEvent(new ScheduleUpdateEvent(schedule.getTogether(),schedule,"일정이 취소되었습니다."));
        schedule.getMembers().clear();
        schedule.getCandidates().clear();
        scheduleRepository.delete(schedule);
    }

    public void scheduleRemoveAll(Account account) {
        List<Schedule> schedules = scheduleRepository.findAllByManager(account);
        if(schedules != null) {
            schedules.forEach(schedule -> {
                schedule.getMembers().clear();
                schedule.getCandidates().clear();
            });
            scheduleRepository.deleteAll(schedules);
        }

        List<Schedule> members = scheduleRepository.findAllByMembers(account);
        if(members != null) {
            members.forEach(member -> {
                member.getMembers().remove(account);
            });
            scheduleRepository.deleteAll(members);
        }

        List<Schedule> candidates = scheduleRepository.findAllByCandidates(account);
        if(candidates != null) {
            candidates.forEach(candidate -> {
                candidate.getCandidates().remove(account);
            });
            scheduleRepository.deleteAll(candidates);
        }
    }
}
