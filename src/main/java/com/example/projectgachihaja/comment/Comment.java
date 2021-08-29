package com.example.projectgachihaja.comment;

import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.UserAccount;
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
public class Comment {
    @Id @GeneratedValue
    Long id;

    @ManyToOne
    Account writer;

    String content;

    LocalDateTime reportingDate;

    public boolean isMine(UserAccount userAccount) {
        return this.writer.equals(userAccount.getAccount());
    }

}
