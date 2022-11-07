package com.nttdata.bootcamp.msbootcoinpurchaserequest.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class BankAccount.
 * Movement microservice class BankAccount.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BankAccount {

    @Id
    private String idBankAccount;

    private Client client;

    private String accountType;

    private String accountNumber;

    private Double commission;

    private Integer movementDate;

    private Integer maximumMovement;

    private Double startingAmount;

    private Double transactionLimit;

    private Double commissionTransaction;
    
    private String currency;

    private Double minimumAmount;

    private Double balance;

}
