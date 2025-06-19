package com.three.recipingrecipeservicebe.global.exception;

import com.three.recipingrecipeservicebe.common.dto.ExceptionDto;
import com.three.recipingrecipeservicebe.global.exception.custom.ForbiddenException;
import com.three.recipingrecipeservicebe.global.exception.custom.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFoundException(final UserNotFoundException e) {
        errorLogger.error("UserNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> entityNotFoundException(final EntityNotFoundException e) {
        errorLogger.error("EntityNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDto> forbiddenException(final ForbiddenException e) {
        errorLogger.warn("ForbiddenException: ", e);
        return createResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionDto> fileUploadException(final FileUploadException e) {
        errorLogger.error("FileUploadException: ", e);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> handleAccessDenied(AccessDeniedException e) {
        errorLogger.error("Access denied: ", e);
        return createResponse(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        errorLogger.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(
            final HttpStatus status,
            final String message
    ) {
        return ResponseEntity
                .status(status)
                .body(new ExceptionDto(status, message));
    }

}
