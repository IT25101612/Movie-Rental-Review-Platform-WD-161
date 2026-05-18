package com.example.movierental.service;
import com.example.movierental.entity.Watchlist;
import java.util.List;

public interface WatchlistService {
    Watchlist createWatchlist(Watchlist watchlist);
    List<Watchlist> getAllWatchlists();
    Watchlist getWatchlistById(Long id);
    Watchlist updateWatchlist(Long id, Watchlist watchlist);
    void deleteWatchlist(Long id);
}
