package service;


import config.DatabaseConfigPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.math.BigDecimal;
import java.util.List;

/*

 - transferirPresupuesto: usa una transacción para restar y sumar presupuesto entre proyectos.

 - asignarEmpleadosConSavepoint: inserta varias asignaciones y usa savepoints
   para poder deshacer solo la inserción que falló sin perder las anteriores.
*/
public class TransaccionesService {

    // Opción A: Transferencia de Presupuesto entre proyectos
    public boolean transferirPresupuesto(int proyectoOrigenId, int proyectoDestinoId, BigDecimal monto) {
        Connection conexion = null;
        try {
            conexion = DatabaseConfigPool.getConexion();
            conexion.setAutoCommit(false); // Iniciamos una transacción manual

            // 1. Restar de proyecto origen
            String sqlRestar = "UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id_proyecto = ?";
            try (PreparedStatement psRestar = conexion.prepareStatement(sqlRestar)) {
                psRestar.setBigDecimal(1, monto);
                psRestar.setInt(2, proyectoOrigenId);
                psRestar.executeUpdate();
            }

            // 2. Sumar al proyecto destino
            String sqlSumar = "UPDATE proyectos SET presupuesto = presupuesto + ? WHERE id_proyecto = ?";
            try (PreparedStatement psSumar = conexion.prepareStatement(sqlSumar)) {
                psSumar.setBigDecimal(1, monto);
                psSumar.setInt(2, proyectoDestinoId);
                psSumar.executeUpdate();
            }

            conexion.commit(); // Confirmamos la transacción si todo fue bien
            return true;
        } catch (SQLException e) {
            // Si hay error hacemos rollback para no dejar datos inconsistentes
            if (conexion != null) {
                try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Cerramos la conexión (si no se usa pool con cierre automático)
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Opción B: Asignación múltiple con Savepoint
    public void asignarEmpleadosConSavepoint(int proyectoId, List<Integer> empleadoIds) {
        Connection conexion = null;
        try {
            conexion = DatabaseConfigPool.getConexion();
            conexion.setAutoCommit(false); // Modo transaccional manual
            for (int empId : empleadoIds) {
                // Creamos un savepoint antes de cada inserción para poder retroceder solo esa inserción
                Savepoint sp = conexion.setSavepoint("SP" + empId);
                try {
                    // Inserción directa en asignaciones (suponiendo que la tabla existe)
                    String sql = "INSERT INTO asignaciones (id_empleado, id_proyecto, fecha_asignacion) VALUES (?, ?, CURDATE())";
                    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                        ps.setInt(1, empId);
                        ps.setInt(2, proyectoId);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    // Si falla una inserción, hacemos rollback hasta el savepoint para deshacer solo esa inserción
                    conexion.rollback(sp); // Rollback parcial
                    // No rompemos el bucle: seguimos intentando con los demás empleados
                }
            }
            conexion.commit(); // Confirmamos todas las inserciones exitosas
        } catch (SQLException e) {
            // Si ocurre un error general, deshacemos todo
            if (conexion != null) {
                try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try { conexion.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}