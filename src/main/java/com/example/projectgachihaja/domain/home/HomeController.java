package com.example.projectgachihaja.domain.home;

import com.example.projectgachihaja.domain.Together.Together;
import com.example.projectgachihaja.domain.Together.TogetherList;
import com.example.projectgachihaja.domain.Together.TogetherRepository;
import com.example.projectgachihaja.domain.Together.TogetherService;
import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.account.AccountRepository;
import com.example.projectgachihaja.domain.account.AccountService;
import com.example.projectgachihaja.domain.account.CurrentAccount;
import com.example.projectgachihaja.domain.account.oauth.AccountOAuthForm;
import com.example.projectgachihaja.domain.notice.Notice;
import com.example.projectgachihaja.domain.notice.NoticeList;
import com.example.projectgachihaja.domain.notice.NoticeService;
import com.example.projectgachihaja.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ScheduleRepository scheduleRepository;
    private final TogetherRepository togetherRepository;
    private final TogetherService togetherService;
    private final NoticeService noticeService;
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;

    @RequestMapping("**/favicon.ico")
    public String favicon(){
        return "forward:/resources/static/images/fav.ico";
    }
    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model){
        if (account != null){
            Account accountFullInfo = accountRepository.findWithTagsAndZonesByNickname(account.getNickname());
            togetherRepository.findFist5WithNewTogetherByAccount(accountFullInfo);
            model.addAttribute(accountFullInfo);
            model.addAttribute("togetherList",togetherRepository.findFist5WithNewTogetherByAccount(accountFullInfo));
            model.addAttribute("scheduleList", scheduleRepository.findAllByMembersAndStartAfterOrderByStartDesc(account, LocalDateTime.now()));
        }
        else {
            List<Together> togetherList = togetherRepository.findFirst8ByPublishedAndClosedOrderByPublishedDateTimeDesc(true, false);
            model.addAttribute("togetherPage", togetherList);
        }
        return "index";
    }

    @GetMapping("/oauth-success")
    public String oauth(){
        AccountOAuthForm account= (AccountOAuthForm) httpSession.getAttribute("account");
        Account accountOauth = accountRepository.findByNickname(account.getNickname());
        accountService.logIn(accountOauth);
        if(accountOauth.getJob() == null){
            return "redirect:/initial-setting";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/search/together")
    public String searchTogether(@CurrentAccount Account account, @PageableDefault(size = 9,sort="publishedDateTime", direction = Sort.Direction.DESC)
                                             Pageable pageable, String keyword, Model model){
        Page<Together> togetherPage = togetherRepository.findByKeyword(keyword, pageable);
        model.addAttribute("togetherPage", togetherPage);
        model.addAttribute("keyword",keyword);
        model.addAttribute("sortProperty", "publishedDateTime");
        if(account != null) {
            model.addAttribute(account);
        }
        return "search";
    }

    @GetMapping("/createTogetherList")
    @ResponseBody
    public List<TogetherList> createTogetherList(@CurrentAccount Account account) {
        List<Together> togethers = togetherService.createTogetherList(account);
        return modelMapper.map(togethers, new TypeToken<List<TogetherList>>() {}.getType());
    }

    @GetMapping("/togetherList")
    @ResponseBody
    public List<TogetherList> togetherList(@CurrentAccount Account account) {
        List<Together> togethers = togetherService.togetherList(account);
        return modelMapper.map(togethers, new TypeToken<List<TogetherList>>() {}.getType());
    }

    @GetMapping("/newNotice")
    @ResponseBody
    public List<NoticeList> noticeList(@CurrentAccount Account account){
        List<Notice> notices = noticeService.getUncheckedNotice(account);

        return modelMapper.map(notices, new TypeToken<List<NoticeList>>() {}.getType());
    }

    @PostMapping("/newNotice")
    @ResponseBody
    public ResponseEntity noticeChecked(@CurrentAccount Account account, @RequestBody NoticeList noticeList, Model model){
        noticeService.noticeCheck(noticeList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/noticeAllCheck")
    @ResponseBody
    public ResponseEntity noticeAllCheck(@CurrentAccount Account account, Model model){
        noticeService.allCheck(account);
        return ResponseEntity.ok().build();
    }
}
