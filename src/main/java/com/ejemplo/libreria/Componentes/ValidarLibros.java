package com.ejemplo.libreria.Componentes;

import org.springframework.stereotype.Component;

import com.ejemplo.libreria.Model.Libros;

/**
 * Componente de validación de negocio para libros.
 *
 * <p>Es un bean {@code @Component} (sin estado propio): los métodos reciben
 * el libro a validar como parámetro en vez de almacenarlo en un campo,
 * para evitar el {@code NullPointerException} del código original y para
 * que el bean sea seguro bajo concurrencia.</p>
 */
@Component
public class ValidarLibros {

    /**
     * Comprueba que el libro tenga un título válido (no nulo, no vacío,
     * no solo espacios).
     */
    public boolean tituloValido(Libros libro) {
        if (libro == null || libro.getTitulo() == null) {
            return false;
        }
        return !libro.getTitulo().trim().isEmpty();
    }

    /** Comprueba que el autor sea válido. */
    public boolean autorValido(Libros libro) {
        if (libro == null || libro.getAutor() == null) {
            return false;
        }
        return !libro.getAutor().trim().isEmpty();
    }
}
