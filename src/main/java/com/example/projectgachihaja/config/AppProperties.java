package com.example.projectgachihaja.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("webapp")
public class AppProperties {
    private String host;
}
