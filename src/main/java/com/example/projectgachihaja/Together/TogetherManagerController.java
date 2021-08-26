package com.example.projectgachihaja.Together;

import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.AccountRepository;
import com.example.projectgachihaja.account.CurrentAccount;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TogetherManagerController {
    private final AccountRepository accountRepository;
    private final TogetherRepository togetherRepository;
    private final TogetherService togetherService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final ZoneRepository zoneRepository;
    private final ZoneService zoneService;

    @GetMapping("/together/{path}/settings")
    public String togetherSettingsView(@CurrentAccount Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);
        model.addAttribute(account);

        List<String> alltagsList = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("tagWhiteList", objectMapper.writeValueAsString(alltagsList));

        List<String> allZonesList = zoneRepository.findAll().stream().map(Zone::getCity).collect(Collectors.toList());
        model.addAttribute("zoneWhiteList", objectMapper.writeValueAsString(allZonesList));

        Set<Tag> tags = togetherService.getTags(together);
        model.addAttribute("tags",tags.stream().map(Tag::getTitle).collect(Collectors.toList()));
        Set<Zone> zones = togetherService.getZones(together);
        model.addAttribute("zones",zones.stream().map(Zone::getCity).collect(Collectors.toList()));

        return "/together/settings";
    }

    @PostMapping("/together/{path}/settings/banner/enable")
    public String enableTogetherBanner(@CurrentAccount Account account, @PathVariable String path){
        Together together = togetherService.managerCheck(account,path);
        togetherService.bannerSetting(together,true);
        return "redirect:/together/"+together.getPath()+"/settings";
    }

    @PostMapping("/together/{path}/settings/banner/disable")
    public String disableTogetherBanner(@CurrentAccount Account account, @PathVariable String path){
        Together together = togetherService.managerCheck(account,path);
        togetherService.bannerSetting(together,false);
        return "redirect:/together/"+together.getPath()+"/settings";
    }

    @PostMapping("/together/{path}/settings/publish")
    public String togetherPublish(@CurrentAccount Account account, @PathVariable String path){
        Together together = togetherService.managerCheck(account,path);
        togetherService.togetherPublish(together);
        return "redirect:/together/"+together.getPath()+"/settings";
    }

    @PostMapping("/together/{path}/settings/close")
    public String togetherClose(@CurrentAccount Account account, @PathVariable String path){
        Together together = togetherService.managerCheck(account,path);
        togetherService.togetherClose(together);
        return "redirect:/together/"+together.getPath()+"/settings";
    }

    @PostMapping("/together/{path}/settings/add")
    @ResponseBody
    public ResponseEntity togetherAddTagAndZone(@CurrentAccount Account account, @RequestBody TogetherForm togetherForm, @PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        if(togetherForm.getTagTitle() != null) {
            String title = togetherForm.getTagTitle();
            Tag tag = tagService.findOrCreateNew(title);
            model.addAttribute(together);
            togetherService.tagUpdate(together, tag);
        }
        else {
            String city = togetherForm.getZoneCity();
            Zone zone = zoneService.findOrCreateNew(city);
            model.addAttribute(together);
            togetherService.zoneUpdate(together, zone);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/together/{path}/settings/remove")
    @ResponseBody
    public ResponseEntity togetherRemoveTagAndZone(@CurrentAccount Account account, @RequestBody TogetherForm togetherForm, @PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        if(togetherForm.getTagTitle() != null) {
            String title = togetherForm.getTagTitle();
            Tag tag = tagService.findOrCreateNew(title);
            model.addAttribute(together);
            togetherService.tagRemove(together, tag);
        }
        else {
            String city = togetherForm.getZoneCity();
            Zone zone = zoneService.findOrCreateNew(city);
            model.addAttribute(together);
            togetherService.zoneRemove(together, zone);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/together/{path}/edit")
    public String togetherEditView(@CurrentAccount Account account,@PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);
        model.addAttribute(modelMapper.map(together,TogetherForm.class));
        model.addAttribute(account);

        return "/together/edit";
    }

    @PostMapping("/together/{path}/edit")
    public String togetherEditComplete(@CurrentAccount Account account,@PathVariable String path,TogetherForm togetherForm,  RedirectAttributes flashmessage){
        Together together = togetherRepository.findByPath(path);
        togetherService.togetherInfoUpdate(together,togetherForm);
        flashmessage.addFlashAttribute("message","변경 완료");

        return "redirect:/together/"+together.getPath()+"/edit";
    }

    @GetMapping("/together/{path}/entryform")
    public String togetherCandidatesList(@CurrentAccount Account account,@PathVariable String path, Model model){
        Together together = togetherRepository.findByPath(path);
        model.addAttribute(together);

        return "/together/entryform";
    }

    @PostMapping("/together/{path}/entryform")
    public String togetherCandidates(@CurrentAccount Account account,@PathVariable String path, String nickname, RedirectAttributes flashmessage){
        Account candidate = accountRepository.findByNickname(nickname);
        Together together = togetherRepository.findByPath(path);
        togetherService.togetherRegistrationApproval(together,candidate);
        flashmessage.addFlashAttribute("message","변경 완료");

        return "redirect:/together/"+together.getPath()+"/entryform";
    }

}
