package com.minh.file_service.exception;

import com.minh.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError error : allErrors) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        ResponseData errorResponse = new ResponseData(HttpStatus.BAD_REQUEST.value(), webRequest.getDescription(false), errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseData> handleGlobalException(Exception ex, WebRequest webRequest) {
        log.error(ex.getMessage(), ex);
        ResponseData errorResponse = new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value(), webRequest.getDescription(false), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }
}
