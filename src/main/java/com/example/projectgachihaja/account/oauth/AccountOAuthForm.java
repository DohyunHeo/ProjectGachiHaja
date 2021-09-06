package com.example.projectgachihaja.account.oauth;

import lombok.Data;

@Data
public class AccountOAuthForm {
    private String nickname;
    private String emailAddress;
    private String profileImage;
    private boolean emailCheck;
}
