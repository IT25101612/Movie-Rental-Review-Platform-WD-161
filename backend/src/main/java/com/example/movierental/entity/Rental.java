package com.example.movierental.entity;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Rental {

    private Long id;
    private User user;
    private Movie movie;

    @NotNull(message = "Rental date is required")
    private LocalDate rentalDate;

    private LocalDate returnDate;
    private Status status = Status.ACTIVE;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    public enum Status { ACTIVE, RETURNED, OVERDUE }

    public Rental() {}

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public User getUser()                      { return user; }
    public void setUser(User user)             { this.user = user; }
    public Movie getMovie()                    { return movie; }
    public void setMovie(Movie movie)          { this.movie = movie; }
    public LocalDate getRentalDate()           { return rentalDate; }
    public void setRentalDate(LocalDate d)     { this.rentalDate = d; }
    public LocalDate getReturnDate()           { return returnDate; }
    public void setReturnDate(LocalDate d)     { this.returnDate = d; }
    public Status getStatus()                  { return status; }
    public void setStatus(Status status)       { this.status = status; }
    public BigDecimal getTotalAmount()         { return totalAmount; }
    public void setTotalAmount(BigDecimal a)   { this.totalAmount = a; }
}
