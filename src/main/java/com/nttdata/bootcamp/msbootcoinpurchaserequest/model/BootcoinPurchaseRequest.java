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
@Document(collection = "BootcoinPurchaseRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BootcoinPurchaseRequest {

    @Id
    private String idBootcoinPurchaseRequest;
    private String methodOfPayment;
    private Client client;
    private Double amount;
    private LocalDateTime bootcoinPurchaseRequestDate;
    private String state;
    private BootcoinsForSale bootcoinsForSale;

}