package br.com.kevincharnock.domain.models.entidades;


import br.com.kevincharnock.domain.enums.PaymentStatus;
import br.com.kevincharnock.domain.models.objectValues.Money;
import br.com.kevincharnock.domain.models.objectValues.PaymentId;

import java.time.Instant;
import java.util.UUID;

public class Payments {

    private final PaymentId paymentId;
    private final Money amount;
    private final String description;

    private PaymentStatus paymentStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    public Payments(PaymentId paymentId, Money amount, String description, PaymentStatus paymentStatus, Instant createdAt, Instant updatedAt) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.description = description;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //implementando design patter factory para deixar setado um pagamento já válido
    public static Payments create(Money amount, String description) {
        var now = Instant.now();
        return new Payments(
                new PaymentId(UUID.randomUUID()),
                amount,
                description,
                PaymentStatus.CREATED,
                now,
                now
        );
    }

    /** Regra: só pode autorizar se estiver CREATED */
    public void authorize() {
        if (this.paymentStatus != PaymentStatus.CREATED) {
            throw new IllegalStateException("Only CREATED payments can be authorized.");
        }
        this.paymentStatus = PaymentStatus.AUTHORIZED;
        this.updatedAt = Instant.now();
    }

    /** Regra: só pode capturar se estiver AUTHORIZED */
    public void capture() {
        if (this.paymentStatus != PaymentStatus.AUTHORIZED) {
            throw new IllegalStateException("Only AUTHORIZED payments can be captured.");
        }
        this.paymentStatus = PaymentStatus.CAPTURED;
        this.updatedAt = Instant.now();
    }

    /** Regra: falha pode ocorrer em qualquer estado não final */
    public void fail(String reason) {
        if (this.paymentStatus == PaymentStatus.CAPTURED || this.paymentStatus == PaymentStatus.CANCELED) {
            throw new IllegalStateException("Cannot fail a finalized payment.");
        }
        this.paymentStatus = PaymentStatus.FAILED;
        this.updatedAt = Instant.now();
        // se quiser, você pode guardar reason em outro atributo depois (ex: failureReason)
    }

    public void cancel() {
        if (this.paymentStatus == PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Cannot cancel a captured payment.");
        }
        this.paymentStatus = PaymentStatus.CANCELED;
        this.updatedAt = Instant.now();
    }

    // SEM SETTER, Mudança de estado só por métodos de negócio
    //create() como factory (bem comum em domínio)
    public PaymentId getPaymentId() {
        return paymentId;
    }

    public Money getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
