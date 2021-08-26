package com.example.projectgachihaja.Together;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class TogetherForm {

    @NotBlank
    private String title;

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,20}$")
    private String path;

    @NotBlank
    @Length(max=100)
    private String shortIntroduce;

    @NotBlank
    private String LongIntroduce;

    private TogetherType togetherType;

    private String tagTitle;

    private String zoneCity;
}
