package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.AccountRepository;
import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherService {
    private final TogetherRepository togetherRepository;
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
        Together byPath = togetherRepository.findByPath(path);
        if(!byPath.getManagers().contains(account)){
            throw new AccessDeniedException("해당 기능을 사용할 권한이 없습니다.");
        }
        return byPath;
    }

    public void bannerSetting(Together together, boolean b) {
        together.setUseBanner(b);
    }

    public void togetherPublish(Together together) {
        together.published();
    }

    public void togetherClose(Together together) {
        together.close();
    }

    public void addTogetherCandidates(Account account, Together together) {
        together.getCandidates().add(account);
        together.candidates_count++;
    }

    public void togetherRegistrationApproval(Together together, Account candidate) {
        if(together.getCandidates().contains(candidate)){
            together.getCandidates().remove(candidate);
            together.getMembers().add(candidate);
            together.candidates_count--;
        }
        else{
            throw new AccessDeniedException("해당 기능을 사용할 권한이 없습니다.");
        }
    }

    public void newPostRegister(Together together, Post newPost) {
        together.getPosts().add(newPost);
    }

    public void postDelete(Together together, Post post) {
        together.getPosts().remove(post);
    }

    public void manymanyCreate(Account account) {
        for(int i= 1 ; i <=30 ; i++){
            Together together = new Together();
            together.getManagers().add(account);
            together.setPath("Test"+i);
            together.setTitle("많을 수록 좋은 테스트 " +i);
            together.setLongIntroduce("많아많아");
            together.setShortIntroduce("많아요");
            togetherRepository.save(together);
        }
    }
}
