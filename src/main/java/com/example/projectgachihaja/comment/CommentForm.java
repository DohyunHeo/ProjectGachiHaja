package com.example.projectgachihaja.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentForm {
    String content;

    LocalDateTime reportingDate;
}
