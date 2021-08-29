package com.example.projectgachihaja.schedule;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @EntityGraph(attributePaths = "members")
    Schedule findWithMembersById(Long id);
}
