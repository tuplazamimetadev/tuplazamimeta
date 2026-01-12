-- Actualizamos la contraseña del ALUMNO
-- La contraseña será: "1234"
UPDATE app_users 
SET password = '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.My7GCIcHeeU.9.6e9.2eF9.7.W' 
WHERE email = 'student@tuplazamimeta.com';

-- Actualizamos la contraseña del ADMIN
-- La contraseña será: "admin"
UPDATE app_users 
SET password = '$2a$10$gqHrslMttQWSsDSVRTKS2Oq.wB.7/..A..eC..eC.r.e.C.r.e.C' 
WHERE email = 'admin@tuplazamimeta.com';
