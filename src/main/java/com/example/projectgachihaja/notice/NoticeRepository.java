package com.example.projectgachihaja.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
