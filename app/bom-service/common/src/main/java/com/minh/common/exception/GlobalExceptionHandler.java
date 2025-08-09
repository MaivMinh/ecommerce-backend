package com.minh.common.exception;

import com.minh.common.constants.ErrorCode;
import com.minh.common.message.MessageCommon;
import com.minh.common.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.common.AxonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageCommon messageCommon;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    protected ResponseEntity<ResponseData> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseData(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseData> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(BAD_REQUEST).body(new ResponseData(BAD_REQUEST.value(), messageCommon.getMessage(ErrorCode.INVALID_PARAMS), errors));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ResponseData> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseData(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ResponseData> handleBusinessException(BusinessException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseData(ex.getMessage()));
    }


    @ExceptionHandler({Exception.class, RuntimeException.class, AxonException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ResponseData> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseData(
                        messageCommon.getMessage(ErrorCode.INTERNAL_SERVER_ERROR))
                );
    }

}
