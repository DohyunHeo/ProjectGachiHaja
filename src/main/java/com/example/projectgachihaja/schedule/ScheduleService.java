package com.example.projectgachihaja.schedule;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ModelMapper modelMapper;
    private final ScheduleRepository scheduleRepository;

    public Schedule createNewSchedule(ScheduleForm scheduleForm, Account account) {
        Schedule schedule = modelMapper.map(scheduleForm, Schedule.class);
        schedule.setManager(account);
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
    }
}
