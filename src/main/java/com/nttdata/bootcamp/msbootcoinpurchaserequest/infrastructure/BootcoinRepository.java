package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.config.WebClientConfig;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.Bootcoin;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.util.Constants;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class BootcoinRepository {

    @Value("${local.property.host.ms-bootcoin}")
    private String propertyHostMsBootcoin;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = Constants.BOOTCOIN_CB, fallbackMethod = "getDefaultBootcoinByDocumentNumber")
    public Mono<Bootcoin> findBootcoinByDocumentNumber(String documentNumber) {
        log.info("Inicio----findBootcoinByDocumentNumber-------documentNumber: " + documentNumber);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsBootcoin + ":8096")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/bootcoin/documentNumber/" + documentNumber).retrieve()
                        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                        .bodyToMono(Bootcoin.class)
                        // .transform(it -> reactiveCircuitBreakerFactory.create("parameter-service").run(it, throwable -> Mono.just(new BankAccount())) )
                );
    }

    public Mono<Bootcoin> getDefaultBootcoinByDocumentNumber(String documentNumber, Exception e) {
        log.info("Inicio----getDefaultBootcoinByDocumentNumber-------documentNumber: " + documentNumber);
        return Mono.empty();
    }
}
