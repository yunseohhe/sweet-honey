package com.sparta.sweethoney.domain.review.repository;

import com.sparta.sweethoney.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.store.id = :storeId AND r.rating BETWEEN :minRating AND :maxRating ORDER BY r.createdAt DESC")
    List<Review> findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(
            @Param("storeId") Long storeId,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating
    );
}
