package com.example.movierental.repository;

import com.example.movierental.entity.Rental;
import com.example.movierental.storage.FileStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RentalRepository {

    private final FileStore<RentalRecord> store;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public RentalRepository(
            @Value("${app.data.dir:./data}") String dataDir,
            ObjectMapper mapper,
            UserRepository userRepository,
            MovieRepository movieRepository) {
        this.store = new FileStore<>(Path.of(dataDir, "rentals.json"), mapper, new TypeReference<>() {});
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public Rental save(Rental rental) {
        List<RentalRecord> all = store.readAll();
        RentalRecord rec = toRecord(rental);
        if (rec.id == null) {
            long nextId = all.stream().mapToLong(r -> r.id == null ? 0L : r.id).max().orElse(0L) + 1;
            rec.id = nextId;
            rental.setId(nextId);
            all.add(rec);
        } else {
            all.replaceAll(r -> r.id.equals(rec.id) ? rec : r);
        }
        store.writeAll(all);
        return rental;
    }

    public List<Rental> findAll() {
        return store.readAll().stream().map(this::toRental).collect(Collectors.toList());
    }

    public Optional<Rental> findById(Long id) {
        return store.readAll().stream().filter(r -> r.id.equals(id)).map(this::toRental).findFirst();
    }

    public void delete(Rental rental) {
        List<RentalRecord> all = store.readAll();
        all.removeIf(r -> r.id.equals(rental.getId()));
        store.writeAll(all);
    }

    private RentalRecord toRecord(Rental r) {
        RentalRecord rec = new RentalRecord();
        rec.id          = r.getId();
        rec.userId      = r.getUser()  != null ? r.getUser().getId()  : null;
        rec.movieId     = r.getMovie() != null ? r.getMovie().getId() : null;
        rec.rentalDate  = r.getRentalDate();
        rec.returnDate  = r.getReturnDate();
        rec.status      = r.getStatus();
        rec.totalAmount = r.getTotalAmount();
        return rec;
    }

    private Rental toRental(RentalRecord rec) {
        Rental r = new Rental();
        r.setId(rec.id);
        if (rec.userId  != null) userRepository.findById(rec.userId).ifPresent(r::setUser);
        if (rec.movieId != null) movieRepository.findById(rec.movieId).ifPresent(r::setMovie);
        r.setRentalDate(rec.rentalDate);
        r.setReturnDate(rec.returnDate);
        r.setStatus(rec.status);
        r.setTotalAmount(rec.totalAmount);
        return r;
    }

    static class RentalRecord {
        public Long          id;
        public Long          userId;
        public Long          movieId;
        public LocalDate     rentalDate;
        public LocalDate     returnDate;
        public Rental.Status status;
        public BigDecimal    totalAmount;
    }
}
