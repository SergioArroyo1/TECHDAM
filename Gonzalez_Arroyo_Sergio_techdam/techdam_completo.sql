DROP DATABASE techdam_completo;
CREATE DATABASE IF NOT EXISTS techdam_completo;
USE techdam_completo;

CREATE TABLE empleados (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(50),
    salario DECIMAL(10,2),
    fecha_contratacion DATE,
    activo BOOLEAN DEFAULT TRUE
);

-- TABLA PROYECTOS
CREATE TABLE proyectos (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    presupuesto DECIMAL(12,2)
);

-- TABLA ASIGNACIONES
CREATE TABLE asignaciones (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado INT,
    id_proyecto INT,
    fecha_asignacion DATE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_proyecto) REFERENCES proyectos(id_proyecto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- DATOS DE PRUEBA
INSERT INTO empleados (nombre, departamento, salario, fecha_contratacion, activo) VALUES
('Ana Pérez', 'Desarrollo', 2500.00, '2020-03-01', TRUE),
('Luis Gómez', 'Marketing', 2000.00, '2019-07-15', TRUE),
('Marta López', 'Desarrollo', 2700.00, '2021-06-10', TRUE),
('Carlos Díaz', 'Soporte', 1800.00, '2018-02-20', TRUE),
('Sofía Ruiz', 'Desarrollo', 3000.00, '2017-11-05', FALSE);

INSERT INTO proyectos (nombre, fecha_inicio, fecha_fin, presupuesto) VALUES
('Sistema ERP', '2023-01-10', '2023-12-31', 50000.00),
('App Móvil', '2023-04-01', '2023-10-01', 30000.00),
('Campaña Publicitaria', '2023-06-01', '2023-09-30', 15000.00),
('Portal Web', '2022-02-15', '2022-12-30', 20000.00),
('CRM Interno', '2024-01-01', '2024-12-31', 45000.00);

INSERT INTO asignaciones (id_empleado, id_proyecto, fecha_asignacion) VALUES
(1, 1, '2023-02-01'),
(2, 3, '2023-06-05'),
(3, 2, '2023-04-10'),
(4, 4, '2022-03-01'),
(1, 5, '2024-01-15');


-- Actualizar el salario por departamento
DELIMITER $$
CREATE PROCEDURE actualizar_salario_departamento(
    IN p_departamento VARCHAR(50),
    IN p_porcentaje DECIMAL(5,2),
    OUT p_empleados_actualizados INT
)
BEGIN
    UPDATE empleados
    SET salario = salario * (1 + p_porcentaje / 100)
    WHERE departamento = p_departamento AND activo = TRUE;

    SET p_empleados_actualizados = ROW_COUNT();
END$$
DELIMITER ;

-- Segundo procedimiento: Obtener cantidad de empleados asignados a un proyecto
DELIMITER $$
CREATE PROCEDURE empleados_por_proyecto(
    IN p_id_proyecto INT,
    OUT p_total_empleados INT
)
BEGIN
    SELECT COUNT(*) 
    INTO p_total_empleados
    FROM asignaciones
    WHERE id_proyecto = p_id_proyecto;
END$$
DELIMITER ;
DELIMITER $$

CREATE FUNCTION salario_anual(p_id_empleado INT)
RETURNS DECIMAL(12,2)
DETERMINISTIC
BEGIN
    DECLARE v_salario DECIMAL(10,2);
    SELECT salario INTO v_salario FROM empleados WHERE id_empleado = p_id_empleado;
    RETURN v_salario * 12;
END$$
DELIMITER ;

SELECT p.id_proyecto, p.nombre AS proyecto, COUNT(a.id_empleado) AS empleados_asignados
FROM proyectos p
LEFT JOIN asignaciones a ON p.id_proyecto = a.id_proyecto
GROUP BY p.id_proyecto, p.nombre
ORDER BY p.id_proyecto;


-- Comprobar salario por departamento
SELECT 
    e.nombre,
    e.salario,
    e.departamento,
    (SELECT SUM(salario) FROM empleados WHERE departamento = e.departamento AND activo = TRUE) AS dinero_departamento
FROM 
    empleados e
WHERE 
    e.activo = TRUE
ORDER BY e.departamento, e.nombre;