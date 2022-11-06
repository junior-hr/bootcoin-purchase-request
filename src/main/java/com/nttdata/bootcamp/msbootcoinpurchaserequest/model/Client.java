package com.nttdata.bootcamp.msbootcoinpurchaserequest.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class Client.
 * BootcoinMovement microservice class Client.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Client {

    @Id
    private String idClient;
    private String names;
    private String documentNumber;
    private Integer cellphone;
    private String email;
}
