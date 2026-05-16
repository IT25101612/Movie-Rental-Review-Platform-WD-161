package com.example.movierental.controller;

import com.example.movierental.entity.Movie;
import com.example.movierental.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService) { this.movieService = movieService; }

    @PostMapping
    public ResponseEntity<Movie> create(@Valid @RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }
    @GetMapping
    public ResponseEntity<List<Movie>> getAll() { return ResponseEntity.ok(movieService.getAllMovies()); }
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getById(@PathVariable Long id) { return ResponseEntity.ok(movieService.getMovieById(id)); }
    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { movieService.deleteMovie(id); return ResponseEntity.noContent().build(); }
}
