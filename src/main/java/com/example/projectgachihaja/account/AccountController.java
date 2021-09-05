package com.example.projectgachihaja.account;


import com.example.projectgachihaja.notice.Notice;
import com.example.projectgachihaja.notice.NoticeList;
import com.example.projectgachihaja.notice.NoticeRepository;
import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.tag.TagRepository;
import com.example.projectgachihaja.tag.TagService;
import com.example.projectgachihaja.zone.Zone;
import com.example.projectgachihaja.zone.ZoneRepository;
import com.example.projectgachihaja.zone.ZoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final CreateAccountFormValidator createAccountInfoValidator;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final TagService tagService;
    private final ZoneService zoneService;
    private final NoticeRepository noticeRepository;

    @InitBinder("createAccountForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(createAccountInfoValidator);
    }

    @GetMapping("/create-account")
    public String createAccountView(Model model){
        model.addAttribute(new CreateAccountForm());
        return "create-account";
    }

    @PostMapping("/create-account")
    public String createAccount(@Valid CreateAccountForm createAccountInfo, Errors errors){
        if(errors.hasErrors()){
            return "create-account";
        }
        Account account = accountService.createNewAccount(createAccountInfo);
        accountService.logIn(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmailAddress(email);

        if(account == null){
            model.addAttribute("error","invalid.email");
            return "redirect:/";
        }
        if(!account.isValidToken(token)){
            model.addAttribute("error","invalid.token");
            return "redirect:/";
        }
        accountService.emailConfirmComplete(account);

        model.addAttribute("nickname", account.getNickname());
        return "redirect:/";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, @CurrentAccount Account account, Model model){
        Account matchingUser = accountRepository.findByNickname(nickname);
        if(nickname == null){
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자는 존재하지 않습니다.");
        }
        model.addAttribute(matchingUser);
        model.addAttribute("isOwner", matchingUser.equals(account));


        return "account/profile";
    }

    @GetMapping("/notice")
    public String noticeView(@CurrentAccount Account account, Model model){
        List<Notice> notices = noticeRepository.findAllByAccount(account);
        NoticeList noticeList = new NoticeList();
        model.addAttribute(account);
        model.addAttribute(noticeList);
        model.addAttribute("notices",notices);
        return "account/notice";
    }

    @GetMapping("/settings/{nickname}")
    public String accountSettingsView(@CurrentAccount Account account, @Valid SettingsForm settingsForm, Model model, RedirectAttributes flashmessage) throws JsonProcessingException {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account,SettingsForm.class));

        List<String> alltagsList = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("tagWhiteList", objectMapper.writeValueAsString(alltagsList));

        List<String> allZonesList = zoneRepository.findAll().stream().map(Zone::getCity).collect(Collectors.toList());
        model.addAttribute("zoneWhiteList", objectMapper.writeValueAsString(allZonesList));

        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags",tags.stream().map(Tag::getTitle).collect(Collectors.toList()));
        Set<Zone> zones = accountService.getZones(account);
        model.addAttribute("zones",zones.stream().map(Zone::getCity).collect(Collectors.toList()));

        return "account/settings";
    }
    @PostMapping("/settings/{nickname}")
    public String accountSettingsUpdate(@CurrentAccount Account account, @Valid SettingsForm settingsForm, Model model, RedirectAttributes flashmessage){
        accountService.updateAccountInfo(account, settingsForm);
        flashmessage.addFlashAttribute("message","변경 완료");

        return "redirect:/settings/" + account.getNickname();
    }

    @PostMapping("/settings/{nickname}/add")
    @ResponseBody
    public ResponseEntity addTagAndZone(@CurrentAccount Account account, @RequestBody SettingsForm settingsForm, Model model){
        if(settingsForm.getTagTitle() != null) {
            String title = settingsForm.getTagTitle();
            Tag tag = tagService.findOrCreateNew(title);
            model.addAttribute(account);
            accountService.tagUpdate(account, tag);
        }
        else {
            String city = settingsForm.getZoneCity();
            Zone zone = zoneService.findOrCreateNew(city);
            model.addAttribute(account);
            accountService.zoneUpdate(account, zone);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/{nickname}/remove")
    @ResponseBody
    public ResponseEntity removeTagAndZone(@CurrentAccount Account account, @RequestBody SettingsForm settingsForm, Model model){
        if(settingsForm.getTagTitle() != null) {
            String title = settingsForm.getTagTitle();
            Tag tag = tagRepository.findByTitle(title);
            model.addAttribute(account);
            accountService.tagRemove(account, tag);
        }
        else {
            String city = settingsForm.getZoneCity();
            Zone zone = zoneRepository.findByCity(city);
            model.addAttribute(account);
            accountService.zoneRemove(account, zone);
        }
        return ResponseEntity.ok().build();
    }


}
