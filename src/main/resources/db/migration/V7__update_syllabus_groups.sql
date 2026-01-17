-- ==================================================================================
-- MIGRACIÓN V7: REESTRUCTURACIÓN COMPLETA (GRUPO A y B - BOCYL 2025)
-- ==================================================================================

-- 1. DESACTIVAR CHEQUEO DE CLAVES FORÁNEAS TEMPORALMENTE (Para poder truncar)
SET FOREIGN_KEY_CHECKS = 0;

-- 2. LIMPIAR TABLAS (Opcional: Si quieres conservar los materiales subidos, NO ejecutes el TRUNCATE de materiales)
-- NOTA: Si borras contents, los materiales asociados quedarán huérfanos si no los borras también.
TRUNCATE TABLE app_materials; 
TRUNCATE TABLE app_contents;

-- 3. REACTIVAR CHEQUEO
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 4. INSERTAR GRUPO A (Temas 1-21)
-- ==========================================

INSERT INTO app_contents (id, title, description, is_premium) VALUES 
(1, 'A.01 - El sistema constitucional español', 'La Constitución Española de 1978. Estructura y contenido. Principios generales. La reforma. El Estado español como Estado Social y Democrático de Derecho.', false),
(2, 'A.02 - Derechos y deberes fundamentales', 'Derechos y Libertades. Garantías. Principios rectores de política social y económica. El Tribunal Constitucional. El Defensor del Pueblo.', false),
(3, 'A.03 - Organización política del Estado (I)', 'Clase y forma de Estado. La Corona. Las Cortes Generales: Estructura y competencias. Procedimiento de elaboración de las leyes.', true),
(4, 'A.04 - Organización política del Estado (II)', 'El poder ejecutivo. El Gobierno y la Administración. El Poder Judicial. Organización judicial española. El Ministerio Fiscal.', true),
(5, 'A.05 - Organización territorial del Estado', 'Las comunidades autónomas. Las administraciones locales.', true),
(6, 'A.06 - La Comunidad Autónoma de Castilla y León', 'Instituciones: las Cortes de Castilla y León, el Presidente y la Junta de Castilla y León. El Estatuto de Autonomía. Competencias.', true),
(7, 'A.07 - La administración pública', 'Derecho administrativo general. Concepto. Fuentes del derecho administrativo. La jerarquía normativa. Los reglamentos.', true),
(8, 'A.08 - El acto administrativo', 'Concepto, clases y elementos. Motivación y notificación. Eficacia y validez de los actos administrativos.', true),
(9, 'A.09 - El procedimiento administrativo', 'Principios informadores. Los interesados. Fases: iniciación, ordenación, instrucción y terminación. Dimensión temporal.', true),
(10, 'A.10 - Procedimientos especiales y recursos', 'Especialidades del procedimiento administrativo local. La revisión de los actos administrativos: de oficio. Recursos administrativos.', true),
(11, 'A.11 - La función pública en general', 'El Estatuto del empleado público. Adquisición y pérdida de la condición de funcionario. Derechos, deberes e incompatibilidades.', true),
(12, 'A.12 - Los funcionarios de las entidades locales', 'Organización de la función pública local. Los grupos de funcionarios de la Administración Especial y General.', true),
(13, 'A.13 - La responsabilidad de la Administración', 'Fundamentos y clases. La responsabilidad de los funcionarios.', true),
(14, 'A.14 - La Administración Local', 'Principios constitucionales. La provincia: concepto y competencias. El Municipio: concepto y elementos. Población y empadronamiento.', true),
(15, 'A.15 - La organización del municipio', 'El ayuntamiento. El alcalde. Los concejales. El pleno. La comisión de gobierno Local. Otros órganos administrativos.', true),
(16, 'A.16 - Funcionamiento y competencias municipales', 'Servicios públicos locales. Formas de gestión. Intervención administrativa (licencias). Ordenanzas, Reglamentos y Bandos.', true),
(17, 'A.17 - Las fuerzas y cuerpos de seguridad', 'Clases y competencias. Disposiciones estatuarias comunes. Función y deontología policial. Principios básicos de actuación.', true),
(18, 'A.18 - Los cuerpos de policía local (I)', 'Participación en la seguridad pública. Funciones: administrativa, de seguridad y judicial. Coordinación. Juntas locales.', true),
(19, 'A.19 - Los cuerpos de policía local (II)', 'Organización y estructura. Estatuto personal: La Ley 9/2003 de coordinación de policías locales de CyL y su desarrollo.', true),
(20, 'A.20 - Los cuerpos de policía local (III)', 'Derechos y deberes. Sistema de responsabilidad, penal civil y administrativa. Régimen disciplinario.', true),
(21, 'A.21 - Prevención de Riesgos Laborales', 'Concepto general de trabajo, salud y riesgos. Marco normativo básico: La Ley 31/1995.', true);

-- ==========================================
-- 5. INSERTAR GRUPO B (Temas 1-23 -> IDs 22-44)
-- ==========================================

INSERT INTO app_contents (id, title, description, is_premium) VALUES 
(22, 'B.01 - El procedimiento penal', 'Principios que lo rigen y clases. Clases y competencias de los Juzgados y tribunales.', true),
(23, 'B.02 - La Policía Judicial', 'Concepto y funciones. El atestado policial en la Ley de Enjuiciamiento Criminal. Concepto y estructura.', true),
(24, 'B.03 - La Detención', 'Concepto, clases, supuestos y plazos. Obligaciones del funcionario. Habeas Corpus. Derechos del detenido.', true),
(25, 'B.04 - El Código Penal', 'Las infracciones penales. Personas responsables. Circunstancias modificativas. Penas y medidas de seguridad. Responsabilidad civil.', true),
(26, 'B.05 - El homicidio y sus formas', 'Las lesiones. La violencia de género y doméstica.', true),
(27, 'B.06 - Delitos contra la libertad', 'Las torturas y otros delitos contra la integridad moral.', true),
(28, 'B.07 - Delitos contra la libertad sexual', 'Delitos contra la libertad e indemnidad sexuales.', true),
(29, 'B.08 - Delitos contra la intimidad', 'Delitos contra la intimidad, el derecho a la propia imagen y la inviolabilidad.', true),
(30, 'B.09 - Delitos contra el patrimonio', 'Delitos contra el patrimonio y el orden socioeconómico. Las falsedades documentales.', true),
(31, 'B.10 - Delitos contra la seguridad colectiva', 'Incendios. Salud pública. Seguridad vial. La omisión del deber de socorro.', true),
(32, 'B.11 - Delitos de funcionarios', 'Delitos cometidos por funcionarios contra garantías constitucionales y contra la Administración Pública.', true),
(33, 'B.12 - Delitos contra el orden público', 'Tipología y características principales.', true),
(34, 'B.13 - Los delitos de imprudencia', 'Con especial relación a los cometidos con vehículos de motor.', true),
(35, 'B.14 - Seguridad ciudadana', 'La protección de la seguridad ciudadana: Ley Orgánica 4/2015, de 30 de marzo.', true),
(36, 'B.15 - La protección civil', 'En España y en Castilla y León. Normas reguladoras.', true),
(37, 'B.16 - El Reglamento de armas', 'Tipos de armas; armas prohibidas; tipos de licencias; guías de armas.', true),
(38, 'B.17 - Espectáculos públicos', 'Normativa autonómica en materia de espectáculos públicos y actividades recreativas.', true),
(39, 'B.18 - Drogodependientes', 'Normativa autonómica reguladora de la prevención, asistencia e integración social.', true),
(40, 'B.19 - Animales', 'Normativa tenencia animales potencialmente peligrosos y protección de animales de compañía.', true),
(41, 'B.20 - Tráfico y seguridad vial', 'Normativa básica. Conceptos básicos. Competencias de las Administraciones del Estado y Municipios.', true),
(42, 'B.21 - Normas generales de circulación', 'Velocidad, prioridad, cambios de dirección, adelantamientos, paradas y estacionamientos. Alumbrado.', true),
(43, 'B.22 - Autorizaciones administrativas', 'Autorizaciones para conducir. Autorizaciones relativas a los vehículos.', true),
(44, 'B.23 - Infracciones y sanciones', 'Medidas cautelares. Procedimiento sancionador en materia de tráfico.', true);

-- ==========================================
-- 6. TEMA ESPECIAL PARA TESTS (ID 99)
-- ==========================================

INSERT INTO app_contents (id, title, description, is_premium) VALUES 
(99, 'Z. PONTE A PRUEBA - SIMULACROS Y TESTS GENERALES', 'Zona exclusiva para realizar simulacros de examen y tests generales acumulativos.', true);