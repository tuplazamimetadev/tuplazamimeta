-- 1. Tabla USUARIOS
CREATE TABLE app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'STUDENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla Padre: CONTENIDOS
CREATE TABLE app_contents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    is_premium BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla Hija: MATERIALES
CREATE TABLE app_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    material_type VARCHAR(50) NOT NULL,
    url VARCHAR(500),
    content_id BIGINT,
    CONSTRAINT fk_content FOREIGN KEY (content_id) REFERENCES app_contents(id)
);

-- DATOS DE PRUEBA
INSERT INTO app_users (name, email, password, role) VALUES ('Admin', 'admin@tuplazamimeta.com', 'admin', 'ADMIN');

-- Tema 1
INSERT INTO app_contents (title, description, is_premium) VALUES ('Tema 1: La Constitución Española', 'Norma suprema del ordenamiento jurídico.', false);

-- Materiales del Tema 1
INSERT INTO app_materials (title, material_type, url, content_id) VALUES ('Videoclase: La Corona', 'VIDEO', 'https://youtube.com/demo', 1);
INSERT INTO app_materials (title, material_type, url, content_id) VALUES ('Test de Nivel 1', 'TEST', '/tests/1', 1);
INSERT INTO app_materials (title, material_type, url, content_id) VALUES ('Esquema PDF', 'PDF', '/downloads/esquema.pdf', 1);
INSERT INTO app_materials (title, material_type, url, content_id) VALUES ('BOE Constitución', 'LINK', 'https://boe.es/constitucion', 1);

-- Tema 2
INSERT INTO app_contents (title, description, is_premium) VALUES ('Tema 2: Derechos y Deberes', 'Título I de la CE.', true);
INSERT INTO app_materials (title, material_type, url, content_id) VALUES ('Test Intensivo', 'TEST', '/tests/2', 2);