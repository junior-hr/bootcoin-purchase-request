package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.config.WebClientConfig;
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
public class MobileWalletRepository {

    @Value("${local.property.host.ms-mobile-wallet}")
    private String propertyHostMsMobileWallet;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = Constants.MOBILEWALLET_CB, fallbackMethod = "getDefaultByDocumentNumber")
    public Mono<Integer> findByDocumentNumber(String documentNumber) {
        log.info("Inicio----findByDocumentNumber-------documentNumber: " + documentNumber);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsMobileWallet + ":8090")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/mobilewallet/count/documentNumber/" + documentNumber).retrieve()
                        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                        .bodyToMono(Integer.class)
                );
    }

    public Mono<Integer> getDefaultByDocumentNumber(String documentNumber, Exception e) {
        log.info("Inicio----getDefaultByDocumentNumber-------documentNumber: " + documentNumber);
        return Mono.empty();
    }
}
