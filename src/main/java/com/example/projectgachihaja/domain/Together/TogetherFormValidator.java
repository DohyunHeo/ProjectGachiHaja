package com.example.projectgachihaja.domain.Together;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TogetherFormValidator implements Validator {
    private final TogetherRepository togetherRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(TogetherForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TogetherForm togetherForm = (TogetherForm) o;
        if(togetherRepository.existsByTitle(togetherForm.getTitle())){
            errors.rejectValue("title","invalid.title", new Object[]{togetherForm.getTitle()},"이미 사용중인 이름 입니다.");
        }
        if(togetherRepository.existsByPath(togetherForm.getPath())){
            errors.rejectValue("path","invalid.path", new Object[]{togetherForm.getTitle()},"이미 사용중인 경로 이름 입니다.");
        }
    }
}
