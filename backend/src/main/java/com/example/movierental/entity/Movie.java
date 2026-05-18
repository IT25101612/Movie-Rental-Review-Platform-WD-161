package com.example.movierental.entity;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movie {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String genre;
    private String description;
    private Integer releaseYear;

    @NotNull(message = "Rental price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal rentalPrice;

    private Boolean available = true;
    private String posterUrl;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Movie() {}

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }
    public String getTitle()                  { return title; }
    public void setTitle(String title)        { this.title = title; }
    public String getGenre()                  { return genre; }
    public void setGenre(String genre)        { this.genre = genre; }
    public String getDescription()            { return description; }
    public void setDescription(String d)      { this.description = d; }
    public Integer getReleaseYear()           { return releaseYear; }
    public void setReleaseYear(Integer y)     { this.releaseYear = y; }
    public BigDecimal getRentalPrice()        { return rentalPrice; }
    public void setRentalPrice(BigDecimal p)  { this.rentalPrice = p; }
    public Boolean getAvailable()             { return available; }
    public void setAvailable(Boolean a)       { this.available = a; }
    public String getPosterUrl()              { return posterUrl; }
    public void setPosterUrl(String url)      { this.posterUrl = url; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}
