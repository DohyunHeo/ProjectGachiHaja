package com.example.projectgachihaja.notice;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.Together.TogetherRepository;
import com.example.projectgachihaja.Together.event.TogetherCreateEvent;
import com.example.projectgachihaja.Together.event.TogetherJoinEvent;
import com.example.projectgachihaja.Together.event.TogetherNoticeEvent;
import com.example.projectgachihaja.Together.event.TogetherUpdateEvent;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.AccountRepository;
import com.example.projectgachihaja.Post.CommentNoticeToPostWriterEvent;
import com.example.projectgachihaja.schedule.Schedule;
import com.example.projectgachihaja.schedule.ScheduleRepository;
import com.example.projectgachihaja.schedule.event.ScheduleJoinEvent;
import com.example.projectgachihaja.schedule.event.ScheduleUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Transactional
@Component
@Async
@RequiredArgsConstructor
public class NoticeEventListener {

    private final TogetherRepository togetherRepository;
    private final AccountRepository accountRepository;
    private final NoticeRepository noticeRepository;
    private final ScheduleRepository scheduleRepository;

    @EventListener
    public void togetherCreateEventHandler(TogetherCreateEvent togetherCreateEvent){
        Together together = togetherRepository.findWithTagsAndZonesById(togetherCreateEvent.getTogether().getId());
        List<Account> accounts = accountRepository.findByTagsAndZones(together.getTags(),together.getZones());
        accounts.forEach(account -> {
            createNotice(together,account,together.pathEncoder(),"새로운 투게더가 개설되었습니다.");
        });
    }
    @EventListener
    public void togetherJoinEventHandler(TogetherJoinEvent togetherJoinEvent){
        createNotice(togetherJoinEvent.getTogether(),togetherJoinEvent.getAccount(),togetherJoinEvent.getTogether().pathEncoder(),togetherJoinEvent.getMessage());
    }

    @EventListener
    public void togetherUpdateEventHandler(TogetherUpdateEvent togetherUpdateEvent){
        Together together = togetherRepository.findWithManagersAndMembersById(togetherUpdateEvent.getTogether().getId());
        Set<Account> accounts = new HashSet<>();
        accounts.addAll(together.getManagers());
        accounts.addAll(together.getMembers());
        accounts.forEach(account -> {
            createNotice(together,account,together.pathEncoder(),togetherUpdateEvent.getMessage());
        });
    }

    @EventListener
    public void togetherNoticeEventHandler(TogetherNoticeEvent togetherNoticeEvent){
        Together together = togetherRepository.findWithManagersAndMembersById(togetherNoticeEvent.getTogether().getId());
        Set<Account> accounts = new HashSet<>();
        accounts.addAll(together.getManagers());
        accounts.addAll(together.getMembers());
        accounts.forEach(account -> {
            createNotice(together,account,together.pathEncoder()+"/board/"+togetherNoticeEvent.getPost().getId(),togetherNoticeEvent.getMessage());
        });
    }

    @EventListener
    public void scheduleUpdateEventHandler(ScheduleUpdateEvent scheduleUpdateEvent){
        Together together = togetherRepository.findWithDefaultInfoByPath(scheduleUpdateEvent.getTogether().getPath());
        Schedule schedule = scheduleRepository.findWithMembersById(scheduleUpdateEvent.getSchedule().getId());
        Set<Account> members = together.getMembers();
        members.forEach(account -> {
            createNotice(together,account,together.pathEncoder()+"/schedule/"+schedule.getId(),scheduleUpdateEvent.getMessage());
        });
    }

    @EventListener
    public void scheduleJoinEventHandler(ScheduleJoinEvent scheduleJoinEvent){
        Together together = togetherRepository.findWithDefaultInfoByPath(scheduleJoinEvent.getTogether().getPath());
        Schedule schedule = scheduleRepository.findWithMembersById(scheduleJoinEvent.getSchedule().getId());
        createNotice(together, scheduleJoinEvent.getAccount(),together.pathEncoder()+"/schedule/"+schedule.getId(),scheduleJoinEvent.getMessage());
    }

    @EventListener
    public void commentNoticeToPostWriterEventHandler(CommentNoticeToPostWriterEvent commentEvent){
        createNotice(commentEvent.getTogether(),commentEvent.getAccount(),commentEvent.getTogether().pathEncoder() + "/board/" + commentEvent.getPost().getId(),"회원님의 게시글에 새로운 댓글이 추가되었습니다.");
    }

    private void createNotice(Together together, Account account,String link, String message) {
        Notice notice = new Notice();
        notice.setTitle(together.getTitle());
        notice.setLink("/together/"+link);
        notice.setChecked(false);
        notice.setCreatedTime(LocalDateTime.now());
        notice.setMessage(message);
        notice.setAccount(account);

        noticeRepository.save(notice);
    }
}
