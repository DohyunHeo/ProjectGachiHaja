package com.example.projectgachihaja.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SettingsForm {
    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$")
    private String nickname;

    @Length(min = 3, max = 30)
    private String newPassword;
    @Length(min = 3, max = 30)
    private String newPasswordConfirm;

    @Length(max = 30)
    private String introduce;

    @Length(max = 50)
    private String job;

    @Length(max = 50)
    private String location;

    private String profileImage;

    private String tagTitle;

    private String zoneCity;

}
