package dao;

import config.DatabaseConfigPool;
import model.Empleado;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/*
 Clase que gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 para los empleados en la base de datos.
 Usa DatabaseConfigPool para obtener conexiones (pool de conexiones).
 Los métodos que piden datos al usuario usan Scanner para leer desde consola.
*/
public class EmpleadoDAO {

    // Método que pide por consola los datos de un empleado y llama a crear()
    public int InsertarEmpleado() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("== Crear nuevo empleado ==");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();

        System.out.print("Salario: ");
        BigDecimal salario = new BigDecimal(scanner.nextLine());

        System.out.print("Fecha de contratación (yyyy-MM-dd): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        System.out.print("¿Activo? (true/false): ");
        boolean activo = Boolean.parseBoolean(scanner.nextLine());

        // Creamos el objeto Empleado y llamamos al método crear()
        Empleado empleado = new Empleado(0, nombre, departamento, salario, fecha, activo);
        return crear(empleado);
    }

    // Inserta un empleado en la base de datos y devuelve el id generado
    public int crear(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, departamento, salario, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";
        // try-with-resources asegura que conexión y statement se cierren automáticamente
        try (Connection conexion= DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Uso de PreparedStatement para evitar inyección SQL y asignar parámetros
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDepartamento());
            ps.setBigDecimal(3, empleado.getSalario());
            ps.setDate(4, Date.valueOf(empleado.getFecha_contratacion()));
            ps.setBoolean(5, empleado.isActivo());
            ps.executeUpdate();

            // Obtener la clave generada por la BD (id_autoincrement)
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Para depuración: muestra el error en consola
        }
        return -1; // Indica fallo
    }

    // Devuelve una lista con todos los empleados
    public List<Empleado> obtenerEmpleados() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Recorremos el ResultSet y vamos creando objetos Empleado
            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("departamento"),
                        rs.getBigDecimal("salario"),
                        rs.getDate("fecha_contratacion").toLocalDate(),
                        rs.getBoolean("activo")
                );
                lista.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Busca un empleado por su id y lo devuelve dentro de un Optional
    public Optional<Empleado> obtenerPorId(int id) {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = new Empleado(
                            rs.getInt("id_empleado"),
                            rs.getString("nombre"),
                            rs.getString("departamento"),
                            rs.getBigDecimal("salario"),
                            rs.getDate("fecha_contratacion").toLocalDate(),
                            rs.getBoolean("activo")
                    );
                    return Optional.of(emp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Si no se encuentra o hay error
    }

    // Método que pide por consola los nuevos datos para un empleado y llama a actualizar()
    public boolean actualizarEmpleado() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el ID del empleado a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Empleado> optEmpleado = obtenerPorId(id);
        if (!optEmpleado.isPresent()) {
            System.out.println("No existe un empleado con ese ID.");
            return false;
        }
        Empleado empleadoActual = optEmpleado.get();

        // Pedimos los campos; si se dejan vacíos conservamos el valor actual
        System.out.print("Nuevo nombre (" + empleadoActual.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) nombre = empleadoActual.getNombre();

        System.out.print("Nuevo departamento (" + empleadoActual.getDepartamento() + "): ");
        String departamento = scanner.nextLine();
        if (departamento.isEmpty()) departamento = empleadoActual.getDepartamento();

        System.out.print("Nuevo salario (" + empleadoActual.getSalario() + "): ");
        String salarioStr = scanner.nextLine();
        BigDecimal salario = salarioStr.isEmpty() ? empleadoActual.getSalario() : new BigDecimal(salarioStr);

        System.out.print("Fecha de contratación (yyyy-MM-dd) (" + empleadoActual.getFecha_contratacion() + "): ");
        String fechaStr = scanner.nextLine();
        LocalDate fecha = fechaStr.isEmpty() ? empleadoActual.getFecha_contratacion() : LocalDate.parse(fechaStr);

        System.out.print("¿Activo? (true/false) (" + empleadoActual.isActivo() + "): ");
        String activoStr = scanner.nextLine();
        boolean activo = activoStr.isEmpty() ? empleadoActual.isActivo() : Boolean.parseBoolean(activoStr);

        Empleado empleadoActualizado = new Empleado(id, nombre, departamento, salario, fecha, activo);
        return actualizar(empleadoActualizado);
    }

    // Actualiza un empleado en la base de datos
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre=?, departamento=?, salario=?, fecha_contratacion=?, activo=? WHERE id_empleado=?";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDepartamento());
            ps.setBigDecimal(3, empleado.getSalario());
            ps.setDate(4, Date.valueOf(empleado.getFecha_contratacion()));
            ps.setBoolean(5, empleado.isActivo());
            ps.setInt(6, empleado.getIdEmpleado());
            return ps.executeUpdate() > 0; // devuelve true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Pide por consola el id y llama a eliminar(id)
    public boolean eliminarEmpleado() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el ID del empleado a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return eliminar(id);
    }

    // Elimina un empleado por id
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id_empleado=?";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; // true si se eliminó alguna fila
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}