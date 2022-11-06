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

    @GetMapping
    public Mono<ResponseEntity<Flux<BootcoinsForSale>>> listBootcoinsForSales() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(bootcoinsForSaleService.findAll()));
    }

    @GetMapping("/{idBootcoinsForSale}")
    public Mono<ResponseEntity<BootcoinsForSale>> getBootcoinsForSaleDetails(@PathVariable("idBootcoinsForSale") String id) {
        return bootcoinsForSaleService.findById(id).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public Mono<ResponseEntity<BootcoinsForSale>> getBootcoinsForSaleByDocumentNumber(@PathVariable("documentNumber") String documentNumber) {
        return bootcoinsForSaleService.findByDocumentNumber(documentNumber).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> saveBootcoinsForSale(@Valid @RequestBody Mono<BootcoinsForSaleDto> bootcoinsForSaleDto) {
        Map<String, Object> request = new HashMap<>();
        return bootcoinsForSaleDto.flatMap(mvDto ->
                bootcoinsForSaleService.save(mvDto).map(c -> {
                    request.put("Movimiento Bootcoin", c);
                    request.put("mensaje", "Movimiento de Bootcoin guardado con exito");
                    request.put("timestamp", new Date());
                    return ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(c.getIdBootcoinPurchaseRequest())))
                            .contentType(MediaType.APPLICATION_JSON).body(request);
                })
        );
    }

    @PutMapping("/{idBootcoinsForSale}")
    public Mono<ResponseEntity<BootcoinsForSale>> editBootcoinsForSale(@Valid @RequestBody BootcoinsForSaleDto bootcoinsForSaleDto, @PathVariable("idBootcoinsForSale") String idBootcoinsForSale) {
        return bootcoinsForSaleService.update(bootcoinsForSaleDto, idBootcoinsForSale)
                .map(c -> ResponseEntity.created(URI.create("/api/bootcoinpurchaserequest/".concat(idBootcoinsForSale)))
                        .contentType(MediaType.APPLICATION_JSON).body(c));
    }

    @DeleteMapping("/{idBootcoinsForSale}")
    public Mono<ResponseEntity<Void>> deleteBootcoinsForSale(@PathVariable("idBootcoinsForSale") String idBootcoinsForSale) {
        return bootcoinsForSaleService.delete(idBootcoinsForSale).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }
}
