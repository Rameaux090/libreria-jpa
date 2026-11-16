package com.ejemplo.libreria.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ejemplo.libreria.Excepciones.LibroDuplicadoException;
import com.ejemplo.libreria.Excepciones.LibroNoEncontradoException;
import com.ejemplo.libreria.Model.Libros;
import com.ejemplo.libreria.RepoLibros.RepoLibros;

/**
 * Implementación del servicio de libros.
 *
 * <p>La dependencia con {@link RepoLibros} (un {@code JpaRepository}) se inyecta
 * por constructor, que es la forma recomendada en Spring (fácil de testear, sin
 * reflexión, y permite marcar el campo como final).</p>
 *
 * <p>El servicio es el único que conoce el repositorio: el controlador no
 * accede nunca a la capa de persistencia directamente.</p>
 */
@Service
public class ServicioLibro implements I_ServicioLibro {

    private final RepoLibros repo;

    public ServicioLibro(RepoLibros repo) {
        this.repo = repo;
    }

    @Override
    public List<Libros> obtenerTodosLosLibros() {
        // JpaRepository.findAll() ya devuelve la lista completa.
        return repo.findAll();
    }

    @Override
    public Optional<Libros> BuscarPorID(Long idLibro) {
        return repo.findById(idLibro);
    }

    @Override
    public Libros guardar(Libros libro) {
        // Si el cliente envió un id y ya existe, lo rechazamos (semántica POST = crear).
        if (libro.getIdLibro() != null && repo.existsById(libro.getIdLibro())) {
            throw new LibroDuplicadoException(libro.getIdLibro());
        }
        // save() inserta si id==null y actualiza si ya existe. Como aquí
        // garantizamos arriba que no existe, siempre será un INSERT.
        return repo.save(libro);
    }

    @Override
    public void eliminarPorId(Long idLibro) {
        if (!repo.existsById(idLibro)) {
            throw new LibroNoEncontradoException(idLibro);
        }
        repo.deleteById(idLibro);
    }

    @Override
    public List<Libros> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return List.of();
        }
        // Consulta derivada (LIKE '%titulo%' sin distinguir mayúsculas).
        return repo.findByTituloContainingIgnoreCase(titulo.trim());
    }
}
