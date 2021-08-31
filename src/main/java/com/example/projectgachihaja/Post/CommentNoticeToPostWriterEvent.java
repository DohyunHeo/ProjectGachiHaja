package com.example.projectgachihaja.Post;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentNoticeToPostWriterEvent {
    private final Account account;

    private final Together together;

    private final Post post;
}
