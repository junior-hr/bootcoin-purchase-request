package com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure;

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
public interface BootcoinPurchaseRequestRepository extends ReactiveMongoRepository<BootcoinPurchaseRequest, String> {
    @Query(value = "{'client.documentNumber' : ?0 }")
    Mono<BootcoinPurchaseRequest> findByDocumentNumber(
            String documentNumber);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0 } }",
            "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    Mono<BootcoinPurchaseRequestDto> findLastBootcoinMovementByAccount(String accountNumber);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0, 'idBootcoinMovement' : { $ne: ?1 } } }", "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    Mono<BootcoinPurchaseRequestDto> findLastBootcoinMovementByAccountExceptCurrentId(String accountNumber, String idBootcoinMovement);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinPurchaseRequestDto> findBootcoinMovementsByAccount(String accountNumber);

    @Query(value = "{$and:[{'movementDate':{$gte:  { '$date' : ?0} }},{'movementDate': {$lte:  { '$date' : ?1} }}],'accountNumber':?2}")
    Flux<BootcoinPurchaseRequest> findBootcoinMovementsByDateRange(String iniDate, String finalDate, String accountNumber);

    @Aggregation(pipeline = {"{ '$match': { 'credit.creditNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    public Mono<BootcoinPurchaseRequest> findByCreditNumber(Integer creditNumber);

    @Aggregation(pipeline = {"{ '$match': { 'loan.loanNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinPurchaseRequestDto> findBootcoinMovementsByLoanNumber(String loanNumber);

    @Aggregation(pipeline = {"{ '$match': { 'credit.creditNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinPurchaseRequestDto> findBootcoinMovementsByCreditNumber(String creditNumber);

}