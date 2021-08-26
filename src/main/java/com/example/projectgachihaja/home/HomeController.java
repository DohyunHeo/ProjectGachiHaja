package com.example.projectgachihaja.home;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.Together.TogetherRepository;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final TogetherRepository togetherRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model){
        if (account != null){
            model.addAttribute(account);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/search/together")
    public String searchTogether(@PageableDefault(size = 9,sort="publishedDateTime", direction = Sort.Direction.DESC)
                                             Pageable pageable, String keyword, Model model){
        Page<Together> togetherPage = togetherRepository.findByKeyword(keyword, pageable);
        model.addAttribute("togetherPage", togetherPage);
        model.addAttribute("keyword",keyword);
        model.addAttribute("sortProperty", pageable.getSort().toString().contains("publishedDateTime") ? "publishedDateTime" : "memberCount");
        return "search";
    }
}
