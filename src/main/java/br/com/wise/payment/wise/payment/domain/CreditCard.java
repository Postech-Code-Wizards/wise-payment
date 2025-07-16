package br.com.wise.payment.wise.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreditCard {
    private Long id;
    private String number;
    private String expirationDate;
    private String cvv;
    private String ownerName;

}
