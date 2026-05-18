package com.example.movierental.controller;

import com.example.movierental.entity.Payment;
import com.example.movierental.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody Payment payment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(payment));
    }
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() { return ResponseEntity.ok(paymentService.getAllPayments()); }
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) { return ResponseEntity.ok(paymentService.getPaymentById(id)); }
    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @Valid @RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.updatePayment(id, payment));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { paymentService.deletePayment(id); return ResponseEntity.noContent().build(); }
}
