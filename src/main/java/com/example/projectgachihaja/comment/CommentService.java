package com.example.projectgachihaja.comment;

import com.example.projectgachihaja.account.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    public Comment newCommentWrite(Account account, CommentForm commentForm) {
        Comment newComment = modelMapper.map(commentForm, Comment.class);
        newComment.setWriter(account);
        newComment.setReportingDate(LocalDateTime.now());

        return commentRepository.save(newComment);
    }

    public void commentRemove(Account account, Comment comment) {
        if(comment.getWriter().equals(account)){
            commentRepository.delete(comment);
        }
    }

    public void allCommentsRemove(Set<Comment> comments) {
        commentRepository.deleteAll(comments);
    }

    public void commentEdit(Comment comment, CommentForm commentForm) {
        comment.setContent(commentForm.getContent());
    }
}
