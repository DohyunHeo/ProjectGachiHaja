package com.example.projectgachihaja.Together.event;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.Together.Together;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherNoticeEvent {
    private final Together together;
    private final Post post;
    private final String message;
}
