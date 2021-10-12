package com.example.projectgachihaja.domain.Post;

import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.comment.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryAdvenced {

    @EntityGraph(attributePaths = {"comments","writer"})
    Post findWithCommentsById(Long postid);

    List<Post> findAllByWriter(Account account);

    Post findByComments(Comment comment);
}
