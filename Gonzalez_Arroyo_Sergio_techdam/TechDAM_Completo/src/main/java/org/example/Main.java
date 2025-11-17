package org.example;

import dao.EmpleadoDAO;
import dao.ProyectoDAO;
import model.Empleado;
import service.ProcedimientosService;
import service.TransaccionesService;

public class Main {
    public static void main (String[] args){
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        ProcedimientosService procedimientosService = new ProcedimientosService();
        TransaccionesService transaccionesService = new TransaccionesService();

        Menu menu = new Menu(empleadoDAO, proyectoDAO, procedimientosService, transaccionesService);
        menu.mostrarMenu();
    }
}

