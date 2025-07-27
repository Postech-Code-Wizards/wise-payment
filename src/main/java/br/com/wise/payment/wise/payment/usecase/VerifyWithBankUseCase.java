package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.domain.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class VerifyWithBankUseCase {
    private static final String NO_FUNDS_MESSAGE = "Insufficient Balance for the transaction";
    private static final String CARD_NUMBER_INVALID = "Credit card number do not match any";

    public boolean execute(Payment payment) {
        BigDecimal balance = getBalance();
        Integer bankCreditCardLength = getBanksCreditCardInformation(payment.getCreditCardNumber().length());

        boolean hasFunds = hasFunds(payment.getTotalValue(), balance);
        if (!hasFunds) payment.messageInformation(NO_FUNDS_MESSAGE);
        boolean validateCard = payment.getCreditCardNumber().length() == bankCreditCardLength;
        if (!validateCard) payment.messageInformation(CARD_NUMBER_INVALID);

        return hasFunds && validateCard;
    }

    private boolean hasFunds(BigDecimal billValue, BigDecimal balance) {
        return balance.compareTo(billValue) > 0;
    }

    private Integer getBanksCreditCardInformation(int clientCardLength) {
        if (clientCardLength == 14 || clientCardLength == 16) {
            return clientCardLength;
        }
        return 0;
    }

    private BigDecimal getBalance() {
        return randomizeBalance();
    }

    private BigDecimal randomizeBalance() {
        Random random = new Random();
        return BigDecimal.valueOf(random.nextDouble() * 10000.00);
    }
}
