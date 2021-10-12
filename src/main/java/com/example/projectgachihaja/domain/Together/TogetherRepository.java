package com.example.projectgachihaja.domain.Together;

import com.example.projectgachihaja.domain.account.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TogetherRepository extends JpaRepository<Together, Long>, TogetherRepositoryAdvenced {

    @EntityGraph(attributePaths = {"members","candidates","managers","tags","zones"})
    Together findByPath(String path);

    @EntityGraph(attributePaths = {"members","candidates","managers"})
    Together findWithDefaultInfoByPath(String path);

    @EntityGraph(attributePaths = {"candidates"})
    Together findTogetherWithCandidatesByPath(String path);

    @EntityGraph(value = "Together.withPostAndAccount")
    Together findWithPostsByPath(String path);

    @EntityGraph(attributePaths = {"members","managers","tags","zones"})
    List<Together> findFirst8ByPublishedAndClosedOrderByPublishedDateTimeDesc(boolean b, boolean b1);

    List<Together> findByMembers(Account account);

    @EntityGraph(attributePaths = {"tags","zones"})
    Together findWithTagsAndZonesById(Long id);

    @EntityGraph(attributePaths ={"members","managers"})
    Together findWithManagersAndMembersById(Long id);

    List<Together> findByManagers(Account account);

    boolean existsByTitle(String title);

    boolean existsByPath(String path);

    List<Together> findAllByManagers(Account account);

    List<Together> findAllByMembers(Account account);

    List<Together> findAllByCandidates(Account account);
}
