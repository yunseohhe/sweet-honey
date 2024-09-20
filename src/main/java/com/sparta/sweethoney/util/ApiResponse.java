package com.sparta.sweethoney.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private enum Status {
        SUCCESS, FAIL, ERROR
    }

    private final Status status;
    private final T data;
    private final String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Status.SUCCESS, data, "요청이 성공적으로 처리되었습니다");
    }

    public static ApiResponse<?> successWithNoContent() {
        return new ApiResponse<>(Status.SUCCESS, null, "요청이 성공적으로 처리되었지만 내용이 없습니다");
    }

    public static ApiResponse<?> fail(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }
        return new ApiResponse<>(Status.FAIL, errors, "검증에 실패했습니다");
    }

    public static ApiResponse<?> fail(Map<String, String> errors) {
        return new ApiResponse<>(Status.FAIL, errors, "검증에 실패했습니다");
    }

    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(Status.ERROR, null, message);
    }
}
