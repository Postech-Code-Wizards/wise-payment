package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.domain.Payment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VerifyWithBankTest {

    @Test
    void shouldReturnTrueWhenCardIsValidAndHasSufficientFunds() {
        Payment payment = mock(Payment.class);
        when(payment.getTotalValue()).thenReturn(BigDecimal.valueOf(1));
        when(payment.getCreditCardNumber()).thenReturn("12345678901234");

        VerifyWithBank verifyWithBank = new VerifyWithBank();
        boolean result = verifyWithBank.execute(payment);

        assertTrue(result);
        verify(payment, never()).messageInformation(anyString());
    }

    @Test
    void shouldReturnFalseWhenInsufficientFunds() {
        Payment payment = mock(Payment.class);
        when(payment.getTotalValue()).thenReturn(BigDecimal.valueOf(100000));
        when(payment.getCreditCardNumber()).thenReturn("12345678901234");

        VerifyWithBank verifyWithBank = new VerifyWithBank();
        boolean result = verifyWithBank.execute(payment);

        assertFalse(result);
        verify(payment).messageInformation("Insufficient Balance for the transaction");
    }

    @Test
    void shouldReturnFalseWhenCardIsInvalid() {
        Payment payment = mock(Payment.class);
        when(payment.getTotalValue()).thenReturn(BigDecimal.valueOf(1));
        when(payment.getCreditCardNumber()).thenReturn("123");

        VerifyWithBank verifyWithBank = new VerifyWithBank();
        boolean result = verifyWithBank.execute(payment);

        assertFalse(result);
        verify(payment).messageInformation("Credit card number do not match any");
    }

    @Test
    void shouldReturnFalseWhenBothCardInvalidAndNoFunds() {
        Payment payment = mock(Payment.class);
        when(payment.getTotalValue()).thenReturn(BigDecimal.valueOf(100000));
        when(payment.getCreditCardNumber()).thenReturn("123");

        VerifyWithBank verifyWithBank = new VerifyWithBank();
        boolean result = verifyWithBank.execute(payment);

        assertFalse(result);
        verify(payment).messageInformation("Insufficient Balance for the transaction");
        verify(payment).messageInformation("Credit card number do not match any");
    }
}