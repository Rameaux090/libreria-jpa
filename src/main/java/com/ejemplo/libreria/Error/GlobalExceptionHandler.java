package com.ejemplo.libreria.Error;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ejemplo.libreria.Excepciones.LibroDuplicadoException;
import com.ejemplo.libreria.Excepciones.LibroNoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador central de excepciones para toda la API.
 *
 * <p>Captura las excepciones lanzadas desde el servicio y las traduce a una
 * respuesta HTTP con un cuerpo JSON consistente ({@link ErrorResponse}),
 * de modo que el cliente siempre reciba el mismo formato de error.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404 - libro no encontrado. */
    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrado(
            LibroNoEncontradoException ex, HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /** 409 - libro duplicado. */
    @ExceptionHandler(LibroDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleDuplicado(
            LibroDuplicadoException ex, HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /** 400 - errores de validación de Bean Validation. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorResponse.ValidationError> errores = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fe -> new ErrorResponse.ValidationError(
                        fe.getField(),
                        fe.getDefaultMessage()))
                .toList();

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Los datos enviados no son válidos",
                request.getRequestURI(),
                errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** 500 - cualquier otra excepción no prevista. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerica(
            Exception ex, WebRequest request, HttpServletRequest httpRequest) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Error interno del servidor",
                httpRequest.getRequestURI(),
                null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
