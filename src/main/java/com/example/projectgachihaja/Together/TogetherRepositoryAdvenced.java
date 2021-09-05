package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TogetherRepositoryAdvenced {
    Page<Together> findByKeyword(String keyword, Pageable pageable);
    List<Together> findFist5WithNewTogetherByAccount(Account account);
}
