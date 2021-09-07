package com.example.projectgachihaja.schedule;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @EntityGraph(attributePaths = "members")
    Schedule findWithMembersById(Long id);

    @EntityGraph(attributePaths = "together")
    List<Schedule> findAllByMembersAndStartAfterOrderByStartDesc(Account account, LocalDateTime now);

    List<Schedule> findAllByTogether(Together together);

    List<Schedule> findAllByManager(Account account);

    List<Schedule> findAllByMembers(Account account);

    List<Schedule> findAllByCandidates(Account account);
}
