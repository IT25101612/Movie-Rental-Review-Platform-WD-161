package com.example.movierental.repository;

import com.example.movierental.entity.Payment;
import com.example.movierental.storage.FileStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentRepository {

    private final FileStore<PaymentRecord> store;
    private final RentalRepository rentalRepository;

    public PaymentRepository(
            @Value("${app.data.dir:./data}") String dataDir,
            ObjectMapper mapper,
            RentalRepository rentalRepository) {
        this.store = new FileStore<>(Path.of(dataDir, "payments.json"), mapper, new TypeReference<>() {});
        this.rentalRepository = rentalRepository;
    }

    public Payment save(Payment payment) {
        List<PaymentRecord> all = store.readAll();
        PaymentRecord rec = toRecord(payment);
        if (rec.id == null) {
            long nextId = all.stream().mapToLong(p -> p.id == null ? 0L : p.id).max().orElse(0L) + 1;
            rec.id = nextId;
            payment.setId(nextId);
            all.add(rec);
        } else {
            all.replaceAll(p -> p.id.equals(rec.id) ? rec : p);
        }
        store.writeAll(all);
        return payment;
    }

    public List<Payment> findAll() {
        return store.readAll().stream().map(this::toPayment).collect(Collectors.toList());
    }

    public Optional<Payment> findById(Long id) {
        return store.readAll().stream().filter(p -> p.id.equals(id)).map(this::toPayment).findFirst();
    }

    public void delete(Payment payment) {
        List<PaymentRecord> all = store.readAll();
        all.removeIf(p -> p.id.equals(payment.getId()));
        store.writeAll(all);
    }

    private PaymentRecord toRecord(Payment p) {
        PaymentRecord rec  = new PaymentRecord();
        rec.id             = p.getId();
        rec.rentalId       = p.getRental() != null ? p.getRental().getId() : null;
        rec.amount         = p.getAmount();
        rec.paymentMethod  = p.getPaymentMethod();
        rec.paymentStatus  = p.getPaymentStatus();
        rec.paymentDate    = p.getPaymentDate();
        rec.transactionId  = p.getTransactionId();
        return rec;
    }

    private Payment toPayment(PaymentRecord rec) {
        Payment p = new Payment();
        p.setId(rec.id);
        if (rec.rentalId != null) rentalRepository.findById(rec.rentalId).ifPresent(p::setRental);
        p.setAmount(rec.amount);
        p.setPaymentMethod(rec.paymentMethod);
        p.setPaymentStatus(rec.paymentStatus);
        p.setPaymentDate(rec.paymentDate);
        p.setTransactionId(rec.transactionId);
        return p;
    }

    static class PaymentRecord {
        public Long                   id;
        public Long                   rentalId;
        public BigDecimal             amount;
        public Payment.PaymentMethod  paymentMethod;
        public Payment.PaymentStatus  paymentStatus;
        public LocalDateTime          paymentDate;
        public String                 transactionId;
    }
}
