package com.example.projectgachihaja.config;

import com.example.projectgachihaja.Together.interceptor.TogetherScheduleInterceptor;
import com.example.projectgachihaja.notice.NoticeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final NoticeInterceptor noticeInterceptor;
    private final TogetherScheduleInterceptor togetherScheduleInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> staticResourcesPath = Arrays.stream(StaticResourceLocation.values())
                        .flatMap(StaticResourceLocation::getPatterns)
                                .collect(Collectors.toList());
        staticResourcesPath.add("/node_modules/**");
        registry.addInterceptor(noticeInterceptor).excludePathPatterns(staticResourcesPath);
        registry.addInterceptor(togetherScheduleInterceptor).addPathPatterns("/together/**/schedule/**/edit","/together/**/schedule/**/remove","/together/**/schedule/create");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
