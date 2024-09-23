package com.sparta.sweethoney.domain.common.exception.review;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.INVALID_RATING;


public class InvalidRatingException extends GlobalException {
  public InvalidRatingException() {
    super(INVALID_RATING);
  }
}
