package com.sparta.sweethoney.exception;

import com.sparta.sweethoney.exception.orderexception.NoOrdersFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * `GlobalException`을 상속받은 예외 클래스를 정의한다. -> super 생성자로 `GlobalExceptionConst`를 넣어준다.
     * `GlobalExceptionConst` -> 상태코드, 메시지를 필드로 가진다.
     * `GlobalException`은 하위 생성자에서 넘어온 `GlobalExceptionConst`에서 상태코드, 메시지를 넣어준다.
     * <p>
     * `GlobalExceptionHandler`은 `GlobalException`을 받아서 상태코드, 메시지를 `ErrorResult`에 넣어서 반환한다.
     *
     * @param GlobalException e
     * @return ResponseEntity<ErrorResult>
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResult> handlerGlobalException(GlobalException e) {
        return new ResponseEntity<>(new ErrorResult(e.getMessage()), e.getHttpStatus());
    }
}
