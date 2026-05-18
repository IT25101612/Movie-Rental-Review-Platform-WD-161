package com.example.movierental.entity;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class Review {

    private Long id;
    private User user;
    private Movie movie;

    @Min(1) @Max(5)
    private Integer rating;

    private String comment;
    private LocalDateTime reviewDate = LocalDateTime.now();

    public Review() {}

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public User getUser()                      { return user; }
    public void setUser(User user)             { this.user = user; }
    public Movie getMovie()                    { return movie; }
    public void setMovie(Movie movie)          { this.movie = movie; }
    public Integer getRating()                 { return rating; }
    public void setRating(Integer rating)      { this.rating = rating; }
    public String getComment()                 { return comment; }
    public void setComment(String comment)     { this.comment = comment; }
    public LocalDateTime getReviewDate()       { return reviewDate; }
    public void setReviewDate(LocalDateTime d) { this.reviewDate = d; }
}
