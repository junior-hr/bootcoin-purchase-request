package com.nttdata.bootcamp.msbootcoinpurchaserequest.application;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinPurchaseRequestDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinsForSaleDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure.*;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.Bootcoin;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinsForSale;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BootcoinsForSaleServiceImpl implements BootcoinsForSaleService {

    @Autowired
    private BootcoinsForSaleRepository bootcoinsForSaleRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private MobileWalletRepository mobileWalletRepository;

    @Autowired
    private BootcoinRepository bootcoinRepository;

    @Override
    public Flux<BootcoinsForSale> findAll() {
        return bootcoinsForSaleRepository.findAll();
    }

    @Override
    public Mono<BootcoinsForSale> findById(String idBankAccount) {
        return Mono.just(idBankAccount)
                .flatMap(bootcoinsForSaleRepository::findById)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "idBankAccount", idBankAccount)));
    }

    @Override
    public Mono<BootcoinsForSale> findByDocumentNumber(String documentNumber) {
        return Mono.just(documentNumber)
                .flatMap(bootcoinsForSaleRepository::findByDocumentNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("BootCoin", "documentNumber", documentNumber)));
    }

    @Override
    public Mono<BootcoinsForSale> save(BootcoinsForSaleDto bootcoinsForSaleDto) {
        log.info("----save-------bootcoinsForSaleDto : " + bootcoinsForSaleDto.toString());
        return Mono.just(bootcoinsForSaleDto)
                .flatMap(bprd -> clientRepository.findClientByDni(bprd.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(cl -> {
                            bprd.setBalance(bprd.getAmount());
                            bprd.setClient(cl);
                            return Mono.just(cl);
                        })
                        .flatMap(bc -> bootcoinRepository.findBootcoinByDocumentNumber(bprd.getDocumentNumber()))
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(bc -> validateBalance(bc, bprd.getAmount()))
                        .flatMap(bcn -> bprd.MapperToBootcoinPurchaseRequest(bcn, "onSale"))
                        .flatMap(bpr -> validatesIfYouHaveABankAccount(bpr.getClient())
                                .then(Mono.just(bpr)))
                        .flatMap(bootcoinsForSaleRepository::save)
                );
    }
    public Mono<Bootcoin> validateBalance(Bootcoin bootcoin, Double amount) {
        if(bootcoin.getBalance() < amount){
            return Mono.error(new ResourceNotFoundException("Bootcoin", "Balance", bootcoin.getBalance().toString()));
        }
        return Mono.just(bootcoin);
    }

    public Mono<Boolean> validatesIfYouHaveABankAccount(Client client) {
        log.info("--validatesIfYouHaveABankAccount-------: ");
        return bankAccountRepository.findCantByDocumentNumber(client.getDocumentNumber())
                .flatMap(cnt -> {
                    if (cnt > 0) {
                        return Mono.just(true);
                    } else {
                        return mobileWalletRepository.findByCantDocumentNumber(client.getDocumentNumber())
                                .flatMap(cntmw -> {
                                    if (cntmw > 0) {
                                        return Mono.just(true);
                                    } else {
                                        return Mono.error(new ResourceNotFoundException("Cuenta bancaria", "Cant", cnt.toString()));
                                    }
                                });
                    }
                });
    }

    @Override
    public Mono<BootcoinsForSale> update(BootcoinsForSaleDto bootcoinsForSaleDto, String idBootcoinsForSale) {
        log.info("----update-------bootcoinsForSaleDto -- idBootcoinsForSale: " + bootcoinsForSaleDto.toString() + " -- " + idBootcoinsForSale);
        return Mono.just(bootcoinsForSaleDto)
                .flatMap(bprd -> clientRepository.findClientByDni(bprd.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(cl -> {
                            bprd.setBalance(bprd.getAmount());
                            bprd.setClient(cl);
                            return Mono.just(cl);
                        })
                        .flatMap(bc -> bootcoinRepository.findBootcoinByDocumentNumber(bprd.getDocumentNumber()))
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(bcn -> bprd.MapperToBootcoinPurchaseRequest(bcn,"onSale"))
                        .flatMap(bpr -> validatesIfYouHaveABankAccount(bpr.getClient())
                                .then(Mono.just(bpr)))
                        .flatMap(bpr -> bootcoinsForSaleRepository.findById(idBootcoinsForSale)
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("bootcoin Purchase Request", "idbootcoinPurchaseRequest", idBootcoinsForSale)))
                                .flatMap(x -> {
                                    bpr.setIdBootcoinsForSale(x.getIdBootcoinsForSale());
                                    return bootcoinsForSaleRepository.save(bpr);
                                })
                        )
                );
    }

    @Override
    public Mono<Void> delete(String idBootcoinPurchaseRequest) {
        return Mono.just(idBootcoinPurchaseRequest)
                .flatMap(b -> bootcoinsForSaleRepository.findById(b))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin Purchase Request", "idBootcoinPurchaseRequest", idBootcoinPurchaseRequest)))
                .flatMap(bootcoinsForSaleRepository::delete);
    }

}
