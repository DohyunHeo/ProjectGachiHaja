package com.example.projectgachihaja.domain.notice;

import com.example.projectgachihaja.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Long countByAccountAndChecked(Account account, boolean b);

    List<Notice> findAllByAccountAndChecked(Account account, boolean b);
    List<Notice> findAllByAccount(Account account);
}
