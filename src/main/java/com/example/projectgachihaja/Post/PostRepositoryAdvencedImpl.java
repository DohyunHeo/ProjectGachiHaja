package com.example.projectgachihaja.Post;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.QAccount;
import com.example.projectgachihaja.tag.QTag;
import com.example.projectgachihaja.zone.QZone;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PostRepositoryAdvencedImpl extends QuerydslRepositorySupport implements PostRepositoryAdvenced{
    public PostRepositoryAdvencedImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findWithPostTitleByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post).where((post.title.containsIgnoreCase(keyword)));
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> postQueryResults = pageableQuery.fetchResults();
        return new PageImpl<>(postQueryResults.getResults(), pageable, postQueryResults.getTotal());
    }

    @Override
    public Page<Post> findWithWriterByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post).where((post.writer.nickname.containsIgnoreCase(keyword)));
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> postQueryResults = pageableQuery.fetchResults();
        return new PageImpl<>(postQueryResults.getResults(), pageable, postQueryResults.getTotal());
    }
}
