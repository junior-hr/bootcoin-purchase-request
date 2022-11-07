package com.nttdata.bootcamp.msbootcoinpurchaserequest.model;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class Movement.
 * Movement microservice class Movement.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Movement {

    @Id
    private String idMovement;

    private String accountNumber;

    private String cellphone;
    
    private Integer numberMovement;
    
    private String movementType;
    
    private Double amount;

    private Double balance;
    
    private String currency;

    private LocalDateTime movementDate;

    private String idBankAccount;

    private Double commission;

    private BankAccount bankAccountTransfer;

}