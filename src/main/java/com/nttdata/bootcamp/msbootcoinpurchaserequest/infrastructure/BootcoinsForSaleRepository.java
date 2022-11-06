package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinsForSaleDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinsForSale;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinPurchaseRequestDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class BootcoinMovementRepository.
 * BootcoinMovement microservice class BootcoinMovementRepository.
 */
public interface BootcoinsForSaleRepository extends ReactiveMongoRepository<BootcoinsForSale, String> {
    @Query(value = "{'client.documentNumber' : ?0 }")
    Mono<BootcoinsForSale> findByDocumentNumber(
            String documentNumber);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0 } }",
            "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    Mono<BootcoinsForSaleDto> findLastBootcoinMovementByAccount(String accountNumber);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0, 'idBootcoinMovement' : { $ne: ?1 } } }", "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    Mono<BootcoinsForSaleDto> findLastBootcoinMovementByAccountExceptCurrentId(String accountNumber, String idBootcoinMovement);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinsForSaleDto> findBootcoinMovementsByAccount(String accountNumber);

    @Query(value = "{$and:[{'movementDate':{$gte:  { '$date' : ?0} }},{'movementDate': {$lte:  { '$date' : ?1} }}],'accountNumber':?2}")
    Flux<BootcoinsForSale> findBootcoinMovementsByDateRange(String iniDate, String finalDate, String accountNumber);

    @Aggregation(pipeline = {"{ '$match': { 'credit.creditNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    public Mono<BootcoinsForSale> findByCreditNumber(Integer creditNumber);

    @Aggregation(pipeline = {"{ '$match': { 'loan.loanNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinsForSaleDto> findBootcoinMovementsByLoanNumber(String loanNumber);

    @Aggregation(pipeline = {"{ '$match': { 'credit.creditNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinsForSaleDto> findBootcoinMovementsByCreditNumber(String creditNumber);

}