package com.example.projectgachihaja.Post;

import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class PostForm {
    @Id
    Long id;

    PostType postType;

    String title;

    String content;

    LocalDateTime reportingDate;
}
