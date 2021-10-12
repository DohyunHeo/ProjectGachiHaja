package com.example.projectgachihaja.domain.comment;

import com.example.projectgachihaja.domain.Post.Post;
import com.example.projectgachihaja.domain.Post.PostRepository;
import com.example.projectgachihaja.domain.account.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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

    public void commentRemoveAll(Account account) {
        List<Comment> comments = commentRepository.findAllByWriter(account);
        comments.forEach(comment -> {
            Post post = postRepository.findByComments(comment);
            post.getComments().remove(comment);
        });
        if(comments != null) {
            commentRepository.deleteAll(comments);
        }
    }
}
