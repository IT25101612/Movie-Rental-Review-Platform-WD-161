package com.example.movierental.service;
import com.example.movierental.entity.Movie;
import java.util.List;

public interface MovieService {
    Movie createMovie(Movie movie);
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    Movie updateMovie(Long id, Movie movie);
    void deleteMovie(Long id);
}
