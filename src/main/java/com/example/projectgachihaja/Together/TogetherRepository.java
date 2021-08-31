package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.schedule.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TogetherRepository extends JpaRepository<Together, Long>, TogetherRepositoryAdvenced {

    @EntityGraph(attributePaths = {"members","candidates","managers","tags","zones"})
    Together findByPath(String path);

    @EntityGraph(attributePaths = {"schedules","members","managers"})
    Together findWithSchedulesByPath(String path);

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

    Together findBySchedules(Schedule schedule);

    List<Together> findByManagers(Account account);
}
