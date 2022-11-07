package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.config.WebClientConfig;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.MovementDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinMovement;
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
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class BootcoinMovementRepository {

    @Value("${local.property.host.ms-bootcoin-movement}")
    private String propertyHostMsBootcoinMovement;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = Constants.BOOTCOINMOVEMENT_CB, fallbackMethod = "getDefaultupdateBootcoinMovement")
    public Mono<Void> updateBootcoinMovement(BootcoinMovement bootcoinMovement) {
        log.info("--updateBootcoinMovement------- movement: " + bootcoinMovement);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsBootcoinMovement + ":8092")
                .flatMap(d -> webconfig.getWebclient().put()
                                .uri("/api/movement")
                                //.accept(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .body(Mono.just(bootcoinMovement), BootcoinMovement.class).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                                .bodyToMono(Void.class)
                );
    }

    public Mono<Void> getDefaultupdateBootcoinMovement(String idBankAccount, Double balance, Exception e) {
        log.info("Inicio----getDefaultupdateBootcoinMovement-------idBankAccount: " + idBankAccount);
        return Mono.empty();
    }
}
