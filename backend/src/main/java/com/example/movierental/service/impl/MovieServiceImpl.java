package com.example.movierental.service.impl;

import com.example.movierental.entity.Movie;
import com.example.movierental.exception.ResourceNotFoundException;
import com.example.movierental.repository.MovieRepository;
import com.example.movierental.service.MovieService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    public MovieServiceImpl(MovieRepository movieRepository) { this.movieRepository = movieRepository; }

    @Override public Movie createMovie(Movie movie) { return movieRepository.save(movie); }
    @Override public List<Movie> getAllMovies() { return movieRepository.findAll(); }
    @Override public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie","id",id));
    }
    @Override public Movie updateMovie(Long id, Movie m) {
        Movie e = getMovieById(id);
        e.setTitle(m.getTitle()); e.setGenre(m.getGenre()); e.setDescription(m.getDescription());
        e.setReleaseYear(m.getReleaseYear()); e.setRentalPrice(m.getRentalPrice());
        e.setAvailable(m.getAvailable()); e.setPosterUrl(m.getPosterUrl());
        return movieRepository.save(e);
    }
    @Override public void deleteMovie(Long id) { movieRepository.delete(getMovieById(id)); }
}
