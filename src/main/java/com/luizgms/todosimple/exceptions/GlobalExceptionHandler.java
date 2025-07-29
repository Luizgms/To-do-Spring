package com.luizgms.todosimple.exceptions;

import java.io.IOException; // Mantido por conta do ServletException, mesmo que não seja usado diretamente no handler.
import java.util.Objects;

import jakarta.servlet.ServletException; // Mantido pois é uma exceção genérica de servlet. Pode ser removido se não houver outras necessidades.
import jakarta.servlet.http.HttpServletRequest; // Mantido pois pode ser usado para obter detalhes da requisição, mesmo sem security.
import jakarta.servlet.http.HttpServletResponse; // Mantido pois pode ser usado para escrever respostas personalizadas, mesmo sem security.
import jakarta.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.luizgms.todosimple.services.exceptions.DataBindingViolationException;
import com.luizgms.todosimple.services.exceptions.ObjectNotFoundException;

// REMOVIDO: import com.luizgms.todosimple.services.exceptions.AuthorizationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
// REMOVIDA A INTERFACE AuthenticationFailureHandler
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    // --- Tratamento de Erros de Validação (@Valid em DTOs) ---
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // Define o path e timestamp para a resposta de erro
        if (request instanceof ServletWebRequest) {
            errorResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        }
        errorResponse.setTimestamp(System.currentTimeMillis());

        log.error("Validation error for request [{}]: {}", errorResponse.getPath(), ex.getMessage());
        return ResponseEntity.unprocessableEntity().headers(headers).body(errorResponse);
    }

    // --- Tratamento de Erros Genéricos (Fallback) ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        final String errorMessage = "An unexpected error occurred.";
        log.error("Unhandled exception for request [{}]: {}", request.getDescription(false), exception.getMessage(),
                exception);
        return buildErrorResponse(
                exception,
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    // --- Tratamento de Erros de Integridade de Dados (Ex: Chave Duplicada) ---
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            WebRequest request) {
        String errorMessage = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : "Data integrity violation: Duplicate entry or foreign key constraint failed.";
        log.error("Data integrity violation for request [{}]: {}", request.getDescription(false), errorMessage, ex);
        return buildErrorResponse(
                ex,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }

    // --- Tratamento de Erros de Validação de Restrições (Ex: @Min, @Max em
    // parâmetros de controller) ---
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");

        ex.getConstraintViolations().forEach(violation -> {
            errorResponse.addValidationError(violation.getPropertyPath().toString(), violation.getMessage());
        });

        if (request instanceof ServletWebRequest) {
            errorResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        }
        errorResponse.setTimestamp(System.currentTimeMillis());

        log.error("Constraint violation for request [{}]: {}", errorResponse.getPath(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    // --- Tratamento de Exceções de Serviço Personalizadas ---

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException ex,
            WebRequest request) {
        log.warn("Object not found for request [{}]: {}", request.getDescription(false), ex.getMessage());
        return buildErrorResponse(
                ex,
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(DataBindingViolationException.class)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException ex,
            WebRequest request) {
        log.error("Data binding violation for request [{}]: {}", request.getDescription(false), ex.getMessage(), ex);
        return buildErrorResponse(
                ex,
                HttpStatus.CONFLICT,
                request);
    }

    // REMOVIDOS OS HANDLERS PARA EXCEÇÕES DE SEGURANÇA (AuthenticationException,
    // AccessDeniedException, AuthorizationException)
    // @ExceptionHandler(AuthenticationException.class) ...
    // @ExceptionHandler(AccessDeniedException.class) ...
    // @ExceptionHandler(AuthorizationException.class) ...

    // --- Métodos Auxiliares para Construção de Resposta de Erro ---

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }

        if (request instanceof ServletWebRequest) {
            errorResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        }
        errorResponse.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // REMOVIDO: Implementação do AuthenticationFailureHandler
    // @Override
    // public void onAuthenticationFailure(...) { ... }
}