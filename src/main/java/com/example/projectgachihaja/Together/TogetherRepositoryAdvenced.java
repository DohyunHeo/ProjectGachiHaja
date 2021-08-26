package com.example.projectgachihaja.Together;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TogetherRepositoryAdvenced {

    Page<Together> findByKeyword(String keyword, Pageable pageable);
}
