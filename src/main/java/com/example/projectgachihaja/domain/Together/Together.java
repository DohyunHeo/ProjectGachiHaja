package com.example.projectgachihaja.domain.Together;
import com.example.projectgachihaja.domain.Post.Post;
import com.example.projectgachihaja.domain.Post.PostType;
import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.account.UserAccount;
import com.example.projectgachihaja.domain.tag.Tag;
import com.example.projectgachihaja.domain.zone.Zone;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NamedEntityGraph(
        name = "Together.withPostAndAccount",
        attributeNodes = {
                @NamedAttributeNode(value = "posts", subgraph = "writer"),
                @NamedAttributeNode("tags"),
                @NamedAttributeNode("zones"),
                @NamedAttributeNode("managers"),
                @NamedAttributeNode("members"),
                @NamedAttributeNode("candidates")
        },
        subgraphs = {
                @NamedSubgraph(name = "writer", attributeNodes = @NamedAttributeNode("writer")),
        }
)
@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@AllArgsConstructor @NoArgsConstructor
public class Together {
    @Id @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @ManyToMany
    private Set<Account> candidates = new HashSet<>();

    @OneToMany
    private Set<Post> posts = new HashSet<>();

    public int candidates_count;

    @NotBlank
    private String title;

    @Column(unique = true)
    private String path;

    private String shortIntroduce;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String LongIntroduce;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String banner;

    private boolean useBanner;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private boolean published;

    private boolean closed;

    private TogetherType togetherType;

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;



    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && !this.members.contains(account) && !this.managers.contains(account) && !this.candidates.contains(account);
    }

    public boolean isCondidate(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && !this.members.contains(account) && !this.managers.contains(account) && this.candidates.contains(account);
    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public void published() {
        if (!this.closed && !this.published) {
            this.published = true;
            this.publishedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("투게더를 공개할 수 없는 상태입니다. 투개더를 이미 공개했거나 종료 되었습니다.");
        }
    }

    public void close() {
        if (this.published && !this.closed) {
            this.closed = true;
            this.closedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("투게더를 종료할 수 없는 상태입니다. 투개더를 공개하지 않았거나 이미 종료 되었습니다.");
        }
    }

    public String pathEncoder(){
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    public List<Post> getPosts(String type){
        List<Post> userPost = new ArrayList<>();
        switch (type) {
            case "user":
                posts.forEach(post -> {
                    if(post.getPostType() != PostType.NOTICE){
                        userPost.add(post);
                    }
                });
                break;
            case  "notice":
                posts.forEach(post -> {
                    if(post.getPostType() == PostType.NOTICE){
                        userPost.add(post);
                    }
                });
                break;
        }
        return userPost;
    }

    public int numberOfPeople(){
        return this.getManagers().size() + this.getMembers().size();
    }
}
