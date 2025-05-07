package com.three.recipingrecipeservicebe.global.exception;

import com.three.recipingrecipeservicebe.common.dto.ExceptionDto;
import com.three.recipingrecipeservicebe.global.exception.custom.ForbiddenException;
import com.three.recipingrecipeservicebe.global.exception.custom.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFoundException(final UserNotFoundException e) {
        log.error("UserNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> entityNotFoundException(final EntityNotFoundException e) {
        log.error("EntityNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDto> forbiddenException(final ForbiddenException e) {
        log.warn("ForbiddenException: ", e);
        return createResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionDto> fileUploadException(final FileUploadException e) {
        log.error("FileUploadException: ", e);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDto> exception(final Exception e) {
        log.error("Exception: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(
            final HttpStatus status,
            final String message
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ExceptionDto(status, message));
    }

}
