package com.example.borrow.client;

import com.example.borrow.domain.service.ext.UserClientDto;
import com.example.borrow.exception.WebClientExceptionMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;

@Service
public class UserClient {
    private final WebClient webClient;
    private final WebClientExceptionMapper exceptionMapper;
    private final String serviceName;

    private final String USER_SERVICE_URL;

    public UserClient(WebClient webClient,
                      WebClientExceptionMapper exceptionMapper,
                      @Value("${spring.application.name}") String serviceName,
                      @Value("${service.user.url}") String userServiceUrl) {
        this.webClient = webClient;
        this.exceptionMapper = exceptionMapper;
        this.serviceName = serviceName;
        this.USER_SERVICE_URL = userServiceUrl;
    }

    public UserClientDto getLoggedUser() {
        return webClient
                .get()
                .uri(this.USER_SERVICE_URL + "api/v1/logged-user")
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> exceptionMapper.mapError(response, serviceName)
                )
                .bodyToMono(UserClientDto.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(500))
                                .filter(this::isRetryableException)
                )
                .block();
    }

    private boolean isRetryableException(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }

    public UserClientDto getUser(UUID userId) {
        return webClient
                .get()
                .uri(this.USER_SERVICE_URL + "api/v1/user/" + userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> exceptionMapper.mapError(response, serviceName)
                         )
                .bodyToMono(UserClientDto.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(500))
                             .filter(this::isRetryableException)
                          )
                .block();
    }

    public UserClientDto getUserByUsername(String username) {
        return webClient
                .get()
                .uri(this.USER_SERVICE_URL + "api/v1/user/username/" + username)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> exceptionMapper.mapError(response, serviceName)
                         )
                .bodyToMono(UserClientDto.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(500))
                             .filter(this::isRetryableException)
                          )
                .block();
    }
}
