package br.com.teste.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        return createError(ex.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.toList());

        return createError("Um ou mais dos atributos estão inválidos", BAD_REQUEST, errors);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        return createError("Um ou mais dos atributos estão inválidos", BAD_REQUEST, errors);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorResponse handleNotFoundException(EntityNotFoundException ex) {
        return createError(ex.getMessage(), NOT_FOUND);
    }

    private ApiErrorResponse createError(String message, HttpStatus status) {
        return new ApiErrorResponse(message, status.name(), status.value(), LocalDateTime.now(), Collections.emptyList());
    }

    private ApiErrorResponse createError(String message, HttpStatus status, List<String> errors) {
        return new ApiErrorResponse(message, status.name(), status.value(), LocalDateTime.now(), errors);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record ApiErrorResponse(String message,
                                   String status,
                                   Integer code,
                                   LocalDateTime timestamp,
                                   List<String> errors) {
    }
}
