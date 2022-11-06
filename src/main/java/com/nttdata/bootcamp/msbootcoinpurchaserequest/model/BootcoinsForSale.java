package com.nttdata.bootcamp.msbootcoinpurchaserequest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class BootcoinPurchaseRequest.
 * BootcoinPurchaseRequest microservice class BootcoinPurchaseRequest.
 */
@Document(collection = "BootcoinsForSale")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BootcoinsForSale {

    @Id
    private String idBootcoinsForSale;
    private Client client;
    private Double amount;
    private LocalDateTime bootcoinPurchaseRequestDate;
    private Bootcoin bootcoin;
    private Double balance;
    private String state;

}