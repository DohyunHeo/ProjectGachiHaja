package com.example.projectgachihaja.domain.notice;

import com.example.projectgachihaja.domain.account.Account;
import com.example.projectgachihaja.domain.account.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NoticeInterceptor implements HandlerInterceptor {
    private final NoticeRepository noticeRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(modelAndView != null && authentication !=null && !Objects.requireNonNull(modelAndView.getViewName()).startsWith("redirect:") && authentication.getPrincipal() instanceof UserAccount){
            Account account = ((UserAccount)authentication.getPrincipal()).getAccount();
            long count = noticeRepository.countByAccountAndChecked(account,false);
            modelAndView.addObject("newNotice", count>0);
        }
    }
}
