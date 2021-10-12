package com.example.projectgachihaja.domain.Together.controller;

import com.example.projectgachihaja.domain.Together.*;
import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.account.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class TogetherController {

    private final TogetherRepository togetherRepository;
    private final TogetherService togetherService;
    private final ModelMapper modelMapper;
    private final TogetherFormValidator togetherFormValidator;

    @InitBinder("togetherForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(togetherFormValidator);
    }

    @GetMapping("/together/create")
    public String togetherCreateView(@CurrentAccount Account account, TogetherForm togetherForm, Model model){
        model.addAttribute(togetherForm);
        model.addAttribute(account);
        return "together/create";
    }

    @PostMapping("/together/create")
    public String togetherCreateComplete(@CurrentAccount Account account, @Valid TogetherForm togetherForm, Errors errors){
        if(errors.hasErrors()){
            return "together/create";
        }
        Together together = togetherService.createNewTogether(modelMapper.map(togetherForm, Together.class), account);
        return "redirect:/together/" + together.pathEncoder()+"/settings";
    }

    @GetMapping("/together/{path}")
    public String togetherView(@CurrentAccount Account account,@PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);
        if(account != null)
        model.addAttribute(account);

        return "together/view";
    }

    @GetMapping("/together/{path}/error")
    public String togetherErrorView(@CurrentAccount Account account,@PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);
        if(account != null)
            model.addAttribute(account);

        return "together/error";
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
        return "redirect:/together/"+together.pathEncoder();
    }

    @GetMapping("/together/{path}/leave")
    public String togetherLeave(@CurrentAccount Account account, @PathVariable String path){
        Together together = togetherRepository.findByPath(path);
        togetherService.togetherLeave(account, together);
        return "redirect:/together/"+together.pathEncoder();
    }
}
