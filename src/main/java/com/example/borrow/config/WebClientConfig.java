package com.example.borrow.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter((request, next) -> {
                    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attrs != null) {
                        HttpServletRequest servletRequest = attrs.getRequest();
                        if (servletRequest.getCookies() != null) {
                            for (Cookie cookie : servletRequest.getCookies()) {
                                if ("jwt".equals(cookie.getName())) {
                                    ClientRequest newRequest = ClientRequest.from(request)
                                            .header(HttpHeaders.COOKIE, "jwt=" + cookie.getValue())
                                            .build();
                                    return next.exchange(newRequest);
                                }
                            }
                        }
                    }
                    return next.exchange(request);
                })
                .build();
    }
}
