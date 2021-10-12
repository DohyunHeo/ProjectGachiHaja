package com.example.projectgachihaja.domain.comment;

import com.example.projectgachihaja.domain.account.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @EntityGraph(attributePaths = "writer")
    List<Comment> findAllByWriter(Account account);
}
