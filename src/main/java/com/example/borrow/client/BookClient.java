package com.example.borrow.client;

import com.example.borrow.dto.out.BookInstanceDto;
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
public class BookClient {
    private final WebClient webClient;
    private final WebClientExceptionMapper exceptionMapper;
    private final String serviceName;

    private final String BOOK_SERVICE_URL;

    public BookClient(WebClient webClient,
                      WebClientExceptionMapper exceptionMapper,
                      @Value("${spring.application.name}") String serviceName,
                      @Value("${service.book.url}") String bookServiceUrl) {
        this.webClient = webClient;
        this.exceptionMapper = exceptionMapper;
        this.serviceName = serviceName;
        this.BOOK_SERVICE_URL = bookServiceUrl;
    }

    public BookInstanceDto getBookInstance(UUID bookInstanceId) {
        return webClient
                .get()
                .uri(this.BOOK_SERVICE_URL + "api/v1/book/instance/" + bookInstanceId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> exceptionMapper.mapError(response, serviceName)
                )
                .bodyToMono(BookInstanceDto.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(500))
                                .filter(this::isRetryableException)
                )
                .block();
    }

//    public BookInstanceDto updateBookInstance(UUID bookInstanceId) { TODO pour optimist lock
//        return webClient
//                .post()
//                .uri(this.BOOK_SERVICE_URL + "api/v1/book/instance/" + bookInstanceId + "/updateLastAttempt")
//                .retrieve()
//                .onStatus(
//                        HttpStatusCode::isError,
//                        response -> exceptionMapper.mapError(response, serviceName)
//                         )
//                .bodyToMono(BookInstanceDto.class)
//                .retryWhen(
//                        Retry.backoff(3, Duration.ofMillis(500))
//                             .filter(this::isRetryableException)
//                          )
//                .block();
//    }


    private boolean isRetryableException(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }
}
