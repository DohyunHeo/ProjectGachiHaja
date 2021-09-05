package com.example.projectgachihaja.Post;

import com.example.projectgachihaja.Together.Together;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PostRepositoryAdvenced {
    Page<Post> findWithPostTitleByKeyword(String keyword, Pageable pageable);
    Page<Post> findWithWriterByKeyword(String keyword, Pageable pageable);
}
