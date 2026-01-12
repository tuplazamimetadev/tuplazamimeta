-- ==================================================================================
-- MIGRACIÓN V4: TEMARIO COMPLETO POLICÍA LOCAL CASTILLA Y LEÓN (45 TEMAS)
-- ==================================================================================

-- 1. LIMPIEZA TOTAL
DELETE FROM app_materials;
DELETE FROM app_contents;

-- 2. REINICIO DE CONTADORES (Para que empiece en el ID 1)
ALTER TABLE app_contents AUTO_INCREMENT = 1;
ALTER TABLE app_materials AUTO_INCREMENT = 1;

-- ============================================================
-- GRUPO I: DERECHO CONSTITUCIONAL Y ADMINISTRATIVO
-- ============================================================

INSERT INTO app_contents (title, description, is_premium) VALUES 
('Tema 1: La Constitución Española de 1978', 'Estructura y contenido. Valores superiores. Título Preliminar.', false),
('Tema 2: Derechos y Deberes Fundamentales', 'Título I de la CE. Garantías y suspensión de derechos.', false),
('Tema 3: La Corona y Las Cortes Generales', 'Funciones del Rey. Congreso y Senado. Elaboración de las leyes.', true),
('Tema 4: El Gobierno y la Administración', 'Relaciones Gobierno-Cortes. Poder Judicial. Principios constitucionales.', true),
('Tema 5: Organización Territorial del Estado', 'Las Comunidades Autónomas. Estatutos de Autonomía. Distribución de competencias.', true),
('Tema 6: El Acto Administrativo', 'Concepto, elementos y clases. Motivación y notificación. Eficacia y validez.', true),
('Tema 7: El Procedimiento Administrativo Común', 'Ley 39/2015. Fases del procedimiento. Derechos de los interesados. Silencio administrativo.', true),
('Tema 8: El Personal al Servicio de la Administración', 'EBEP. Clases de personal. Derechos y deberes. Régimen disciplinario.', true);

-- ============================================================
-- GRUPO II: RÉGIMEN LOCAL Y AUTONÓMICO (CYL)
-- ============================================================

INSERT INTO app_contents (title, description, is_premium) VALUES 
('Tema 9: Estatuto de Autonomía de Castilla y León', 'Estructura y contenido. Instituciones propias: Cortes, Presidente y Junta.', false),
('Tema 10: El Municipio', 'Concepto y elementos. El término municipal. La población y el empadronamiento.', true),
('Tema 11: Organización Municipal', 'Alcalde, Tenientes de Alcalde, Pleno y Junta de Gobierno Local. Competencias.', true),
('Tema 12: Ordenanzas y Reglamentos', 'Potestad reglamentaria de las Entidades Locales. Procedimiento de elaboración. Bandos.', true),
('Tema 13: La Función Pública Local', 'Organización de la función pública. Selección y provisión de puestos de trabajo.', true),
('Tema 14: Haciendas Locales', 'Presupuesto municipal. Impuestos, tasas y precios públicos.', true);

-- ============================================================
-- GRUPO III: FUNCIÓN POLICIAL Y DERECHO PENAL
-- ============================================================

INSERT INTO app_contents (title, description, is_premium) VALUES 
('Tema 15: Ley Orgánica de Fuerzas y Cuerpos de Seguridad', 'Principios básicos de actuación. Disposiciones comunes y estatutarias.', false),
('Tema 16: La Policía Local', 'Funciones según la LO 2/1986. Colaboración con otras Fuerzas de Seguridad.', true),
('Tema 17: Ley de Coordinación de Policías Locales de CyL', 'Ley 9/2003. Normas Marco. Selección, promoción y movilidad.', true),
('Tema 18: La Policía Judicial', 'Funciones de la Policía Local como Policía Judicial. El atestado policial.', true),
('Tema 19: Derecho Penal (Parte General)', 'Concepto de delito. Dolo y culpa. Sujetos y objeto. Circunstancias modificativas.', true),
('Tema 20: El Homicidio y las Lesiones', 'Delitos contra la vida y la integridad física. Violencia doméstica y de género.', true),
('Tema 21: Delitos contra la Libertad', 'Detenciones ilegales, secuestros, amenazas y coacciones. Delitos contra la libertad sexual.', true),
('Tema 22: Delitos contra el Patrimonio', 'Hurto, robo, estafa, daños. Delitos contra el orden socioeconómico.', true),
('Tema 23: Delitos contra la Seguridad Colectiva', 'Incendios. Delitos contra la salud pública (tráfico de drogas).', true),
('Tema 24: Delitos contra la Administración Pública', 'Prevaricación, cohecho, malversación. Atentado y desobediencia a la autoridad.', true),
('Tema 25: Responsabilidad Penal de los Menores', 'Ley Orgánica 5/2000. Medidas y competencias de la Policía Local.', true);

-- ============================================================
-- GRUPO IV: TRÁFICO Y SEGURIDAD VIAL
-- ============================================================

INSERT INTO app_contents (title, description, is_premium) VALUES 
('Tema 26: Ley de Seguridad Vial', 'Texto Refundido (RDL 6/2015). Competencias. Infracciones y sanciones.', false),
('Tema 27: Reglamento General de Circulación', 'Normas generales de comportamiento. Circulación de vehículos y peatones.', true),
('Tema 28: Conductores y Vehículos', 'Permisos y licencias de conducción. El permiso por puntos. Documentación del vehículo.', true),
('Tema 29: Señalización', 'Normas generales. Prioridad entre señales. Semáforos y marcas viales.', true),
('Tema 30: Velocidad y Preferencia de Paso', 'Límites de velocidad. Supuestos de prioridad. Adelantamientos.', true),
('Tema 31: Parada y Estacionamiento', 'Normas generales. Lugares prohibidos. Retirada de vehículos (Grúa).', true),
('Tema 32: Alumbrado y Uso de la Vía', 'Uso obligatorio de alumbrado. Advertencias acústicas y ópticas.', true),
('Tema 33: Accidentes de Tráfico', 'Concepto y clases. Actuación policial en accidentes. Primeros auxilios.', true),
('Tema 34: Delitos contra la Seguridad Vial', 'Conducción bajo efectos de alcohol/drogas. Velocidad excesiva. Conducción temeraria.', true),
('Tema 35: Alcoholemia y Drogas', 'Procedimiento de detección. Tasas máximas. Inmovilización del vehículo.', true),
('Tema 36: Transporte', 'Transporte de viajeros y mercancías. Mercancías peligrosas (ADR).', true),
('Tema 37: ITV y Seguros', 'Inspección Técnica de Vehículos. Seguro Obligatorio (SOA).', true);

-- ============================================================
-- GRUPO V: SOCIEDAD Y MEDIO AMBIENTE
-- ============================================================

INSERT INTO app_contents (title, description, is_premium) VALUES 
('Tema 38: Policía Administrativa', 'Venta ambulante. Espectáculos públicos y actividades recreativas en CyL.', true),
('Tema 39: Medio Ambiente', 'Protección ambiental. Ruidos y vibraciones. Residuos urbanos.', true),
('Tema 40: Urbanismo', 'Infracciones urbanísticas. Obras en la vía pública. Competencias municipales.', true),
('Tema 41: Protección Civil', 'Concepto y organización. Planes de emergencia. Junta Local de Protección Civil.', true),
('Tema 42: Igualdad y Violencia de Género', 'Ley Orgánica 1/2004. Medidas de protección integral. Protocolos policiales.', true);


-- ============================================================
-- INSERCIÓN DE MATERIALES (EJEMPLOS ESTRATÉGICOS PARA RELLENAR)
-- ============================================================

-- Añadimos un PDF y un TEST a cada tema para que la app se vea completa
-- Usamos una subselect para asignar materiales a los IDs generados dinámicamente

-- Materiales Tema 1
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('CE 1978 Texto Consolidado', 'PDF', '/downloads/constitucion.pdf', 1),
('Test General Constitución (50 preg)', 'TEST', '/tests/constitucion-general', 1);

-- Materiales Tema 9 (Estatuto CyL)
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('Estatuto Autonomía CyL', 'PDF', '/downloads/estatuto-cyl.pdf', 9),
('Mapa Competencias Junta CyL', 'LINK', 'https://www.jcyl.es', 9);

-- Materiales Tema 15 (LOFCS)
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('Esquema Competencias Cuerpos', 'PDF', '/downloads/esquema-lofcs.pdf', 15),
('Video Explicativo LO 2/86', 'VIDEO', 'https://youtube.com', 15);

-- Materiales Tema 20 (Penal)
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('Código Penal (Libro II)', 'LINK', 'https://boe.es', 20),
('Casos Prácticos Homicidio', 'VIDEO', 'https://youtube.com', 20);

-- Materiales Tema 26 (Tráfico)
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('Ley Seguridad Vial 2026', 'PDF', '/downloads/lsv-2026.pdf', 26),
('Test Infracciones Tráfico', 'TEST', '/tests/trafico-infracciones', 26);

-- Materiales Tema 35 (Alcoholemia)
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('Protocolo Detección Alcohol', 'WORD', '/downloads/protocolo-alcohol.docx', 35),
('Diligencias Prevención', 'PDF', '/downloads/diligencias.pdf', 35);

-- Materiales Tema 42 (Violencia Género)
INSERT INTO app_materials (title, material_type, url, content_id) VALUES 
('Guía VioGén Policías', 'PDF', '/downloads/viogen.pdf', 42),
('Teléfonos de Interés', 'LINK', 'https://violenciagenero.igualdad.gob.es/', 42);