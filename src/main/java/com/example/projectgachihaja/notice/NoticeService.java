package com.example.projectgachihaja.notice;

import com.example.projectgachihaja.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getUncheckedNotice(Account account) {
        return noticeRepository.findAllByAccountAndChecked(account, false);
    }

    public void noticeCheck(NoticeList noticeList) {
        Optional<Notice> notice = noticeRepository.findById(noticeList.id);
        notice.orElseThrow().setChecked(true);
    }

    public void allCheck(Account account) {
        List<Notice> allByAccountAndChecked = noticeRepository.findAllByAccountAndChecked(account, false);
        allByAccountAndChecked.forEach(e -> e.setChecked(true));
    }

    public void noticeRemoveAll(Account account) {
        List<Notice> notices = noticeRepository.findAllByAccount(account);
        if(notices != null) {
            noticeRepository.deleteAll(notices);
        }
    }
}
