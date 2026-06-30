-- users-service/sql/init_users.sql

-- 1. Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS pichangapp;
USE pichangapp;

-- 2. Crear la tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Los datos de prueba deben insertarse mediante scripts separados con variables de entorno.
