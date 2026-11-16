package com.ejemplo.libreria.Error;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO estándar de error devuelto por la API cuando algo falla.
 * La serialización JSON omite los campos que sean null.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Momento del error (ISO-8601, UTC). */
    private Instant timestamp;

    /** Código HTTP. */
    private int status;

    /** Motivo HTTP estándar (Bad Request, Not Found...). */
    private String error;

    /** Mensaje legible para el cliente. */
    private String message;

    /** Ruta que se intentó invocar. */
    private String path;

    /** Lista de errores de validación (cuando aplique). */
    private List<ValidationError> validationErrors;

    /**
     * Error de validación concreto (campo + mensaje).
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
