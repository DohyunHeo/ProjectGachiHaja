package com.example.projectgachihaja.Post;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.Together.TogetherService;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public Post createNewPost(PostForm postForm, Account writer) {
        Post post = modelMapper.map(postForm, Post.class);
        post.setWriter(writer);
        post.setReportingDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    public void newCommentRegister(Post post, Comment newComment) {
        post.getComments().add(newComment);
        postRepository.save(post);
    }

    public void updateViewer(Post post, Account account) {
        if(!post.getViewerList().contains(account.getNickname())){
            post.getViewerList().add(account.getNickname());
            post.view++;
        }
    }

    public void commentRemove(Post post, Comment comment) {
        post.getComments().remove(comment);
    }

    public void allCommentsRemove(Post post){
        post.setComments(null);
    }

    public void postDelete(Post post, Account account) {
        if(post.getWriter().equals(account)){
            postRepository.delete(post);
            allCommentsRemove(post);
        }
        else {
            throw new RuntimeException("권한이 없습니다.");
        }
    }

    public void postEdit(Post post, PostForm postForm) {
        post.setContent(postForm.getContent());
        post.setTitle(postForm.getTitle());
        post.setPostType(postForm.getPostType());
        postRepository.save(post);
    }
}
