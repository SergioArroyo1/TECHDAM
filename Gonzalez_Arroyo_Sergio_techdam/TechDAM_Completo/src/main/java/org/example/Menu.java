package org.example;

import dao.EmpleadoDAO;
import dao.ProyectoDAO;
import service.ProcedimientosService;
import service.TransaccionesService;

import java.math.BigDecimal;
import java.util.*;

import static config.DatabaseConfigPool.cerrarPool;

/*
 Clase que muestra el menú de la aplicación por consola.
 Contiene submenús para empleados, proyectos, procedimientos y transacciones.
 Usa Scanner para leer la opción del usuario.
 Al salir del menú de transacciones se cierra el pool de conexiones.
*/
public class Menu {

    private final EmpleadoDAO empleadoDAO;
    private final ProyectoDAO proyectoDAO;
    private final ProcedimientosService procedimientosService;
    private final TransaccionesService transaccionesService;
    private final Scanner scanner= new Scanner(System.in);

    public Menu(
            EmpleadoDAO empleadoDAO,
            ProyectoDAO proyectoDAO,
            ProcedimientosService procedimientosService,
            TransaccionesService transaccionesService
    ) {
        this.empleadoDAO = empleadoDAO;
        this.proyectoDAO = proyectoDAO;
        this.procedimientosService = procedimientosService;
        this.transaccionesService = transaccionesService;
    }

    // Bucle principal que muestra opciones hasta que el usuario elige 0
    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n== MENÚ PRINCIPAL ==");
            System.out.println("1. Gestión de empleados");
            System.out.println("2. Gestión de proyectos");
            System.out.println("3. Procedimientos almacenados");
            System.out.println("4. Transacciones");
            System.out.println("0. Salir");
            System.out.print("Elige opción: ");

            opcion = scanner.hasNextInt() ? scanner.nextInt() : -1;
            scanner.nextLine(); // limpiar input

            switch (opcion) {
                case 1:
                    submenuEmpleados();
                    break;
                case 2:
                    submenuProyectos();
                    break;
                case 3:
                    submenuProcedimientos();
                    break;
                case 4:
                    submenuTransacciones();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    // Submenú para operaciones con empleados
    private void submenuEmpleados() {
        int opcion;
        do {
            System.out.println("\n-- Gestión de empleados --");
            System.out.println("1. Mostrar empleados");
            System.out.println("2. Insertar empleado");
            System.out.println("3. Modificar empleado");
            System.out.println("4. Eliminar empleado");
            System.out.println("0. Volver");
            System.out.print("Elige opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    // Muestra la lista por consola usando toString() de Empleado
                    empleadoDAO.obtenerEmpleados().forEach(System.out::println);
                    break;
                case 2:
                    int nuevoId = empleadoDAO.InsertarEmpleado();
                    if (nuevoId > 0)
                        System.out.println("Empleado creado con ID " + nuevoId);
                    else
                        System.out.println("No se pudo crear el empleado.");
                    break;
                case 3:
                    if (empleadoDAO.actualizarEmpleado())
                        System.out.println("Empleado modificado correctamente.");
                    else
                        System.out.println("No se pudo modificar el empleado.");
                    break;
                case 4:
                    if (empleadoDAO.eliminarEmpleado())
                        System.out.println("Empleado eliminado correctamente.");
                    else
                        System.out.println("No se pudo eliminar el empleado.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
    }

    // Submenú para operaciones con proyectos
    private void submenuProyectos() {
        int opcion;
        do {
            System.out.println("\n-- Gestión de proyectos --");
            System.out.println("1. Mostrar proyectos");
            System.out.println("2. Insertar proyecto");
            System.out.println("3. Modificar proyecto");
            System.out.println("4. Eliminar proyecto");
            System.out.println("0. Volver");
            System.out.print("Elige opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    proyectoDAO.obtenerProyectos().forEach(System.out::println);
                    break;
                case 2:
                    int nuevoId = proyectoDAO.InsertarProyecto();
                    if (nuevoId > 0)
                        System.out.println("Proyecto creado con ID " + nuevoId);
                    else
                        System.out.println("No se pudo crear el proyecto.");
                    break;
                case 3:
                    if (proyectoDAO.actualizarProyecto())
                        System.out.println("Proyecto modificado correctamente.");
                    else
                        System.out.println("No se pudo modificar el proyecto.");
                    break;
                case 4:
                    if (proyectoDAO.eliminarProyecto())
                        System.out.println("Proyecto eliminado correctamente.");
                    else
                        System.out.println("No se pudo eliminar el proyecto.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
    }

    // Submenú para llamar a procedimientos almacenados
    private void submenuProcedimientos() {
        int opcion;
        do {
            System.out.println("\n--- Procedimientos almacenados ---");
            System.out.println("1. Actualizar salario por departamento");
            System.out.println("2. Empleados por proyecto");
            System.out.println("0. Volver");
            System.out.print("Elige opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    procedimientosService.actualizarSalarioDepartamento();
                    break;
                case 2:
                    procedimientosService.empleadosPorProyecto();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
    }

    // Submenú para transacciones: transferencias y savepoints
    private void submenuTransacciones() {
        int opcion;
        do {
            System.out.println("\n-- Transacciones --");
            System.out.println("1. Transferir presupuesto entre proyectos");
            System.out.println("2. Asignar empleados a proyecto");
            System.out.println("0. Volver");
            System.out.print("Elige opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Id de proyecto de origen: ");
                    int idOrigen = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Id de proyecto de destino: ");
                    int idDestino = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Dinero a transferir: ");
                    BigDecimal Dinero = new BigDecimal(scanner.nextLine());
                    boolean transferido = transaccionesService.transferirPresupuesto(idOrigen, idDestino, Dinero);
                    System.out.println(transferido ? "Transferencia realizada correctamente." : "No se pudo realizar la transferencia.");
                    break;
                case 2:
                    System.out.print("Id de proyecto destino: ");
                    int idProyectoSP = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Introduce los IDs de empleados separados por coma: ");
                    String ids = scanner.nextLine();
                    List<Integer> idList = new ArrayList<>();
                    Arrays.stream(ids.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .forEach(s -> {
                                try {
                                    idList.add(Integer.parseInt(s));
                                } catch (NumberFormatException ignored) {
                                }
                            });
                    transaccionesService.asignarEmpleadosConSavepoint(idProyectoSP, idList);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
        cerrarPool(); // Cierra el pool de conexiones al salir del menú de transacciones
    }
}