package com.example.movierental.controller;

import com.example.movierental.entity.Rental;
import com.example.movierental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    public RentalController(RentalService rentalService) { this.rentalService = rentalService; }

    @PostMapping
    public ResponseEntity<Rental> create(@Valid @RequestBody Rental rental) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.createRental(rental));
    }
    @GetMapping
    public ResponseEntity<List<Rental>> getAll() { return ResponseEntity.ok(rentalService.getAllRentals()); }
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getById(@PathVariable Long id) { return ResponseEntity.ok(rentalService.getRentalById(id)); }
    @PutMapping("/{id}")
    public ResponseEntity<Rental> update(@PathVariable Long id, @Valid @RequestBody Rental rental) {
        return ResponseEntity.ok(rentalService.updateRental(id, rental));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { rentalService.deleteRental(id); return ResponseEntity.noContent().build(); }
}
