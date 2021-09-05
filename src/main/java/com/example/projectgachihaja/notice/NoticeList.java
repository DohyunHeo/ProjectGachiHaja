package com.example.projectgachihaja.notice;

import com.example.projectgachihaja.comment.Comment;
import lombok.Data;

import java.util.Comparator;

@Data
public class NoticeList {
    Long id;
    String title;
    String message;
    String link;

    public Comparator<Notice> noticeComparator = (o1, o2) -> o2.getCreatedTime().compareTo(o1.getCreatedTime());
}
