package service;

import config.DatabaseConfigPool;

import java.sql.*;
import java.util.Scanner;

/*
 Clase que encapsula llamadas a procedimientos almacenados (stored procedures)
 de la base de datos. Usa CallableStatement para llamar a procedimientos con
 parámetros IN y OUT.
*/
public class ProcedimientosService {

    // Metodo para actualizar salario por departamento (actualizar_salario_departamento)
    public void actualizarSalarioDepartamento() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el nombre del departamento: ");
        String departamento = scanner.nextLine();

        System.out.print("Introduce el porcentaje de incremento (Ejemplo: 5%): ");
        double porcentaje = Double.parseDouble(scanner.nextLine());

        // {CALL nombre_procedimiento(?, ?, ?)} -> sintaxis para llamar a procedimientos
        String sql = "{CALL actualizar_salario_departamento(?, ?, ?)}";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             CallableStatement cs = conexion.prepareCall(sql)) {

            cs.setString(1, departamento);  // IN (departamento)
            cs.setDouble(2, porcentaje);    // IN (porcentaje)
            cs.registerOutParameter(3, Types.INTEGER); // OUT (empleados actualizados)

            cs.execute();

            int empleadosActualizados = cs.getInt(3); // leer parámetro OUT
            System.out.println("Empleados actualizados: " + empleadosActualizados);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo para obtener cantidad de empleados asignados a un proyecto (empleados_por_proyecto)
    public void empleadosPorProyecto() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el ID del proyecto: ");
        int idProyecto = scanner.nextInt();
        scanner.nextLine();

        String sql = "{CALL empleados_por_proyecto(?, ?)}";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             CallableStatement cs = conexion.prepareCall(sql)) {

            cs.setInt(1, idProyecto);        // IN
            cs.registerOutParameter(2, Types.INTEGER);  // OUT

            cs.execute();

            int totalEmpleados = cs.getInt(2); // leer OUT
            System.out.println("Total empleados asignados al proyecto: " + totalEmpleados);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}