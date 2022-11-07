package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.config.WebClientConfig;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.MovementDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BankAccount;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.Movement;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.util.Constants;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class MovementRepository {

    @Value("${local.property.host.ms-movement}")
    private String propertyHostMsMovement;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = Constants.MOVEMENT_CB, fallbackMethod = "getDefaultUpdateMovement")
    public Mono<Void> updateMovement(MovementDto movement) {
        log.info("--updateBalanceBankAccount------- movement: " + movement);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsMovement + ":8085")
                .flatMap(d -> webconfig.getWebclient().put()
                                .uri("/api/movement")
                                //.accept(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .body(Mono.just(movement), MovementDto.class).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                                .bodyToMono(Void.class)
                );
    }

    public Mono<Void> getDefaultUpdateMovement(String idBankAccount, Double balance, Exception e) {
        log.info("Inicio----getDefaultUpdateMovement-------idBankAccount: " + idBankAccount);
        return Mono.empty();
    }
}
