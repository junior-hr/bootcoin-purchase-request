package com.nttdata.bootcamp.msbootcoinpurchaserequest.application;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinPurchaseRequestDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class BootcoinPurchaseRequestService.
 * BootcoinPurchaseRequest microservice class BootcoinPurchaseRequestService.
 */
public interface BootcoinPurchaseRequestService {
    public Flux<BootcoinPurchaseRequest> findAll();
    public Mono<BootcoinPurchaseRequest> findById(String idBootcoinPurchaseRequest);
    public Mono<BootcoinPurchaseRequest> save(BootcoinPurchaseRequestDto bootcoinPurchaseRequestDto);
    public Mono<BootcoinPurchaseRequest> update(BootcoinPurchaseRequestDto bootcoinPurchaseRequestDto, String idBootcoinPurchaseRequest);
    public Mono<Void> delete(String idBootcoinPurchaseRequest);
    public Mono<BootcoinPurchaseRequest> findByDocumentNumber(String documentNumber);
    public Mono<BootcoinPurchaseRequest> acceptSale(String idBootcoinPurchaseRequest);

}
