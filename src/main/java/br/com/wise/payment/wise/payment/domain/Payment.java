package br.com.wise.payment.wise.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Payment {
    private UUID paymentId;
    private BigDecimal totalValue;
    private Status status;
    private String creditCardNumber;
    private String message;

    public void newStatus(Status status) {
        this.status = status;
    }

    public void messageInformation(String message) {
        this.message = message;
    }
}
