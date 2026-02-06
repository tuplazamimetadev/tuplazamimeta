CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    statement TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL, -- 'A', 'B', 'C', 'D'
    explanation TEXT,
    category VARCHAR(100) DEFAULT 'GENERAL' -- 'BLOQUE_A', 'BLOQUE_B', 'GENERAL'
);

-- Insertamos algunas preguntas de ejemplo para que no dé error al probar
INSERT INTO questions (statement, option_a, option_b, option_c, option_d, correct_option, explanation, category) VALUES 
('¿Cuál es la capital de España?', 'Barcelona', 'Madrid', 'Sevilla', 'Valencia', 'B', 'Madrid es capital desde 1561.', 'GENERAL'),
('¿Qué artículo de la CE habla del derecho a la vida?', 'Art 15', 'Art 14', 'Art 17', 'Art 24', 'A', 'El artículo 15 CE reconoce el derecho a la vida.', 'BLOQUE_A'),
('¿Quién es el padre de la sociología?', 'Weber', 'Durkheim', 'Comte', 'Marx', 'C', 'Auguste Comte acuñó el término.', 'BLOQUE_B');