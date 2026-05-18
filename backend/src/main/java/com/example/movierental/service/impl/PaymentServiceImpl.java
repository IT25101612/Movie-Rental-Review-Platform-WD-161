package com.example.movierental.service.impl;

import com.example.movierental.entity.Payment;
import com.example.movierental.exception.ResourceNotFoundException;
import com.example.movierental.repository.PaymentRepository;
import com.example.movierental.service.PaymentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository) { this.paymentRepository = paymentRepository; }

    @Override public Payment createPayment(Payment p) { return paymentRepository.save(p); }
    @Override public List<Payment> getAllPayments() { return paymentRepository.findAll(); }
    @Override public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment","id",id));
    }
    @Override public Payment updatePayment(Long id, Payment p) {
        Payment e = getPaymentById(id);
        e.setRental(p.getRental()); e.setAmount(p.getAmount());
        e.setPaymentMethod(p.getPaymentMethod()); e.setPaymentStatus(p.getPaymentStatus());
        e.setPaymentDate(p.getPaymentDate()); e.setTransactionId(p.getTransactionId());
        return paymentRepository.save(e);
    }
    @Override public void deletePayment(Long id) { paymentRepository.delete(getPaymentById(id)); }
}
