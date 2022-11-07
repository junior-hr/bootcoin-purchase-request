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
 * Class BootcoinMovement.
 * BootcoinMovement microservice class BootcoinMovement.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BootcoinMovement {

    @Id
    private String idBootcoinMovement;
    private String documentNumber;
    private String cellphone;
    private String bootcoinMovementType;
    private Double amount;
    private Double balance;
    private String currency;
    private String documentNumberForTransfer;

}