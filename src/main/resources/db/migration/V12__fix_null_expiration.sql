-- 1. A los ADMIN y PROFESORES, ponerles fecha muy lejana (año 2099)
UPDATE app_users 
SET expiration_date = '2099-12-31' 
WHERE (role = 'ADMIN' OR role = 'PROFESOR') AND expiration_date IS NULL;

-- 2. A los usuarios normales antiguos, darles 30 días de prueba desde hoy
-- (Así evitas que se encuentren la cuenta bloqueada de golpe)
UPDATE app_users 
SET expiration_date = DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY) 
WHERE expiration_date IS NULL;