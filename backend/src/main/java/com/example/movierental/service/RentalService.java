package com.example.movierental.service;
import com.example.movierental.entity.Rental;
import java.util.List;

public interface RentalService {
    Rental createRental(Rental rental);
    List<Rental> getAllRentals();
    Rental getRentalById(Long id);
    Rental updateRental(Long id, Rental rental);
    void deleteRental(Long id);
}
