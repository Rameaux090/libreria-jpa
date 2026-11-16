package com.ejemplo.libreria.RepoLibros;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejemplo.libreria.Model.Libros;

/**
 * Repositorio JPA de libros.
 *
 * <p>Al extender {@link JpaRepository} heredamos gratis todas las operaciones
 * CRUD habituales ({@code save}, {@code findById}, {@code findAll},
 * {@code deleteById}, {@code existsById}...) sin escribir una sola línea
 * de implementación: Spring Data JPA las genera en tiempo de ejecución.</p>
 *
 * <p>Solo declaramos aquí las consultas derivadas que no vienen de serie,
 * como la búsqueda por título parcial usando {@code LIKE}.</p>
 */
@Repository
public interface RepoLibros extends JpaRepository<Libros, Long> {

    /**
     * Busca libros cuyo título contenga el fragmento indicado, sin distinguir
     * mayúsculas y minúsculas. Spring Data deriva la consulta del nombre del
     * método: {@code findBy + Titulo + ContainingIgnoreCase}.
     */
    List<Libros> findByTituloContainingIgnoreCase(String titulo);
}
