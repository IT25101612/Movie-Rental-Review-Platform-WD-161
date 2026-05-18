package com.example.movierental.repository;

import com.example.movierental.entity.Review;
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
public class ReviewRepository {

    private final FileStore<ReviewRecord> store;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewRepository(
            @Value("${app.data.dir:./data}") String dataDir,
            ObjectMapper mapper,
            UserRepository userRepository,
            MovieRepository movieRepository) {
        this.store = new FileStore<>(Path.of(dataDir, "reviews.json"), mapper, new TypeReference<>() {});
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public Review save(Review review) {
        List<ReviewRecord> all = store.readAll();
        ReviewRecord rec = toRecord(review);
        if (rec.id == null) {
            long nextId = all.stream().mapToLong(r -> r.id == null ? 0L : r.id).max().orElse(0L) + 1;
            rec.id = nextId;
            review.setId(nextId);
            all.add(rec);
        } else {
            all.replaceAll(r -> r.id.equals(rec.id) ? rec : r);
        }
        store.writeAll(all);
        return review;
    }

    public List<Review> findAll() {
        return store.readAll().stream().map(this::toReview).collect(Collectors.toList());
    }

    public Optional<Review> findById(Long id) {
        return store.readAll().stream().filter(r -> r.id.equals(id)).map(this::toReview).findFirst();
    }

    public void delete(Review review) {
        List<ReviewRecord> all = store.readAll();
        all.removeIf(r -> r.id.equals(review.getId()));
        store.writeAll(all);
    }

    private ReviewRecord toRecord(Review r) {
        ReviewRecord rec = new ReviewRecord();
        rec.id         = r.getId();
        rec.userId     = r.getUser()  != null ? r.getUser().getId()  : null;
        rec.movieId    = r.getMovie() != null ? r.getMovie().getId() : null;
        rec.rating     = r.getRating();
        rec.comment    = r.getComment();
        rec.reviewDate = r.getReviewDate();
        return rec;
    }

    private Review toReview(ReviewRecord rec) {
        Review r = new Review();
        r.setId(rec.id);
        if (rec.userId  != null) userRepository.findById(rec.userId).ifPresent(r::setUser);
        if (rec.movieId != null) movieRepository.findById(rec.movieId).ifPresent(r::setMovie);
        r.setRating(rec.rating);
        r.setComment(rec.comment);
        r.setReviewDate(rec.reviewDate);
        return r;
    }

    static class ReviewRecord {
        public Long          id;
        public Long          userId;
        public Long          movieId;
        public Integer       rating;
        public String        comment;
        public LocalDateTime reviewDate;
    }
}
