package com.example.projectgachihaja.domain.Together.event;

import com.example.projectgachihaja.domain.Post.Post;
import com.example.projectgachihaja.domain.Together.Together;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherNoticeEvent {
    private final Together together;
    private final Post post;
    private final String message;
}
