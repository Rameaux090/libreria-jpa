package com.ejemplo.libreria.Controlador;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests de integración del controlador REST usando MockMvc.
 *
 * <p>Levantan todo el contexto de Spring (incluido el repositorio en memoria
 * con sus 5 libros de ejemplo) y simulan peticiones HTTP sin abrir un puerto
 * real.</p>
 *
 * <p>Como el repositorio en memoria mantiene estado entre tests, marcamos la
 * clase con {@code @DirtiesContext} para que Spring recree el contexto antes
 * de cada clase de test y arranque siempre con los 5 libros de ejemplo.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ControladorLibreriaTest {

    @Autowired
    private MockMvc mockMvc;

    // ---------- GET /api/libros ----------

    @Test
    void listarTodos_devuelve200_yCincoLibros() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].titulo", is("El Quijote")));
    }

    @Test
    void listarTodos_filtrandoPorTitulo_devuelveSoloCoincidentes() throws Exception {
        mockMvc.perform(get("/api/libros").param("titulo", "quijote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("El Quijote")));
    }

    // ---------- GET /api/libros/{id} ----------

    @Test
    void obtenerPorId_existente_devuelve200() throws Exception {
        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLibro", is(1)))
                .andExpect(jsonPath("$.autor", is("Miguel de Cervantes")));
    }

    @Test
    void obtenerPorId_inexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/libros/999"))
                .andExpect(status().isNotFound());
    }

    // ---------- POST /api/libros ----------

    @Test
    void crear_libroValido_devuelve201_yLocation() throws Exception {
        String body = """
                {"autor":"Antoine de Saint-Exupéry","titulo":"El Principito","fechaPublicacion":"1943-04-06"}
                """;

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.titulo", is("El Principito")));
    }

    @Test
    void crear_libroSinTitulo_devuelve400_yListaErrores() throws Exception {
        // Sin "titulo" → @NotBlank falla.
        String body = """
                {"autor":"Autor Válido","fechaPublicacion":"2020-01-01"}
                """;

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", not(empty())));
    }

    @Test
    void crear_libroConFechaFutura_devuelve400() throws Exception {
        String body = """
                {"autor":"Autor","titulo":"Titulo","fechaPublicacion":"2999-01-01"}
                """;

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ---------- DELETE /api/libros/{id} ----------

    @Test
    void eliminar_existente_devuelve204() throws Exception {
        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_devuelve404() throws Exception {
        mockMvc.perform(delete("/api/libros/999"))
                .andExpect(status().isNotFound());
    }
}
