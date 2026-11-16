# API REST de Libros — `libreria`

Proyecto construido con **Spring Boot 3.4.0** y **Java 17**. Implementa una API
REST para gestionar un catálogo de libros, persistida en una **base de datos H2
en memoria** mediante **Spring Data JPA**.

## Características

- ✅ API REST con verbos HTTP correctos (GET / POST / DELETE).
- ✅ Arquitectura por capas: **Controlador → Servicio → Repositorio → Base de datos**.
- ✅ Persistencia real con **Spring Data JPA + Hibernate**, sin escribir una
  línea de SQL en el repositorio: `RepoLibros extends JpaRepository` y Spring
  genera la implementación en tiempo de ejecución.
- ✅ Base de datos **H2 en memoria embebida** (no requiere instalación externa);
  datos de ejemplo cargados desde `data.sql` al arrancar.
- ✅ **Consola web H2** para inspeccionar la base de datos en `/h2-console`.
- ✅ Inyección de dependencias por constructor (recomendada en Spring).
- ✅ Validación de datos con **Bean Validation** (`@NotBlank`, `@Size`, `@PastOrPresent`...).
- ✅ Manejo centralizado de excepciones (`@RestControllerAdvice`) con cuerpo de error JSON consistente.
- ✅ Respuestas REST correctas: `201 Created` + cabecera `Location` en el POST, `404` en not-found, `409` en duplicado.
- ✅ Documentación OpenAPI / Swagger UI autogenerada.
- ✅ Tests de integración con MockMvc cubriendo los casos principales.
- ✅ Página informativa en la raíz (`/`) en vez del Whitelabel Error.

## Requisitos

- **Java 17** o superior.
- **Maven 3.8+** (o usar el wrapper incluido `./mvnw`).
- **No requiere instalar ninguna base de datos**: H2 se ejecuta embebida dentro de la app.

## Cómo ejecutar

```bash
# Desde la raíz del proyecto:

./mvnw spring-boot:run
```

La aplicación arranca en `http://localhost:8099/`. Al arrancar:

1. Hibernate crea la tabla `libros` en la BD H2 en memoria.
2. Spring ejecuta `src/main/resources/data.sql` e inserta los 5 libros de ejemplo.
3. La API queda lista para recibir peticiones.

> ⚠️ Al ser una BD en memoria, los datos se pierden al parar la app. En el
> siguiente arranque se vuelven a crear desde `data.sql`.

## Endpoints

Base URL: `http://localhost:8099/api/libros`

| Método | Ruta                          | Descripción                                  |
|--------|-------------------------------|----------------------------------------------|
| GET    | `/api/libros`                 | Lista todos los libros.                      |
| GET    | `/api/libros?titulo=quijote`  | Filtra libros por título (parcial).           |
| GET    | `/api/libros/{id}`            | Devuelve un libro por id.                    |
| POST   | `/api/libros`                 | Crea un libro (JSON en el cuerpo).           |
| DELETE | `/api/libros/{id}`            | Elimina un libro por id.                     |

### Recursos adicionales

- Página principal: `http://localhost:8099/`
- Swagger UI: `http://localhost:8099/swagger-ui.html`
- Esquema OpenAPI: `http://localhost:8099/v3/api-docs`
- **Consola H2**: `http://localhost:8099/h2-console`
  - JDBC URL: `jdbc:h2:mem:libreriadb`
  - Usuario: `sa`
  - Contraseña: *(vacía)*

## Ejemplos de uso

```bash
# Listar todos los libros
curl http://localhost:8099/api/libros

# Filtrar por título
curl 'http://localhost:8099/api/libros?titulo=quijote'

# Obtener un libro por id
curl http://localhost:8099/api/libros/1

# Crear un libro nuevo
curl -X POST http://localhost:8099/api/libros \
  -H "Content-Type: application/json" \
  -d '{"autor":"Antoine de Saint-Exupéry","titulo":"El Principito","fechaPublicacion":"1943-04-06"}'

# Eliminar un libro
curl -X DELETE http://localhost:8099/api/libros/1
```

## Ejecutar los tests

```bash
./mvnw test
```

## Estructura del proyecto

```
src/main/java/com/ejemplo/libreria/
├── LibreriaApplication.java        ← punto de entrada (@SpringBootApplication)
├── Controlador/
│   └── ControladorLibreria.java    ← API REST
├── Servicio/
│   ├── I_ServicioLibro.java        ← interfaz del servicio
│   └── ServicioLibro.java          ← lógica de negocio
├── RepoLibros/
│   └── RepoLibros.java             ← JpaRepository (Spring Data JPA)
├── Model/
│   └── Libros.java                 ← entidad JPA (@Entity) + validaciones
├── Componentes/
│   └── ValidarLibros.java          ← utilidades de validación de negocio
├── Excepciones/
│   ├── LibroNoEncontradoException.java
│   └── LibroDuplicadoException.java
└── Error/
    ├── ErrorResponse.java          ← DTO estándar de error
    └── GlobalExceptionHandler.java ← traduce excepciones → HTTP

src/main/resources/
├── application.properties          ← configuración H2 + JPA + Swagger
├── data.sql                        ← datos de ejemplo (5 libros)
└── static/index.html               ← landing page en /
```

## Esquema de la base de datos

Tabla `libros` (generada automáticamente por Hibernate a partir de `Libros.java`):

| Columna            | Tipo         | Restricciones                          |
|--------------------|--------------|----------------------------------------|
| `id_libro`         | BIGINT       | PK, autoincrement (IDENTITY)           |
| `autor`            | VARCHAR(100) | NOT NULL                               |
| `titulo`           | VARCHAR(200) | NOT NULL                               |
| `fecha_publicacion`| DATE         | NOT NULL                               |

## Cómo migrar a PostgreSQL/MySQL

Solo hay que cambiar la configuración en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/libreria
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update   # en producción: validate o Flyway/Liquibase
```

Y añadir la dependencia del driver en el `pom.xml`. **El código Java no cambia en absoluto**.

## Próximos pasos sugeridos

- Añadir `PUT /api/libros/{id}` para actualizar libros.
- Añadir paginación y ordenación en el listado.
- Añadir autenticación con Spring Security + JWT.
- Migrar la inicialización de datos de `data.sql` a un `CommandLineRunner`.
- Desplegar en Docker.

## Tecnologías

- Java 17
- Spring Boot 3.4.0
- Spring Web MVC
- Spring Data JPA + Hibernate
- H2 Database (en memoria)
- Bean Validation (jakarta.validation)
- Lombok
- Springdoc OpenAPI (Swagger)
- JUnit 5 + MockMvc
