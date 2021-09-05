package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.Post.PostType;
import com.example.projectgachihaja.Together.event.TogetherCreateEvent;
import com.example.projectgachihaja.Together.event.TogetherJoinEvent;
import com.example.projectgachihaja.Together.event.TogetherUpdateEvent;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.AccountRepository;
import com.example.projectgachihaja.schedule.Schedule;
import com.example.projectgachihaja.schedule.event.ScheduleUpdateEvent;
import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherService {
    private final TogetherRepository togetherRepository;
    private final ApplicationEventPublisher eventPublisher;
    public Together createNewTogether(Together together, Account account) {
        together.getManagers().add(account);
        Together newTogether = togetherRepository.save(together);
        return newTogether;
    }

    public void tagUpdate(Together together, Tag tag) {
        Optional<Together> byId = togetherRepository.findById(together.getId());
        byId.ifPresent(t->t.getTags().add(tag));
    }

    public void zoneUpdate(Together together, Zone zone) {
        Optional<Together> byId = togetherRepository.findById(together.getId());
        byId.ifPresent(t->t.getZones().add(zone));
    }

    public void tagRemove(Together together, Tag tag) {
        Optional<Together> byId = togetherRepository.findById(together.getId());
        byId.ifPresent(t->t.getTags().remove(tag));
    }

    public void zoneRemove(Together together, Zone zone) {
        Optional<Together> byId = togetherRepository.findById(together.getId());
        byId.ifPresent(t->t.getZones().remove(zone));
    }

    public Set<Tag> getTags(Together together) {
        Optional<Together> byId = togetherRepository.findById(together.getId());
        return byId.orElseThrow().getTags();
    }

    public Set<Zone> getZones(Together together) {
        Optional<Together> byId = togetherRepository.findById(together.getId());
        return byId.orElseThrow().getZones();
    }

    public void togetherInfoUpdate(Together together, TogetherForm togetherForm) {
        together.setPath(togetherForm.getPath());
        together.setTitle(togetherForm.getTitle());
        together.setLongIntroduce(togetherForm.getLongIntroduce());
        together.setShortIntroduce(togetherForm.getShortIntroduce());
        togetherRepository.save(together);
    }

    public Together managerCheck(Account account, String path) {
        Together together = togetherRepository.findByPath(path);
        if(!together.getManagers().contains(account)){
            throw new AccessDeniedException("해당 기능을 사용할 권한이 없습니다.");
        }
        return together;
    }

    public void bannerSetting(Together together, boolean b) {
        together.setUseBanner(b);
    }

    public void togetherPublish(Together together) {
        together.published();
        eventPublisher.publishEvent(new TogetherCreateEvent(together));
    }

    public void togetherClose(Together together) {
        together.close();
    }

    public void addTogetherCandidates(Account account, Together together) {
        together.getCandidates().add(account);
        together.candidates_count++;
    }

    public void togetherRegistrationApproval(Together together, Account candidate, Boolean request) {
        if(together.getCandidates().contains(candidate) && request){
            together.getCandidates().remove(candidate);
            together.getMembers().add(candidate);
            together.candidates_count--;
            eventPublisher.publishEvent(new TogetherJoinEvent(together,candidate,"가입이 승인되었습니다."));
        }
        else if (together.getCandidates().contains(candidate) && !request){
            together.getCandidates().remove(candidate);
            together.candidates_count--;
            eventPublisher.publishEvent(new TogetherJoinEvent(together,candidate,"가입이 거부되었습니다."));
        }
    }

    public void newPostRegister(Together together, Post newPost) {
        together.getPosts().add(newPost);
        if (newPost.getPostType() == PostType.NOTICE){
            eventPublisher.publishEvent(new TogetherUpdateEvent(together,"새 공지가 게시되었습니다."));
        }
    }

    public void postDelete(Together together, Post post) {
        together.getPosts().remove(post);
    }

    public List<Together> togetherList(Account account) {
        return togetherRepository.findByMembers(account);
    }

    public void togetherLeave(Account account, Together together) {
        together.getMembers().remove(account);
    }

    public List<Together> createTogetherList(Account account) {
        return togetherRepository.findByManagers(account);
    }

    public void bannerUpdate(Together together, String image) {
        together.setBanner(image);
    }
}
