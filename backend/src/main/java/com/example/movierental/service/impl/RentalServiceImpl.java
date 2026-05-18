package com.example.movierental.service.impl;

import com.example.movierental.entity.Rental;
import com.example.movierental.exception.ResourceNotFoundException;
import com.example.movierental.repository.RentalRepository;
import com.example.movierental.service.RentalService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    public RentalServiceImpl(RentalRepository rentalRepository) { this.rentalRepository = rentalRepository; }

    @Override public Rental createRental(Rental r) { return rentalRepository.save(r); }
    @Override public List<Rental> getAllRentals() { return rentalRepository.findAll(); }
    @Override public Rental getRentalById(Long id) {
        return rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rental","id",id));
    }
    @Override public Rental updateRental(Long id, Rental r) {
        Rental e = getRentalById(id);
        e.setUser(r.getUser()); e.setMovie(r.getMovie());
        e.setRentalDate(r.getRentalDate()); e.setReturnDate(r.getReturnDate());
        e.setStatus(r.getStatus()); e.setTotalAmount(r.getTotalAmount());
        return rentalRepository.save(e);
    }
    @Override public void deleteRental(Long id) { rentalRepository.delete(getRentalById(id)); }
}
