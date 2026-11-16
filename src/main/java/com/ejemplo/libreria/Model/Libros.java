package com.ejemplo.libreria.Model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un libro en la librería.
 *
 * <p>Se persiste en la tabla {@code libros} de la base de datos H2 en memoria.</p>
 *
 * Anotaciones JPA:
 *  - @Entity    marca la clase como entidad persistente.
 *  - @Table     nombre físico de la tabla (opcional, por defecto el de la clase).
 *  - @Id        clave primaria.
 *  - @GeneratedValue   el id lo genera la BD (IDENTITY = autoincrement de H2).
 *  - @Column    metadatos de la columna (nullabilidad, longitud, unicidad).
 *
 * Anotaciones Bean Validation (jakarta.validation.constraints):
 *  - @NotBlank  el campo debe tener contenido (no null, no vacío, no solo espacios).
 *  - @NotNull   el campo no puede ser null.
 *  - @Size      longitud mínima/máxima del texto.
 *  - @PastOrPresent  la fecha no puede ser futura.
 *
 * Anotaciones Lombok:
 *  - @Data        genera getters, setters, toString, equals y hashCode.
 *  - @AllArgsConstructor  constructor con todos los campos.
 *  - @NoArgsConstructor  constructor sin argumentos (obligatorio en entidades JPA).
 */
@Entity
@Table(name = "libros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Libros implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long idLibro;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 2, max = 100, message = "El autor debe tener entre 2 y 100 caracteres")
    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 200, message = "El título debe tener entre 1 y 200 caracteres")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotNull(message = "La fecha de publicación es obligatoria")
    @PastOrPresent(message = "La fecha de publicación no puede ser futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate fechaPublicacion;

    @Override
    public Libros clone() {
        try {
            return (Libros) super.clone();
        } catch (CloneNotSupportedException e) {
            // Cloneable está implementado, esta excepción no debería ocurrir nunca.
            throw new AssertionError();
        }
    }
}
