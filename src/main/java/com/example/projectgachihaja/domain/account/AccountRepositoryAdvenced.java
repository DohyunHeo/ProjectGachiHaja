package com.example.projectgachihaja.domain.account;

import com.example.projectgachihaja.domain.tag.Tag;
import com.example.projectgachihaja.domain.zone.Zone;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface AccountRepositoryAdvenced {
    List<Account> findByTagsAndZones(Set<Tag> tags, Set<Zone> zones);
}
