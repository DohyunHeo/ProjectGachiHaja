package com.example.projectgachihaja.account;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long>, AccountRepositoryAdvenced {
    Account findByEmailAddress(String id);
    Optional<Account> findWithOptionalByEmailAddress(String emailAddress);

    Account findByNickname(String id);

    boolean existsByEmailAddress(String email);

    boolean existsByNickname(String nickname);

    @EntityGraph(attributePaths = {"tags","zones"})
    Account findWithTagsAndZonesByNickname(String nickname);
}
