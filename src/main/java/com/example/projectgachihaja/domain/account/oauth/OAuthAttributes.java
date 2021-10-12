package com.example.projectgachihaja.domain.account.oauth;

import com.example.projectgachihaja.domain.account.Account;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String emailAddress;
    private String profileImage;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.nickname = name;
        this.emailAddress = email;
        this.profileImage = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public Account toEntity() {
        return Account.builder()
                .nickname(nickname)
                .emailAddress(emailAddress)
                .profileImage(profileImage)
                .password(UUID.randomUUID().toString())
                .emailCheck(true)
                .build();
    }

}
