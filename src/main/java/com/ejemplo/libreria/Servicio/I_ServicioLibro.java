package com.ejemplo.libreria.Servicio;

import java.util.List;
import java.util.Optional;

import com.ejemplo.libreria.Model.Libros;

/**
 * Contrato del servicio de libros. Es la capa de reglas de negocio:
 * orquesta el repositorio y aplica validaciones de dominio.
 */
public interface I_ServicioLibro {

    /** Devuelve todos los libros. */
    List<Libros> obtenerTodosLosLibros();

    /** Busca un libro por id. Vacío si no existe. */
    Optional<Libros> BuscarPorID(Long idLibro);

    /** Crea un libro nuevo. Lanza excepción si el id ya existe. */
    Libros guardar(Libros libro);

    /** Elimina un libro por id. Lanza excepción si no existe. */
    void eliminarPorId(Long idLibro);

    /** Busca libros por título (parcial, sin distinguir mayúsculas). */
    List<Libros> buscarPorTitulo(String titulo);
}
