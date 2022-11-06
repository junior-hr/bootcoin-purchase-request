package com.nttdata.bootcamp.msbootcoinpurchaserequest.dto;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinsForSale;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
@Builder
public class BootcoinPurchaseRequestDto {

    @Id
    private String idBootcoinPurchaseRequest;
    private String idBootcoinsForSale;
    private String methodOfPayment;
    private String documentNumber;
    private Client client;
    private Double amount;
    private LocalDateTime bootcoinPurchaseRequestDate;
    private BootcoinsForSale bootcoinsForSale;
    private String state;

    public Mono<BootcoinPurchaseRequest> MapperToBootcoinPurchaseRequest(String state) {
        LocalDateTime date = LocalDateTime.now();
        log.info("ini validateBootcoinMovementLimit-------: LocalDateTime.now()" + LocalDateTime.now());
        log.info("ini validateBootcoinMovementLimit-------date: " + date);

        BootcoinPurchaseRequest movement = BootcoinPurchaseRequest.builder()
                .idBootcoinPurchaseRequest(this.getIdBootcoinPurchaseRequest())
                //.idBootcoinsForSale(this.getIdBootcoinsForSale())
                .methodOfPayment(this.getMethodOfPayment())
                .amount(this.getAmount())
                .bootcoinPurchaseRequestDate(date)
                .client(this.getClient())
                .bootcoinsForSale(this.getBootcoinsForSale())
                .state(state)
                .build();

        return Mono.just(movement);
    }
}
