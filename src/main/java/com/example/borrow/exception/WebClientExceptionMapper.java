package com.example.borrow.exception;

import com.example.borrow.exception.global.RemoteServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class WebClientExceptionMapper {

    public Mono<? extends Throwable> mapError(ClientResponse response, String serviceName) {
        return response.bodyToMono(ErrorResponse.class)
                .flatMap(body -> {
                    ErrorResponse errorResponse = new ErrorResponse(
                            serviceName + " -> " + body.service(),
                            response.statusCode().value(),
                            body.message(),
                            LocalDateTime.now()
                    );

                    return Mono.error(new RemoteServiceException(errorResponse));
                });
    }
}

