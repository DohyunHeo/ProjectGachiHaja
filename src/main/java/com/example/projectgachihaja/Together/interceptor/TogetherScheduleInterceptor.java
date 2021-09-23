package com.example.projectgachihaja.Together.interceptor;

import com.example.projectgachihaja.Together.Together;
import com.example.projectgachihaja.Together.TogetherRepository;
import com.example.projectgachihaja.account.Account;
import com.example.projectgachihaja.account.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TogetherScheduleInterceptor implements HandlerInterceptor {
    private final TogetherRepository togetherRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Together together = togetherRepository.findByPath((String) pathVariables.get("path"));
        if(!together.isManager((UserAccount)authentication.getPrincipal())){
            response.sendRedirect("/together/" +together.pathEncoder() +"/schedule");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
