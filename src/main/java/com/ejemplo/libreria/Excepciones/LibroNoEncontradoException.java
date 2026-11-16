package com.ejemplo.libreria.Excepciones;

/**
 * Se lanza cuando se intenta acceder a un libro que no existe.
 * El {@link GlobalExceptionHandler} la traduce a una respuesta HTTP 404.
 */
public class LibroNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LibroNoEncontradoException(Long id) {
        super("No existe ningún libro con id=" + id);
    }
}
