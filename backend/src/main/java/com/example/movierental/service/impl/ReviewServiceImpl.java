package com.example.movierental.service.impl;

import com.example.movierental.entity.Review;
import com.example.movierental.exception.ResourceNotFoundException;
import com.example.movierental.repository.ReviewRepository;
import com.example.movierental.service.ReviewService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    public ReviewServiceImpl(ReviewRepository reviewRepository) { this.reviewRepository = reviewRepository; }

    @Override public Review createReview(Review r) { return reviewRepository.save(r); }
    @Override public List<Review> getAllReviews() { return reviewRepository.findAll(); }
    @Override public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review","id",id));
    }
    @Override public Review updateReview(Long id, Review r) {
        Review e = getReviewById(id);
        e.setUser(r.getUser()); e.setMovie(r.getMovie());
        e.setRating(r.getRating()); e.setComment(r.getComment()); e.setReviewDate(r.getReviewDate());
        return reviewRepository.save(e);
    }
    @Override public void deleteReview(Long id) { reviewRepository.delete(getReviewById(id)); }
}
