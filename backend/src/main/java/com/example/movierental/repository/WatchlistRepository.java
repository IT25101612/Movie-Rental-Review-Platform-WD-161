package com.example.movierental.repository;

import com.example.movierental.entity.Watchlist;
import com.example.movierental.storage.FileStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WatchlistRepository {

    private final FileStore<WatchlistRecord> store;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchlistRepository(
            @Value("${app.data.dir:./data}") String dataDir,
            ObjectMapper mapper,
            UserRepository userRepository,
            MovieRepository movieRepository) {
        this.store = new FileStore<>(Path.of(dataDir, "watchlist.json"), mapper, new TypeReference<>() {});
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public Watchlist save(Watchlist watchlist) {
        List<WatchlistRecord> all = store.readAll();
        WatchlistRecord rec = toRecord(watchlist);
        if (rec.id == null) {
            long nextId = all.stream().mapToLong(w -> w.id == null ? 0L : w.id).max().orElse(0L) + 1;
            rec.id = nextId;
            watchlist.setId(nextId);
            all.add(rec);
        } else {
            all.replaceAll(w -> w.id.equals(rec.id) ? rec : w);
        }
        store.writeAll(all);
        return watchlist;
    }

    public List<Watchlist> findAll() {
        return store.readAll().stream().map(this::toWatchlist).collect(Collectors.toList());
    }

    public Optional<Watchlist> findById(Long id) {
        return store.readAll().stream().filter(w -> w.id.equals(id)).map(this::toWatchlist).findFirst();
    }

    public boolean existsByUserIdAndMovieId(Long userId, Long movieId) {
        return store.readAll().stream()
                .anyMatch(w -> userId.equals(w.userId) && movieId.equals(w.movieId));
    }

    public void delete(Watchlist watchlist) {
        List<WatchlistRecord> all = store.readAll();
        all.removeIf(w -> w.id.equals(watchlist.getId()));
        store.writeAll(all);
    }

    private WatchlistRecord toRecord(Watchlist w) {
        WatchlistRecord rec = new WatchlistRecord();
        rec.id        = w.getId();
        rec.userId    = w.getUser()  != null ? w.getUser().getId()  : null;
        rec.movieId   = w.getMovie() != null ? w.getMovie().getId() : null;
        rec.addedDate = w.getAddedDate();
        rec.status    = w.getStatus();
        return rec;
    }

    private Watchlist toWatchlist(WatchlistRecord rec) {
        Watchlist w = new Watchlist();
        w.setId(rec.id);
        if (rec.userId  != null) userRepository.findById(rec.userId).ifPresent(w::setUser);
        if (rec.movieId != null) movieRepository.findById(rec.movieId).ifPresent(w::setMovie);
        w.setAddedDate(rec.addedDate);
        w.setStatus(rec.status);
        return w;
    }

    static class WatchlistRecord {
        public Long              id;
        public Long              userId;
        public Long              movieId;
        public LocalDateTime     addedDate;
        public Watchlist.Status  status;
    }
}
