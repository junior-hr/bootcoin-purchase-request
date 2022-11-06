package com.nttdata.bootcamp.msbootcoinpurchaserequest.application;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.dto.BootcoinPurchaseRequestDto;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure.BankAccountRepository;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure.BootcoinPurchaseRequestRepository;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure.ClientRepository;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.infrastructure.MobileWalletRepository;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BootcoinPurchaseRequest;
import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BootcoinPurchaseRequestServiceImpl implements BootcoinPurchaseRequestService {

    @Autowired
    private BootcoinPurchaseRequestRepository bootcoinPurchaseRequestRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private MobileWalletRepository mobileWalletRepository;

    @Override
    public Flux<BootcoinPurchaseRequest> findAll() {
        return bootcoinPurchaseRequestRepository.findAll();
    }

    @Override
    public Mono<BootcoinPurchaseRequest> findById(String idBankAccount) {
        return Mono.just(idBankAccount)
                .flatMap(bootcoinPurchaseRequestRepository::findById)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta Bancaria", "idBankAccount", idBankAccount)));
    }

    @Override
    public Mono<BootcoinPurchaseRequest> findByDocumentNumber(String documentNumber) {
        return Mono.just(documentNumber)
                .flatMap(bootcoinPurchaseRequestRepository::findByDocumentNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("BootCoin", "documentNumber", documentNumber)));
    }

    @Override
    public Mono<BootcoinPurchaseRequest> save(BootcoinPurchaseRequestDto bootcoinPurchaseRequestDto) {
        log.info("----save-------bootcoinPurchaseRequestDto : " + bootcoinPurchaseRequestDto.toString());
        return Mono.just(bootcoinPurchaseRequestDto)
                .flatMap(bprd -> clientRepository.findClientByDni(bprd.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(cl -> {
                            bprd.setBalance(bprd.getAmount());
                            return Mono.just(cl);
                        })
                        .flatMap(cl -> bprd.MapperToBootcoinPurchaseRequest(cl))
                        .flatMap(bpr -> validatesIfUouHaveABankAccount(bpr.getClient())
                                .then(Mono.just(bpr)))
                        .flatMap(bootcoinPurchaseRequestRepository::save)
                );
    }

    public Mono<Boolean> validatesIfUouHaveABankAccount(Client client) {
        log.info("--validatesIfUouHaveABankAccount-------: ");
        return bankAccountRepository.findByDocumentNumber(client.getDocumentNumber())
                .flatMap(cnt -> {
                    if (cnt > 0) {
                        return Mono.just(true);
                    } else {
                        return mobileWalletRepository.findByDocumentNumber(client.getDocumentNumber())
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
    public Mono<BootcoinPurchaseRequest> update(BootcoinPurchaseRequestDto bootcoinPurchaseRequestDto, String idbootcoinPurchaseRequest) {
        log.info("----update-------BootcoinPurchaseRequestDto -- idbootcoinPurchaseRequest: " + bootcoinPurchaseRequestDto.toString() + " -- " + idbootcoinPurchaseRequest);
        return Mono.just(bootcoinPurchaseRequestDto)
                .flatMap(bprd -> clientRepository.findClientByDni(bprd.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(cl -> {
                            bprd.setBalance(bprd.getAmount());
                            return Mono.just(cl);
                        })
                        .flatMap(cl -> bprd.MapperToBootcoinPurchaseRequest(cl))
                        .flatMap(bpr -> validatesIfUouHaveABankAccount(bpr.getClient())
                                .then(Mono.just(bpr)))
                        .flatMap(bpr -> bootcoinPurchaseRequestRepository.findById(idbootcoinPurchaseRequest)
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("bootcoin Purchase Request", "idbootcoinPurchaseRequest", idbootcoinPurchaseRequest)))
                                .flatMap(x -> {
                                    bpr.setIdBootcoinPurchaseRequest(x.getIdBootcoinPurchaseRequest());
                                    return bootcoinPurchaseRequestRepository.save(bpr);
                                })
                        )
                );
    }

    @Override
    public Mono<Void> delete(String idBootcoinPurchaseRequest) {
        return Mono.just(idBootcoinPurchaseRequest)
                .flatMap(b -> bootcoinPurchaseRequestRepository.findById(b))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin Purchase Request", "idBootcoinPurchaseRequest", idBootcoinPurchaseRequest)))
                .flatMap(bootcoinPurchaseRequestRepository::delete);
    }


    @Override
    public Mono<BootcoinPurchaseRequest> saveBootcoinPurchaseRequest(BootcoinPurchaseRequestDto bootcoinPurchaseRequestDto) {
        log.info("----saveBootcoinPurchaseRequest-------bootcoinPurchaseRequestDto : " + bootcoinPurchaseRequestDto.toString());
        return Mono.just(bootcoinPurchaseRequestDto)
                .flatMap(bprd -> clientRepository.findClientByDni(bprd.getDocumentNumber())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", "DocumentNumber", bprd.getDocumentNumber())))
                        .flatMap(cl -> {
                            bprd.setBalance(bprd.getAmount());
                            return Mono.just(cl);
                        })
                        .flatMap(cl -> bprd.MapperToBootcoinPurchaseRequest(cl))
                        .flatMap(bpr -> validatesIfUouHaveABankAccount(bpr.getClient())
                                .then(Mono.just(bpr)))
                        .flatMap(bootcoinPurchaseRequestRepository::save)
                );
    }

}
