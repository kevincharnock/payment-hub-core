package br.com.kevincharnock.domain.models.objectValues;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) {

    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount must have at most 2 decimal places.");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("Currency must not be blank.");
        }
    }
}
