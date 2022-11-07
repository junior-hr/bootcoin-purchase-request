package com.nttdata.bootcamp.msbootcoinpurchaserequest.dto;

import com.nttdata.bootcamp.msbootcoinpurchaserequest.model.BankAccount;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Class Movement.
 * Movement microservice class Movement.
 */
@Document(collection = "Movement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MovementDto {

    @Id
    private String idMovement;

    private String accountNumber;

    private String debitCardNumber;

    private Integer numberMovement;

    private Integer creditNumber;

    private Integer loanNumber;

    private String cellphone;

    private String movementType;

    private Double amount;

    private Double balance;

    private String currency;

    private Double commission;

    private String accountNumberForTransfer;

}