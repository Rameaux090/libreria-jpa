-- Datos iniciales del catálogo de libros.
-- Se ejecutan automáticamente por Spring Boot al arrancar la aplicación,
-- DESPUÉS de que Hibernate cree el esquema (gracias a
-- spring.jpa.defer-datasource-initialization=true en application.properties).
--
-- Como la PK (id_libro) es IDENTITY (autoincrement), la omitimos: la BD
-- asigna los ids 1, 2, 3, 4, 5 por orden de inserción.

INSERT INTO libros (autor, titulo, fecha_publicacion) VALUES
  ('Miguel de Cervantes',      'El Quijote',                              DATE '1605-01-16'),
  ('Gabriel García Márquez',    'Cien Años de Soledad',                    DATE '1967-05-30'),
  ('J.K. Rowling',             'Harry Potter y la Piedra Filosofal',      DATE '1997-06-26'),
  ('George Orwell',             '1984',                                    DATE '1949-06-08'),
  ('F. Scott Fitzgerald',       'El Gran Gatsby',                          DATE '1925-04-10');
