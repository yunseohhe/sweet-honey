package com.sparta.sweethoney.domain.review.controller;

import com.sparta.sweethoney.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    public final ReviewService reviewService;
}
