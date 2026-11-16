package com.ejemplo.libreria.Excepciones;

/**
 * Se lanza cuando se intenta crear un libro con un id ya existente.
 * El {@link GlobalExceptionHandler} la traduce a una respuesta HTTP 409 (Conflict).
 */
public class LibroDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LibroDuplicadoException(Long id) {
        super("Ya existe un libro con id=" + id + ". Omita el id para que se asigne automáticamente.");
    }
}
