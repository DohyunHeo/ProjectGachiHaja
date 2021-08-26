package com.example.projectgachihaja.account;

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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final ModelMapper modelMapper;
    public final PasswordEncoder passwordEncoder;
    public final AccountRepository accountRepository;

    public Account createNewAccount(CreateAccountForm createAccountInfo) {
        Account account = saveAccount(createAccountInfo);
        emailConfirm(account);
        return account;
    }

    private void emailConfirm(Account account) {
        account.generateEmailToken();
        log.info("/check-email-token?token="+account.getEmailToken()+"&email="+account.getEmailAddress());
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
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailAddress(id);
        if(account == null){
            account = accountRepository.findByNickname(id);
        }
        if(account == null){
            throw new UsernameNotFoundException(id);
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
}
