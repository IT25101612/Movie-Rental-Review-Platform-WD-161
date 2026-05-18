package com.example.movierental.repository;

import com.example.movierental.entity.Movie;
import com.example.movierental.storage.FileStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MovieRepository {

    private final FileStore<Movie> store;

    public MovieRepository(@Value("${app.data.dir:./data}") String dataDir, ObjectMapper mapper) {
        this.store = new FileStore<>(Path.of(dataDir, "movies.json"), mapper, new TypeReference<>() {});
    }

    public Movie save(Movie movie) {
        List<Movie> all = store.readAll();
        if (movie.getId() == null) {
            long nextId = all.stream().mapToLong(m -> m.getId() == null ? 0L : m.getId()).max().orElse(0L) + 1;
            movie.setId(nextId);
            all.add(movie);
        } else {
            all.replaceAll(m -> m.getId().equals(movie.getId()) ? movie : m);
        }
        store.writeAll(all);
        return movie;
    }

    public List<Movie> findAll() {
        return store.readAll();
    }

    public Optional<Movie> findById(Long id) {
        return store.readAll().stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public List<Movie> findByTitleContainingIgnoreCase(String title) {
        String lower = title.toLowerCase();
        return store.readAll().stream()
                .filter(m -> m.getTitle() != null && m.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public void delete(Movie movie) {
        List<Movie> all = store.readAll();
        all.removeIf(m -> m.getId().equals(movie.getId()));
        store.writeAll(all);
    }
}
