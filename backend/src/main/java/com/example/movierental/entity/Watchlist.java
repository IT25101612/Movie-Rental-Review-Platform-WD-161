package com.example.movierental.entity;

import java.time.LocalDateTime;

public class Watchlist {

    private Long id;
    private User user;
    private Movie movie;
    private LocalDateTime addedDate = LocalDateTime.now();
    private Status status = Status.WANT_TO_WATCH;

    public enum Status { WANT_TO_WATCH, WATCHING, WATCHED }

    public Watchlist() {}

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public User getUser()                      { return user; }
    public void setUser(User user)             { this.user = user; }
    public Movie getMovie()                    { return movie; }
    public void setMovie(Movie movie)          { this.movie = movie; }
    public LocalDateTime getAddedDate()        { return addedDate; }
    public void setAddedDate(LocalDateTime d)  { this.addedDate = d; }
    public Status getStatus()                  { return status; }
    public void setStatus(Status status)       { this.status = status; }
}
