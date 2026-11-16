package com.ejemplo.libreria.Controlador;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ejemplo.libreria.Model.Libros;
import com.ejemplo.libreria.Servicio.I_ServicioLibro;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST de la API de libros.
 *
 * <p>Buenas prácticas REST que se aplican aquí:</p>
 * <ul>
 *   <li>Verbo HTTP correcto para cada acción (GET, POST, DELETE).</li>
 *   <li>Rutas en plural, sustantivos, no verbos: {@code /api/libros}.</li>
 *   <li>{@code POST} devuelve 201 Created + cabecera Location con la URL del
 *       recurso creado (en vez de 200 OK con el cuerpo).</li>
 *   <li>{@code @Valid} activa la validación del cuerpo recibido.</li>
 *   <li>{@code DELETE} sobre un recurso inexistente devuelve 404.</li>
 *   <li>Búsqueda por query param ({@code ?titulo=...}) en vez de inventar
 *       una ruta especial para cada búsqueda.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "Operaciones CRUD sobre el catálogo de libros")
public class ControladorLibreria {

    private final I_ServicioLibro servicio;

    /** Inyección por constructor (recomendada en Spring). */
    public ControladorLibreria(I_ServicioLibro servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar todos los libros (opcional: filtrar por título)")
    @ApiResponse(responseCode = "200", description = "Listado de libros",
            content = @Content(schema = @Schema(implementation = Libros.class)))
    @GetMapping
    public ResponseEntity<List<Libros>> obtenerTodosLosLibros(
            @Parameter(description = "Filtro opcional por título (parcial, sin distinguir mayúsculas)")
            @RequestParam(value = "titulo", required = false) String titulo) {

        List<Libros> resultado = (titulo == null || titulo.isBlank())
                ? servicio.obtenerTodosLosLibros()
                : servicio.buscarPorTitulo(titulo);

        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Obtener un libro por id")
    @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(schema = @Schema(implementation = Libros.class)))
    @ApiResponse(responseCode = "404", description = "No existe el libro")
    @GetMapping("/{id}")
    public ResponseEntity<Libros> obtenerLibroPorId(
            @Parameter(description = "Identificador del libro") @PathVariable Long id) {
        return servicio.BuscarPorID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un libro nuevo")
    @ApiResponse(responseCode = "201", description = "Libro creado",
            content = @Content(schema = @Schema(implementation = Libros.class)))
    @ApiResponse(responseCode = "400", description = "Datos no válidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un libro con ese id")
    @PostMapping
    public ResponseEntity<Libros> agregarLibro(@Valid @RequestBody Libros libro) {
        Libros creado = servicio.guardar(libro);

        // Cabecera Location con la URL del nuevo recurso: estándar REST.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getIdLibro())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Eliminar un libro por id")
    @ApiResponse(responseCode = "204", description = "Libro eliminado")
    @ApiResponse(responseCode = "404", description = "No existe el libro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(
            @Parameter(description = "Identificador del libro") @PathVariable Long id) {
        servicio.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
