package com.example.movierental.entity;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private Rental rental;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private LocalDateTime paymentDate = LocalDateTime.now();
    private String transactionId;

    public enum PaymentMethod { CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH, UPI }
    public enum PaymentStatus { PENDING, COMPLETED, FAILED, REFUNDED }

    public Payment() {}

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }
    public Rental getRental()                        { return rental; }
    public void setRental(Rental rental)             { this.rental = rental; }
    public BigDecimal getAmount()                    { return amount; }
    public void setAmount(BigDecimal amount)         { this.amount = amount; }
    public PaymentMethod getPaymentMethod()          { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod m)    { this.paymentMethod = m; }
    public PaymentStatus getPaymentStatus()          { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus s)    { this.paymentStatus = s; }
    public LocalDateTime getPaymentDate()            { return paymentDate; }
    public void setPaymentDate(LocalDateTime d)      { this.paymentDate = d; }
    public String getTransactionId()                 { return transactionId; }
    public void setTransactionId(String tid)         { this.transactionId = tid; }
}
