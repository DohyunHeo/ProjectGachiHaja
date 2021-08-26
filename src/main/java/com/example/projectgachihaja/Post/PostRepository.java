package com.example.projectgachihaja.Post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryAdvenced {

    @EntityGraph(attributePaths = {"comments","writer"})
    Post findWithCommentsById(Long postid);
}
