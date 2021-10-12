package com.example.projectgachihaja.domain.Post;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.account.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentNoticeToPostWriterEvent {
    private final Account account;

    private final Together together;

    private final Post post;
}
