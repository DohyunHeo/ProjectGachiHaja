package com.example.projectgachihaja.account;

import com.example.projectgachihaja.tag.QTag;
import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.zone.QZone;
import com.example.projectgachihaja.zone.Zone;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

public class AccountRepositoryAdvencedImpl extends QuerydslRepositorySupport implements AccountRepositoryAdvenced {
    public AccountRepositoryAdvencedImpl() {
        super(Account.class);
    }

    @Override
    public List<Account> findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;
        JPQLQuery<Account> query = from(account).where(account.tags.any().in(tags).and(account.zones.any().in(zones)))
                .leftJoin(account.tags, QTag.tag).fetchJoin()
                .leftJoin(account.zones, QZone.zone).fetchJoin()
                .distinct();
        return query.fetch();
    }
}
