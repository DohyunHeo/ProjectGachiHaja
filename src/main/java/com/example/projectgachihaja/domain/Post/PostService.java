package com.example.projectgachihaja.domain.Post;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.Together.event.TogetherNoticeEvent;
import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public Post createNewPost(PostForm postForm, Together together, Account writer) {
        Post post = modelMapper.map(postForm, Post.class);
        post.setWriter(writer);
        post.setReportingDate(LocalDateTime.now());

        if(post.getPostType() == PostType.NOTICE){
            eventPublisher.publishEvent(new TogetherNoticeEvent(together,post,"새로운 공지가 등록되었습니다."));
        }
        return postRepository.save(post);
    }

    public void newCommentRegister(Together together, Post post, Comment newComment) {
        post.getComments().add(newComment);
        postRepository.save(post);

        if(!post.getWriter().getNickname().equals(newComment.getWriter().getNickname()))
        eventPublisher.publishEvent(new CommentNoticeToPostWriterEvent(post.getWriter(),together,post));
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

    public void postRemoveAll(Account account) {
        List<Post> posts = postRepository.findAllByWriter(account);
        if(posts != null) {
            posts.forEach(post -> {
                post.getComments().clear();
            });
            postRepository.deleteAll(posts);
        }
    }
}
