package com.example.projectgachihaja.domain.account;

import com.example.projectgachihaja.domain.tag.Tag;
import com.example.projectgachihaja.domain.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public String nicknameEncoder(){
        return URLEncoder.encode(nickname, StandardCharsets.UTF_8);
    }

    public Account update(){
        this.password = UUID.randomUUID().toString();
        return this;
    }
}
