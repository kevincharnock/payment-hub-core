package br.com.kevincharnock.domain.models.objectValues;

import java.util.Objects;
import java.util.UUID;

public record PaymentId(UUID paymentId) {

    public PaymentId {
        Objects.requireNonNull(paymentId);
    }

}
