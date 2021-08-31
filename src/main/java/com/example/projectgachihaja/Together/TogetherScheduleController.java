package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.Post.PostRepository;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.AccountRepository;
import com.example.projectgachihaja.account.CurrentAccount;
import com.example.projectgachihaja.schedule.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TogetherScheduleController {
    private final TogetherRepository togetherRepository;
    private final TogetherService togetherService;
    private final PostRepository postRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    @GetMapping("/together/{path}/schedule")
    public String togetherScheduleView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Together together = togetherRepository.findWithSchedulesByPath(path);
        model.addAttribute(together);
        model.addAttribute(account);
        return "together/schedule/view";
    }

    @GetMapping("/together/{path}/schedule/load")
    @ResponseBody
    public List<ScheduleToCalendar> scheduleToCalendarView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        List<Schedule> schedules = scheduleRepository.findAll();
        return modelMapper.map(schedules, new TypeToken<List<ScheduleToCalendar>>() {}.getType());
    }

    @GetMapping("/together/{path}/schedule/create")
    public String togetherScheduleCreate(@CurrentAccount Account account, @PathVariable String path, String startDate, Model model){
        Together together = togetherRepository.findWithSchedulesByPath(path);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss.SSS");
        startDate = startDate + "00:00:00.000";
        ScheduleForm scheduleForm = new ScheduleForm();
        scheduleForm.setStart(LocalDateTime.parse(startDate,formatter));
        model.addAttribute(together);
        model.addAttribute(scheduleForm);
        return "/together/schedule/create";
    }

    @PostMapping("/together/{path}/schedule/create")
    public String togetherScheduleCreateComplete(@CurrentAccount Account account, @PathVariable String path, ScheduleForm scheduleForm) {
        Together together = togetherRepository.findWithSchedulesByPath(path);
        Schedule newSchedule = scheduleService.createNewSchedule(scheduleForm, account);
        togetherService.addSchedule(together,newSchedule);

        return "redirect:/together/"+path+"/schedule";
    }

    @GetMapping("/together/{path}/schedule/{id}")
    public String togetherScheduleInfo(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id, Model model) {
        Together together = togetherRepository.findWithSchedulesByPath(path);
        Schedule schedule = scheduleRepository.findWithMembersById(id);
        model.addAttribute(schedule);
        model.addAttribute(together);
        model.addAttribute(account);
        return "/together/schedule/info";
    }

    @PostMapping("/together/{path}/schedule/{id}/join")
    public String togetherScheduleJoin(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id, Model model) {
        Together together = togetherRepository.findWithSchedulesByPath(path);
        Schedule schedule = scheduleRepository.findWithMembersById(id);
        if(schedule.getCandidates().contains(account)){
            scheduleService.cancelScheduleCandidates(account,schedule);
        }else {
            scheduleService.addScheduleCandidates(account, schedule);
        }
        return "redirect:/together/"+path+"/schedule/"+id;
    }

    @PostMapping("/together/{path}/schedule/{id}/candidate")
    public String togetherScheduleManagement(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id, String nickname) {
        Schedule schedule = scheduleRepository.findWithMembersById(id);
        Account candidate = accountRepository.findByNickname(nickname);
        scheduleService.scheduleRegistrationApproval(candidate, schedule);


        return "redirect:/together/"+path+"/schedule/"+id;
    }

    @GetMapping("/together/{path}/schedule/{id}/edit")
    public String togetherScheduleEdit(@CurrentAccount Account account, @PathVariable String path,@PathVariable Long id, Model model){
        Together together = togetherRepository.findWithSchedulesByPath(path);
        Schedule schedule = scheduleRepository.findWithMembersById(id);
        ScheduleForm scheduleForm = modelMapper.map(schedule, ScheduleForm.class);
        model.addAttribute(scheduleForm);
        model.addAttribute(together);
        model.addAttribute(account);
        return "/together/schedule/edit";
    }

    @PostMapping("/together/{path}/schedule/{id}/edit")
    public String togetherScheduleEditComplete(@CurrentAccount Account account, @PathVariable String path,@PathVariable Long id, ScheduleForm scheduleForm){
        Together together = togetherRepository.findWithSchedulesByPath(path);
        Schedule schedule = scheduleRepository.findWithMembersById(id);
        scheduleService.editSchedule(schedule, scheduleForm);
        return "redirect:/together/"+path+"/schedule/"+id;
    }

    @PostMapping("/together/{path}/schedule/{id}/remove")
    public String togetherScheduleRemove(@CurrentAccount Account account, @PathVariable String path,@PathVariable Long id){
        Together together = togetherRepository.findWithSchedulesByPath(path);
        Schedule schedule = scheduleRepository.findWithMembersById(id);
        togetherService.removeSchedule(schedule,together);
        scheduleService.removeSchedule(schedule);
        return "redirect:/together/"+path+"/schedule";
    }


}
