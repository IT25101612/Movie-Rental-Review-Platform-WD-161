package com.example.movierental.controller;

import com.example.movierental.entity.Review;
import com.example.movierental.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) { this.reviewService = reviewService; }

    @PostMapping
    public ResponseEntity<Review> create(@Valid @RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(review));
    }
    @GetMapping
    public ResponseEntity<List<Review>> getAll() { return ResponseEntity.ok(reviewService.getAllReviews()); }
    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable Long id) { return ResponseEntity.ok(reviewService.getReviewById(id)); }
    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @Valid @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.updateReview(id, review));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { reviewService.deleteReview(id); return ResponseEntity.noContent().build(); }
}
