package com.example.projectgachihaja.account;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id @GeneratedValue
    Long id;

    @Column(unique = true)
    String nickname;

    @Column(unique = true)
    String emailAddress;

    String password;

    String emailToken;

    @Lob @Basic(fetch = FetchType.EAGER)
    String profileImage;

    String introduce;

    String job;

    String Location;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    boolean emailCheck;

    LocalDateTime emailTokenGeneratedAt;

    public void generateEmailToken() {
        this.emailToken = UUID.randomUUID().toString();
        this.emailTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return emailToken.equals(token);
    }
}
