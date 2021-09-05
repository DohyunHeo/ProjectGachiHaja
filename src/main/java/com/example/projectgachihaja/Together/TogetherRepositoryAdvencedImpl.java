package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.QAccount;
import com.example.projectgachihaja.tag.QTag;
import com.example.projectgachihaja.zone.QZone;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TogetherRepositoryAdvencedImpl extends QuerydslRepositorySupport implements TogetherRepositoryAdvenced {
    public TogetherRepositoryAdvencedImpl() {
        super(Together.class);
    }

    @Override
    public Page<Together> findByKeyword(String keyword, Pageable pageable) {
        QTogether together = QTogether.together;

        JPQLQuery<Together> query = from(together).where(together.published.isTrue()
                        .and(together.title.containsIgnoreCase(keyword))
                        .or(together.tags.any().title.containsIgnoreCase(keyword))
                        .or(together.zones.any().city.containsIgnoreCase(keyword)))
                .leftJoin(together.tags, QTag.tag).fetchJoin()
                .leftJoin(together.zones, QZone.zone).fetchJoin()
                .leftJoin(together.managers, QAccount.account).fetchJoin()
                .leftJoin(together.members, QAccount.account).fetchJoin()
                .distinct();
        JPQLQuery<Together> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Together> togetherQueryResults = pageableQuery.fetchResults();
        return new PageImpl<>(togetherQueryResults.getResults(), pageable, togetherQueryResults.getTotal());
    }

    @Override
    public List<Together> findFist5WithNewTogetherByAccount(Account account) {
        QTogether together = QTogether.together;
        JPQLQuery<Together> query = from(together).where(together.published.isTrue()
                .and(together.tags.any().in(account.getTags()))
                .and(together.zones.any().in(account.getZones())))
                .leftJoin(together.tags, QTag.tag).fetchJoin()
                .leftJoin(together.zones, QZone.zone).fetchJoin()
                .orderBy(together.publishedDateTime.desc())
                .distinct()
                .limit(5);

        return query.fetch();
    }
}
