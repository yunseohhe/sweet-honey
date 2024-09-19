package com.sparta.sweethoney.domain.review.repository;

import com.sparta.sweethoney.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
