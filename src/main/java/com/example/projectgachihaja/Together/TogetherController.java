package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.Post.PostRepository;
import com.example.projectgachihaja.Post.PostService;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.CurrentAccount;
import com.example.projectgachihaja.comment.CommentRepository;
import com.example.projectgachihaja.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TogetherController {

    private final TogetherRepository togetherRepository;
    private final TogetherService togetherService;
    private final ModelMapper modelMapper;
    private final PostService postService;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @GetMapping("/together/create")
    public String togetherCreateView(@CurrentAccount Account account,TogetherForm togetherForm, Model model){
        model.addAttribute(togetherForm);
        model.addAttribute(account);
        return "together/create";
    }
    @GetMapping("/together/create/many")
    public String togetherCreateMany(@CurrentAccount Account account,TogetherForm togetherForm, Model model){
        model.addAttribute(togetherForm);
        model.addAttribute(account);
        togetherService.manymanyCreate(account);
        return "redirect:/";
    }

    @PostMapping("/together/create")
    public String togetherCreateComplete(@CurrentAccount Account account,TogetherForm togetherForm){
        Together together = togetherService.createNewTogether(modelMapper.map(togetherForm, Together.class), account);
        return "redirect:/together/" + together.getPath();
    }

    @GetMapping("/together/{path}")
    public String togetherView(@CurrentAccount Account account,@PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);
        model.addAttribute(account);

        return "together/view";
    }

    @GetMapping("/together/{path}/members")
    public String togetherMembersView(@CurrentAccount Account account,@PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);
        model.addAttribute(account);

        return "together/members";
    }

    @GetMapping("/together/{path}/join")
    public String togetherJoinRequest(@CurrentAccount Account account, @PathVariable String path){
        Together together = togetherRepository.findTogetherWithCandidatesByPath(path);
        togetherService.addTogetherCandidates(account, together);
        return "redirect:/together/"+together.getPath();
    }

    @GetMapping("/together/{path}/board")
    public String togetherBoardView(@CurrentAccount Account account, @PathVariable String path,
                                    @PageableDefault(size = 9,sort="reportingDate", direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Together together = togetherRepository.findWithPostsByPath(path);
        Page<Post> postPage = postRepository.findWithAllPage(pageable);

        model.addAttribute(together);
        model.addAttribute("postPage",postPage);
        model.addAttribute("sortProperty", pageable.getSort().toString().contains("reportingDate") ? "reportingDate" : "memberCount");

        return "together/board/board";
    }
}
