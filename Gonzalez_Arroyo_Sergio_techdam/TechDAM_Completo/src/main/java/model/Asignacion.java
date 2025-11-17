package model;

import java.time.LocalDate;

/*
 Clase modelo que representa una fila de la tabla asignaciones.
 Tiene campos básicos y getters/setters.
*/
public class Asignacion {
    private int idAgisnacion;
    private int id_Empleado;
    private int id_Proyecto;
    private LocalDate fecha_asignacion;

    public Asignacion(int idAgisnacion, int id_Empleado, int id_Proyecto, LocalDate fecha_asignacion) {
        this.idAgisnacion = idAgisnacion;
        this.id_Empleado = id_Empleado;
        this.id_Proyecto = id_Proyecto;
        this.fecha_asignacion = fecha_asignacion;
    }

    public Asignacion(){}

    public int getIdAgisnacion() {
        return idAgisnacion;
    }

    public void setIdAgisnacion(int idAgisnacion) {
        this.idAgisnacion = idAgisnacion;
    }

    public int getId_Empleado() {
        return id_Empleado;
    }

    public void setId_Empleado(int id_Empleado) {
        this.id_Empleado = id_Empleado;
    }

    public int getId_Proyecto() {
        return id_Proyecto;
    }

    public void setId_Proyecto(int id_Proyecto) {
        this.id_Proyecto = id_Proyecto;
    }

    public LocalDate getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(LocalDate fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "idAgisnacion=" + idAgisnacion +
                ", id_Empleado=" + id_Empleado +
                ", id_Proyecto=" + id_Proyecto +
                ", fecha_asignacion=" + fecha_asignacion +
                '}';
    }
}