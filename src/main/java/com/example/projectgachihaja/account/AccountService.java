package com.example.projectgachihaja.account;

import com.example.projectgachihaja.config.AppProperties;
import com.example.projectgachihaja.mail.EmailMessage;
import com.example.projectgachihaja.mail.EmailService;
import com.example.projectgachihaja.tag.Tag;
import com.example.projectgachihaja.zone.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final EmailService emailService;


    public Account createNewAccount(CreateAccountForm createAccountInfo) {
        Account account = saveAccount(createAccountInfo);
        emailConfirm(account);
        return account;
    }

    private void emailConfirm(Account account) {
        account.generateEmailToken();

        Context context = new Context();
        context.setVariable("link", "/check-email-token?token="+account.getEmailToken()+"&email="+account.getEmailAddress() );
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName","이메일 인증을 통한 회원 가입 링크");
        context.setVariable("message","가치하자는 회원가입의 마지막 절차로 이메일 인증을 사용하고 있습니다.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/email-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmailAddress())
                .subject("가치하자 회원가입 인증 메일")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    private Account saveAccount(CreateAccountForm createAccountInfo) {
        createAccountInfo.setPassword(passwordEncoder.encode(createAccountInfo.getPassword()));
        Account account = modelMapper.map(createAccountInfo,Account.class);
        return accountRepository.save(account);
    }

    public void logIn(Account account) {
        UsernamePasswordAuthenticationToken token;
        if(!account.emailCheck){
            token = new UsernamePasswordAuthenticationToken(new UserAccount(account,List.of(new SimpleGrantedAuthority("ROLE_ASSOCIATE")))
                            ,account.getPassword()
                            , List.of(new SimpleGrantedAuthority("ROLE_ASSOCIATE")));
        }
        else {
            token = new UsernamePasswordAuthenticationToken(new UserAccount(account,List.of(new SimpleGrantedAuthority("ROLE_ASSOCIATE")))
                            ,account.getPassword()
                            , List.of(new SimpleGrantedAuthority("ROLE_USER")));
        }
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailAddress(emailAddress);
        if(account == null){
            account = accountRepository.findByNickname(emailAddress);
        }
        if(account == null){
            throw new UsernameNotFoundException(emailAddress);
        }
        if(!account.emailCheck){
            return new UserAccount(account,List.of(new SimpleGrantedAuthority("ROLE_ASSOCIATE")));
        }

        return new UserAccount(account,List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public void emailConfirmComplete(Account account) {
        account.emailCheck = true;
        logIn(account);
    }

    public Set<Tag> getTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getTags();
    }

    public Set<Zone> getZones(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getZones();
    }

    public void updateAccountInfo(Account account, SettingsForm settingsForm) {
        account.setNickname(settingsForm.getNickname());
        account.setIntroduce(settingsForm.getIntroduce());
        account.setJob(settingsForm.getJob());
        account.setLocation(settingsForm.getLocation());
        account.setProfileImage(settingsForm.getProfileImage());
        accountRepository.save(account);
    }

    public void tagUpdate(Account account, Tag tag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->a.getTags().add(tag));
    }

    public void zoneUpdate(Account account, Zone zone) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->a.getZones().add(zone));
    }

    public void zoneRemove(Account account, Zone zone) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->a.getZones().remove(zone));
    }

    public void tagRemove(Account account, Tag tag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->a.getTags().remove(tag));
    }

    public void sendEmailLoginLink(Account account) {
        account.generateEmailToken();

        Context context = new Context();
        context.setVariable("link", "/email-login?token="+account.getEmailToken()+"&email="+account.getEmailAddress() );
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName","1회용 로그인 링크");
        context.setVariable("message","가치하자는 비밀 번호 분실 시 가입할때 기입한 이메일을 통해 1회용 로그인을 하실 수 있습니다.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/email-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmailAddress())
                .subject("가치하자 이메일을 통한 1회용 로그인")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }
}
