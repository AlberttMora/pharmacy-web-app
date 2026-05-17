-- ============================================================
-- NOTA: Con spring.jpa.hibernate.ddl-auto=update, Hibernate
-- crea las tablas automaticamente al iniciar la aplicacion.
-- Este script es solo de referencia o para crear la BD manualmente.
-- ============================================================

CREATE DATABASE IF NOT EXISTS farmacia
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE farmacia;

-- Las tablas son generadas por Hibernate segun las anotaciones @Entity.
-- Estructura resultante:

-- pacientes (id, nombre, apellidos, edad, sexo, motivo_consulta)
-- medicamentos (id, nombre, descripcion, edad_recomendada)
-- paciente_medicamento (paciente_id FK, medicamento_id FK)  <- tabla intermedia ManyToMany
