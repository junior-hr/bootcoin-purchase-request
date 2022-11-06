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
public class BootcoinsForSaleDto {

    @Id
    private String idBootcoinPurchaseRequest;
    private String documentNumber;
    private Client client;
    private Double amount;
    private LocalDateTime bootcoinPurchaseRequestDate;
    private Double balance;

    public Mono<BootcoinsForSale> MapperToBootcoinPurchaseRequest(Client client) {
        LocalDateTime date = LocalDateTime.now();
        log.info("ini validateBootcoinMovementLimit-------: LocalDateTime.now()" + LocalDateTime.now());
        log.info("ini validateBootcoinMovementLimit-------date: " + date);

        BootcoinsForSale bootcoinsForSale = BootcoinsForSale.builder()
                .idBootcoinPurchaseRequest(this.getIdBootcoinPurchaseRequest())
                .amount(this.getAmount())
                .balance(this.getBalance())
                .bootcoinPurchaseRequestDate(date)
                .client(client)
                .build();

        return Mono.just(bootcoinsForSale);
    }
}
