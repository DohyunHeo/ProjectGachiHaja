package com.example.projectgachihaja.domain.schedule;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.account.UserAccount;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id @GeneratedValue
    Long id;

    @Column(nullable = false)
    String title;

    @Lob
    String introduce;

    @Column(nullable = false, name = "startDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime start;

    @Column(nullable = false, name = "endDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end;

    @Column(nullable = false)
    int maxMember;

    @ManyToOne
    private Account manager;

    @ManyToOne
    private Together together;

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @ManyToMany
    private Set<Account> candidates = new HashSet<>();

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.members.contains(account);
    }

    public boolean isCondidate(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return !this.members.contains(account) && this.candidates.contains(account);
    }
}
