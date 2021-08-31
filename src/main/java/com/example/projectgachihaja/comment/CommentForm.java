package com.example.projectgachihaja.comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

@Data
public class CommentForm {
    String content;

    LocalDateTime reportingDate;

    public Comparator<Comment> commentComparator = (o1, o2) -> o1.getReportingDate().compareTo(o2.getReportingDate());
}
