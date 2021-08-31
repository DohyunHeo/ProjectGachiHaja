package com.example.projectgachihaja.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long>, AccountRepositoryAdvenced {
    Account findByEmailAddress(String id);

    Account findByNickname(String id);

    boolean existsByEmailAddress(String email);

    boolean existsByNickname(String nickname);
}
