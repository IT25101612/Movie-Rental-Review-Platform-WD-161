package com.example.movierental.service.impl;

import com.example.movierental.entity.Watchlist;
import com.example.movierental.exception.ResourceNotFoundException;
import com.example.movierental.repository.WatchlistRepository;
import com.example.movierental.service.WatchlistService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    private final WatchlistRepository watchlistRepository;
    public WatchlistServiceImpl(WatchlistRepository watchlistRepository) { this.watchlistRepository = watchlistRepository; }

    @Override public Watchlist createWatchlist(Watchlist w) {
        if (watchlistRepository.existsByUserIdAndMovieId(w.getUser().getId(), w.getMovie().getId()))
            throw new IllegalArgumentException("Movie already in watchlist for this user.");
        return watchlistRepository.save(w);
    }
    @Override public List<Watchlist> getAllWatchlists() { return watchlistRepository.findAll(); }
    @Override public Watchlist getWatchlistById(Long id) {
        return watchlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Watchlist","id",id));
    }
    @Override public Watchlist updateWatchlist(Long id, Watchlist w) {
        Watchlist e = getWatchlistById(id);
        e.setUser(w.getUser()); e.setMovie(w.getMovie());
        e.setStatus(w.getStatus()); e.setAddedDate(w.getAddedDate());
        return watchlistRepository.save(e);
    }
    @Override public void deleteWatchlist(Long id) { watchlistRepository.delete(getWatchlistById(id)); }
}
