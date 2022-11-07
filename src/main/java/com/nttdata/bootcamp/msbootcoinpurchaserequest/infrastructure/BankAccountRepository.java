package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.config.WebClientConfig;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BankAccount;
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
public class BankAccountRepository {

    @Value("${local.property.host.ms-bank-account}")
    private String propertyHostMsBankAccount;

    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @CircuitBreaker(name = Constants.BANKACCOUNT_CB, fallbackMethod = "getDefaultByDocumentNumber")
    public Mono<BankAccount> findByDocumentNumber(String documentNumber) {
        log.info("Inicio----findByDocumentNumber-------documentNumber: " + documentNumber);
        log.info("Inicio----findByDocumentNumber-------propertyHostMsBankAccount: " + propertyHostMsBankAccount);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsBankAccount + ":8085")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/bankaccounts/first/documentNumber/" + documentNumber).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                                .bodyToMono(BankAccount.class)
                );
    }

    @CircuitBreaker(name = Constants.BANKACCOUNT_CB, fallbackMethod = "getDefaultCantByDocumentNumber")
    public Mono<Long> findCantByDocumentNumber(String documentNumber) {
        log.info("Inicio----findBankAccountByDocumentNumber-------documentNumber: " + documentNumber);
        WebClientConfig webconfig = new WebClientConfig();
        return webconfig.setUriData("http://" + propertyHostMsBankAccount + ":8085")
                .flatMap(d -> webconfig.getWebclient().get().uri("/api/bankaccounts/cant/documentNumber/" + documentNumber).retrieve()
                        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                        .bodyToMono(Long.class)
                );
    }

    public Mono<BankAccount> getDefaultByDocumentNumber(String documentNumber, Exception e) {
        log.info("Inicio----getDefaultByDocumentNumber-------documentNumber: " + documentNumber);
        return Mono.empty();
    }

    public Mono<Long> getDefaultCantByDocumentNumber(String documentNumber, Exception e) {
        log.info("Inicio----getDefaultCantByDocumentNumber-------documentNumber: " + documentNumber);
        return Mono.empty();
    }
}
