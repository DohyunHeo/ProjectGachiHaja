package com.example.projectgachihaja.Together;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TogetherRepository extends JpaRepository<Together, Long>, TogetherRepositoryAdvenced {

    @EntityGraph(attributePaths = {"members","managers","tags","zones"})
    Together findByPath(String path);

    @EntityGraph(attributePaths = {"candidates"})
    Together findTogetherWithCandidatesByPath(String path);

    @EntityGraph(value = "Together.withPostAndAccount")
    Together findWithPostsByPath(String path);
}
