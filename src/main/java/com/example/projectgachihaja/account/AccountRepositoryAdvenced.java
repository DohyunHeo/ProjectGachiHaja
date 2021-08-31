package com.example.projectgachihaja.account;

import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.zone.Zone;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface AccountRepositoryAdvenced {
    List<Account> findByTagsAndZones(Set<Tag> tags, Set<Zone> zones);
}
