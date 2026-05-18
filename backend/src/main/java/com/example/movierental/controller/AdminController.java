package com.example.movierental.controller;

import com.example.movierental.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository    userRepository;
    private final MovieRepository   movieRepository;
    private final RentalRepository  rentalRepository;
    private final ReviewRepository  reviewRepository;
    private final PaymentRepository paymentRepository;

    public AdminController(UserRepository userRepository,
                           MovieRepository movieRepository,
                           RentalRepository rentalRepository,
                           ReviewRepository reviewRepository,
                           PaymentRepository paymentRepository) {
        this.userRepository    = userRepository;
        this.movieRepository   = movieRepository;
        this.rentalRepository  = rentalRepository;
        this.reviewRepository  = reviewRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(Authentication auth) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("welcome",       "Welcome, " + auth.getName());
        data.put("role",          "ADMIN");
        data.put("totalUsers",    userRepository.findAll().size());
        data.put("totalMovies",   movieRepository.findAll().size());
        data.put("totalRentals",  rentalRepository.findAll().size());
        data.put("totalReviews",  reviewRepository.findAll().size());
        data.put("totalPayments", paymentRepository.findAll().size());
        return ResponseEntity.ok(data);
    }
}
