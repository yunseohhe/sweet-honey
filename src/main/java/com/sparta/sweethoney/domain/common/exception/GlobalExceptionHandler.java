package com.sparta.sweethoney.domain.common.exception;

import com.sparta.sweethoney.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparta.sweethoney.util.ApiResponse.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. `GlobalException`을 상속받은 예외 클래스를 정의한다. -> super 생성자로 `GlobalExceptionConst`를 넣어준다.
     * 2. `GlobalExceptionConst`는 `ENUM`으로 상태코드, 메시지를 필드로 가진다.
     * 3. 새로 정의한 예외 클래스에 super 생성자로 새로 정의한 `GlobalExceptionConst`를 넣어준다.
     * 4. `GlobalExceptionHandler`는 `GlobalException`을 받아서 상태코드, 메시지를 `ErrorResult`에 넣어서 반환한다.
     *
     * @param GlobalException e
     * @return ResponseEntity<ErrorResult>
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<?>> handlerGlobalException(GlobalException e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()), e.getHttpStatus());
    }
}
