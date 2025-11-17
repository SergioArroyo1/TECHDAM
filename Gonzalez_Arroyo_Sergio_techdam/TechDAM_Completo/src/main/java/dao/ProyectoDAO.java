package dao;
import config.DatabaseConfigPool;
import model.Proyecto;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/*
 DAO para la entidad Proyecto. Contiene métodos CRUD y métodos que leen
 datos desde consola para crear/actualizar proyectos.
*/
public class ProyectoDAO {

    // Pide por consola los datos de un proyecto y llama a crear()
    public int InsertarProyecto() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("== Crear nuevo proyecto ==");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Fecha inicio (yyyy-MM-dd): ");
        LocalDate inicio = LocalDate.parse(scanner.nextLine());

        System.out.print("Fecha fin (yyyy-MM-dd): ");
        LocalDate fin = LocalDate.parse(scanner.nextLine());

        System.out.print("Presupuesto: ");
        BigDecimal presupuesto = new BigDecimal(scanner.nextLine());

        Proyecto proyecto = new Proyecto(0, nombre, inicio, fin, presupuesto);
        return crear(proyecto);
    }

    // Inserta proyecto en la BD y devuelve el id generado
    public int crear(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (nombre, fecha_inicio, fecha_fin, presupuesto) VALUES (?, ?, ?, ?)";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proyecto.getNombre());
            ps.setDate(2, Date.valueOf(proyecto.getFecha_inicio()));
            ps.setDate(3, Date.valueOf(proyecto.getFecha_fin()));
            ps.setBigDecimal(4, proyecto.getPresupuesto());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Devuelve todos los proyectos
    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proyecto proy = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getBigDecimal("presupuesto")
                );
                lista.add(proy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Busca un proyecto por id y lo devuelve como Optional
    public Optional<Proyecto> obtenerPorId(int id) {
        String sql = "SELECT * FROM proyectos WHERE id_proyecto = ?";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proyecto proyecto = new Proyecto(
                            rs.getInt("id_proyecto"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_fin").toLocalDate(),
                            rs.getBigDecimal("presupuesto")
                    );
                    return Optional.of(proyecto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Pide por consola los nuevos datos de un proyecto y llama a actualizar()
    public boolean actualizarProyecto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el ID del proyecto a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Proyecto> optProyecto = obtenerPorId(id);
        if (!optProyecto.isPresent()) {
            System.out.println("No existe un proyecto con ese ID.");
            return false;
        }
        Proyecto proyectoActual = optProyecto.get();

        // Si el usuario deja vacío un campo, se mantiene el valor actual
        System.out.print("Nuevo nombre (" + proyectoActual.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) nombre = proyectoActual.getNombre();

        System.out.print("Nueva fecha inicio (yyyy-MM-dd) (" + proyectoActual.getFecha_inicio() + "): ");
        String inicioStr = scanner.nextLine();
        LocalDate inicio = inicioStr.isEmpty() ? proyectoActual.getFecha_inicio() : LocalDate.parse(inicioStr);

        System.out.print("Nueva fecha fin (yyyy-MM-dd) (" + proyectoActual.getFecha_fin() + "): ");
        String finStr = scanner.nextLine();
        LocalDate fin = finStr.isEmpty() ? proyectoActual.getFecha_fin() : LocalDate.parse(finStr);

        System.out.print("Nuevo presupuesto (" + proyectoActual.getPresupuesto() + "): ");
        String presupuestoStr = scanner.nextLine();
        BigDecimal presupuesto = presupuestoStr.isEmpty() ? proyectoActual.getPresupuesto() : new BigDecimal(presupuestoStr);

        Proyecto proyectoActualizado = new Proyecto(id, nombre, inicio, fin, presupuesto);
        return actualizar(proyectoActualizado);
    }

    // Actualiza en la base de datos los datos del proyecto
    public boolean actualizar(Proyecto proyecto) {
        String sql = "UPDATE proyectos SET nombre=?, fecha_inicio=?, fecha_fin=?, presupuesto=? WHERE id_proyecto=?";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, proyecto.getNombre());
            ps.setDate(2, Date.valueOf(proyecto.getFecha_inicio()));
            ps.setDate(3, Date.valueOf(proyecto.getFecha_fin()));
            ps.setBigDecimal(4, proyecto.getPresupuesto());
            ps.setInt(5, proyecto.getIdProyecto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Pide id por consola y llama a eliminar(id)
    public boolean eliminarProyecto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el ID del proyecto a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return eliminar(id);
    }

    // Elimina proyecto por id
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proyectos WHERE id_proyecto=?";
        try (Connection conexion = DatabaseConfigPool.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}