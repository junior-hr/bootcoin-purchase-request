package com.nttdata.bootcamp.msbootcoinpurchaserequest.application;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinPurchaseRequestDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinsForSaleDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinsForSale;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class BootcoinsForSaleService.
 * BootcoinsForSale microservice class BootcoinsForSaleService.
 */
public interface BootcoinsForSaleService {
    public Flux<BootcoinsForSale> findAll();
    public Mono<BootcoinsForSale> findById(String idBootcoinsForSale);
    public Mono<BootcoinsForSale> save(BootcoinsForSaleDto bootcoinsForSaleDto);

    public Mono<BootcoinsForSale> update(BootcoinsForSaleDto bootcoinsForSaleDto, String idBootcoinsForSale);
    public Mono<Void> delete(String idBootcoinsForSale);
    public Mono<BootcoinsForSale> findByDocumentNumber(String documentNumber);

}
