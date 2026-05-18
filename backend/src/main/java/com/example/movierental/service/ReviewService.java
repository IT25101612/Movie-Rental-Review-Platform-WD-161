package com.example.movierental.service;
import com.example.movierental.entity.Review;
import java.util.List;

public interface ReviewService {
    Review createReview(Review review);
    List<Review> getAllReviews();
    Review getReviewById(Long id);
    Review updateReview(Long id, Review review);
    void deleteReview(Long id);
}
