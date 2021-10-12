package com.example.projectgachihaja.domain.Post;

import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.account.UserAccount;
import com.example.projectgachihaja.domain.comment.Comment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor @NoArgsConstructor
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ",
        initialValue = 1,
        allocationSize = 1)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    Long id;

    PostType postType;

    String title;

    String content;

    @ManyToOne
    Account writer;

    int view;

    @ElementCollection
    Set<String> viewerList = new HashSet<>();

    @OneToMany
    Set<Comment> comments = new HashSet<>();

    LocalDateTime reportingDate;

    public boolean isMine(UserAccount userAccount) {
        return this.writer.equals(userAccount.getAccount());
    }
}
