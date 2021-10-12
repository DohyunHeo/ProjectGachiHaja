package com.example.projectgachihaja.domain.Together;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class TogetherForm {

    @NotBlank
    @Length(max = 50, message = "제시된 문자 길이에 맞춰 작성해 주세요.")
    private String title;

    @NotBlank
    @Length(min = 3, max = 20, message = "제시된 문자 길이에 맞춰 작성해 주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,20}$", message = "사용할 수 없는 특수문자가 기입되었습니다.")
    private String path;

    @NotBlank
    @Length(max=100, message = "제시된 문자 길이에 맞춰 작성해 주세요.")
    private String shortIntroduce;

    @NotBlank
    private String LongIntroduce;

    private TogetherType togetherType;

    private String tagTitle;

    private String zoneCity;
}
