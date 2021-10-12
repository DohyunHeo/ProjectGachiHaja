package com.example.projectgachihaja.domain.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateAccountForm {

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "사용할 수 없는 문자를 사용하셨거나 제한 길이를 벗어났습니다.")
    private String nickname;

    @Email
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String password;
}
