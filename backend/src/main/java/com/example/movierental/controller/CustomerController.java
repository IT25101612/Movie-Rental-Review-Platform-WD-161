package com.example.movierental.controller;

import com.example.movierental.entity.Movie;
import com.example.movierental.entity.Payment;
import com.example.movierental.entity.Rental;
import com.example.movierental.entity.User;
import com.example.movierental.repository.MovieRepository;
import com.example.movierental.repository.PaymentRepository;
import com.example.movierental.repository.RentalRepository;
import com.example.movierental.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final MovieRepository   movieRepository;
    private final RentalRepository  rentalRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository    userRepository;

    public CustomerController(MovieRepository movieRepository,
                               RentalRepository rentalRepository,
                               PaymentRepository paymentRepository,
                               UserRepository userRepository) {
        this.movieRepository   = movieRepository;
        this.rentalRepository  = rentalRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository    = userRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Movie>  availableMovies = movieRepository.findAll().stream()
                .filter(Movie::getAvailable).toList();
        List<Rental> myRentals = rentalRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(user.getId())).toList();

        Set<Long> myRentalIds = myRentals.stream()
                .map(Rental::getId).collect(Collectors.toSet());
        List<Payment> myPayments = paymentRepository.findAll().stream()
                .filter(p -> p.getRental() != null && myRentalIds.contains(p.getRental().getId())).toList();
        BigDecimal totalSpent = myPayments.stream()
                .filter(p -> p.getPaymentStatus() == Payment.PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("welcome",          "Welcome, " + user.getFullName());
        data.put("role",             "CUSTOMER");
        data.put("availableMovies",  availableMovies.size());
        data.put("myRentals",        myRentals.size());
        data.put("activeRentals",    myRentals.stream()
                .filter(r -> r.getStatus() == Rental.Status.ACTIVE).count());
        data.put("totalSpent",       totalSpent);
        data.put("myPayments",       myPayments.size());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> browseMovies() {
        return ResponseEntity.ok(
                movieRepository.findAll().stream().filter(Movie::getAvailable).toList());
    }

    @GetMapping("/my-rentals")
    public ResponseEntity<List<Rental>> myRentals(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(
                rentalRepository.findAll().stream()
                        .filter(r -> r.getUser().getId().equals(user.getId())).toList());
    }

    @GetMapping("/my-payments")
    public ResponseEntity<List<Payment>> myPayments(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Set<Long> myRentalIds = rentalRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .map(Rental::getId)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(
                paymentRepository.findAll().stream()
                        .filter(p -> p.getRental() != null && myRentalIds.contains(p.getRental().getId()))
                        .toList());
    }

    /** Rent a movie and auto-create its payment in one step. */
    @PostMapping("/rent/{movieId}")
    public ResponseEntity<Map<String, Object>> rentMovie(
            @PathVariable Long movieId,
            @RequestBody Map<String, Object> body,
            Authentication auth) {

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (!Boolean.TRUE.equals(movie.getAvailable())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Movie is not available for rent"));
        }

        int rentalDays = body.get("rentalDays") != null
                ? Integer.parseInt(body.get("rentalDays").toString()) : 3;
        String paymentMethod = body.get("paymentMethod") != null
                ? body.get("paymentMethod").toString() : "CREDIT_CARD";

        BigDecimal totalAmount = movie.getRentalPrice()
                .multiply(BigDecimal.valueOf(rentalDays));

        // Create Rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setMovie(movie);
        rental.setRentalDate(java.time.LocalDate.now());
        rental.setReturnDate(java.time.LocalDate.now().plusDays(rentalDays));
        rental.setStatus(Rental.Status.ACTIVE);
        rental.setTotalAmount(totalAmount);
        rental = rentalRepository.save(rental);

        // Create Payment
        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentMethod));
        payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
        payment.setPaymentDate(java.time.LocalDateTime.now());
        payment.setTransactionId("TXN-" + java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        payment = paymentRepository.save(payment);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Movie rented successfully!");
        result.put("rental", rental);
        result.put("payment", payment);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(result);
    }
}

