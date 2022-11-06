package com.nttdata.bootcamp.msbootcoinpurchaserequest.controller;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.application.BootcoinPurchaseRequestService;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.application.BootcoinsForSaleService;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinPurchaseRequestDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinsForSaleDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinsForSale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bootcoinpurchaserequest")
@Slf4j
public class BootcoinPurchaseRequestController {
    @Autowired
    private BootcoinPurchaseRequestService service;

    @Autowired
    private BootcoinsForSaleService bootcoinsForSaleService;

    @GetMapping("/BootcoinsForSale")
    public Mono<ResponseEntity<Flux<BootcoinsForSale>>> listBootcoinsForSales() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(bootcoinsForSaleService.findAll()));
    }

    @GetMapping("/BootcoinsForSale/{idBootcoinsForSale}")
    public Mono<ResponseEntity<BootcoinsForSale>> getBootcoinsForSaleDetails(@PathVariable("idBootcoinsForSale") String id) {
        return bootcoinsForSaleService.findById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/BootcoinsForSale/documentNumber/{documentNumber}")
    public Mono<ResponseEntity<BootcoinsForSale>> getBootcoinsForSaleByDocumentNumber(@PathVariable("documentNumber") String documentNumber) {
        return bootcoinsForSaleService.findByDocumentNumber(documentNumber).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/BootcoinsForSale")
    public Mono<ResponseEntity<Map<String, Object>>> saveBootcoinsForSale(@Valid @RequestBody Mono<BootcoinsForSaleDto> bootcoinsForSaleDto) {
        Map<String, Object> request = new HashMap<>();
        return bootcoinsForSaleDto.flatMap(mvDto ->
                bootcoinsForSaleService.save(mvDto).map(c -> {
                    request.put("Movimiento Bootcoin", c);
                    request.put("mensaje", "Movimiento de Bootcoin guardado con exito");
                    request.put("timestamp", new Date());
                    return ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(c.getIdBootcoinsForSale())))
                            .contentType(MediaType.APPLICATION_JSON).body(request);
                })
        );
    }

    @PutMapping("/BootcoinsForSale/{idBootcoinsForSale}")
    public Mono<ResponseEntity<BootcoinsForSale>> editBootcoinsForSale(@Valid @RequestBody BootcoinsForSaleDto bootcoinsForSaleDto, @PathVariable("idBootcoinsForSale") String idBootcoinsForSale) {
        return bootcoinsForSaleService.update(bootcoinsForSaleDto, idBootcoinsForSale)
                .map(c -> ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(idBootcoinsForSale)))
                        .contentType(MediaType.APPLICATION_JSON).body(c));
    }

    @DeleteMapping("/BootcoinsForSale/{idBootcoinsForSale}")
    public Mono<ResponseEntity<Void>> deleteBootcoinsForSale(@PathVariable("idBootcoinsForSale") String idBootcoinsForSale) {
        return bootcoinsForSaleService.delete(idBootcoinsForSale).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

    @GetMapping("/purchase")
    public Mono<ResponseEntity<Flux<BootcoinPurchaseRequest>>> listBootcoinPurchaseRequests() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
    }

    @GetMapping("/purchase/{idBootcoinPurchaseRequest}")
    public Mono<ResponseEntity<BootcoinPurchaseRequest>> getBootcoinPurchaseRequestDetails(@PathVariable("idBootcoinPurchaseRequest") String id) {
        return service.findById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/purchase/documentNumber/{documentNumber}")
    public Mono<ResponseEntity<BootcoinPurchaseRequest>> getBootcoinPurchaseRequestByDocumentNumber(@PathVariable("documentNumber") String documentNumber) {
        return service.findByDocumentNumber(documentNumber).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/purchase")
    public Mono<ResponseEntity<Map<String, Object>>> saveBootcoinPurchaseRequest(@Valid @RequestBody Mono<BootcoinPurchaseRequestDto> bootcoinPurchaseRequestDto) {
        Map<String, Object> request = new HashMap<>();
        return bootcoinPurchaseRequestDto.flatMap(mvDto ->
                service.save(mvDto).map(c -> {
                    request.put("Movimiento Bootcoin", c);
                    request.put("mensaje", "Movimiento de Bootcoin guardado con exito");
                    request.put("timestamp", new Date());
                    return ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(c.getIdBootcoinPurchaseRequest())))
                            .contentType(MediaType.APPLICATION_JSON).body(request);
                })
        );
    }

    @PutMapping("/purchase/{idBootcoinPurchaseRequest}")
    public Mono<ResponseEntity<BootcoinPurchaseRequest>> editBootcoinPurchaseRequest(@Valid @RequestBody BootcoinPurchaseRequestDto bootcoinPurchaseRequestDto, @PathVariable("idBootcoinPurchaseRequest") String idBootcoinPurchaseRequest) {
        return service.update(bootcoinPurchaseRequestDto, idBootcoinPurchaseRequest)
                .map(c -> ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(idBootcoinPurchaseRequest)))
                        .contentType(MediaType.APPLICATION_JSON).body(c));
    }

    @DeleteMapping("/purchase/{idBootcoinPurchaseRequest}")
    public Mono<ResponseEntity<Void>> deleteBootcoinPurchaseRequest(@PathVariable("idBootcoinPurchaseRequest") String idBootcoinPurchaseRequest) {
        return service.delete(idBootcoinPurchaseRequest).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

    @PostMapping("/purchase/acceptSale/{idBootcoinPurchaseRequest}")
    public Mono<ResponseEntity<Map<String, Object>>> acceptSaleBootcoinPurchaseRequest(@PathVariable("idBootcoinPurchaseRequest") String idBootcoinPurchaseRequest) {
        Map<String, Object> request = new HashMap<>();
        return Mono.just(idBootcoinPurchaseRequest).flatMap(id ->
                service.acceptSale(id).map(c -> {
                    request.put("Movimiento Bootcoin", c);
                    request.put("mensaje", "Movimiento de Bootcoin guardado con exito");
                    request.put("timestamp", new Date());
                    return ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(c.getIdBootcoinPurchaseRequest())))
                            .contentType(MediaType.APPLICATION_JSON).body(request);
                })
        );
    }
}
