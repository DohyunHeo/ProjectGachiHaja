package com.example.projectgachihaja.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CreateAccountFormValidator implements Validator {
    private final AccountRepository accountRepository;
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(CreateAccountForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateAccountForm info = (CreateAccountForm) o;
        if(accountRepository.existsByEmailAddress(info.getEmailAddress())){
            errors.rejectValue("emailAddress","invalid.emailAddress", new Object[]{info.getEmailAddress()},"이미 사용중인 이메일 입니다.");
        }
        if(accountRepository.existsByNickname(info.getNickname())){
            errors.rejectValue("nickname","invalid.nickname", new Object[]{info.getNickname()},"이미 사용중인 닉네임 입니다.");
        }
    }
}
