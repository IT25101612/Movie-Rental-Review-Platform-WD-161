package com.example.movierental.controller;

import com.example.movierental.entity.Watchlist;
import com.example.movierental.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    public WatchlistController(WatchlistService watchlistService) { this.watchlistService = watchlistService; }

    @PostMapping
    public ResponseEntity<Watchlist> create(@Valid @RequestBody Watchlist watchlist) {
        return ResponseEntity.status(HttpStatus.CREATED).body(watchlistService.createWatchlist(watchlist));
    }
    @GetMapping
    public ResponseEntity<List<Watchlist>> getAll() { return ResponseEntity.ok(watchlistService.getAllWatchlists()); }
    @GetMapping("/{id}")
    public ResponseEntity<Watchlist> getById(@PathVariable Long id) { return ResponseEntity.ok(watchlistService.getWatchlistById(id)); }
    @PutMapping("/{id}")
    public ResponseEntity<Watchlist> update(@PathVariable Long id, @Valid @RequestBody Watchlist watchlist) {
        return ResponseEntity.ok(watchlistService.updateWatchlist(id, watchlist));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { watchlistService.deleteWatchlist(id); return ResponseEntity.noContent().build(); }
}
