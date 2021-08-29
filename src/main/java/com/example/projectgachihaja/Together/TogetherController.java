package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.Post.Post;
import com.example.projectgachihaja.Post.PostRepository;
import com.example.projectgachihaja.Post.PostService;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.CurrentAccount;
import com.example.projectgachihaja.comment.CommentRepository;
import com.example.projectgachihaja.comment.CommentService;
import com.example.projectgachihaja.schedule.Schedule;
import com.example.projectgachihaja.schedule.ScheduleForm;
import com.example.projectgachihaja.schedule.ScheduleRepository;
import com.example.projectgachihaja.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class TogetherController {

    private final TogetherRepository togetherRepository;
    private final TogetherService togetherService;
    private final ModelMapper modelMapper;

    @GetMapping("/together/create")
    public String togetherCreateView(@CurrentAccount Account account,TogetherForm togetherForm, Model model){
        model.addAttribute(togetherForm);
        model.addAttribute(account);
        return "together/create";
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
        if(account != null)
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
}
