package com.example.projectgachihaja.notice;

import com.example.projectgachihaja.account.Account;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    @Id @GeneratedValue
    Long id;

    @ManyToOne
    Account account;

    String title;

    String message;

    String link;

    boolean checked;

    LocalDateTime createdTime;
}
