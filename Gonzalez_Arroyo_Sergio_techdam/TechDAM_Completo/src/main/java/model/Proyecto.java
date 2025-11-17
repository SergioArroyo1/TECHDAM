package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
 Clase modelo para Proyecto con sus atributos básicos y métodos.
*/
public class Proyecto {
    private int idProyecto;
    private String nombre;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private BigDecimal presupuesto;

    public Proyecto(int idProyecto, String nombre, LocalDate fecha_inicio, LocalDate fecha_fin, BigDecimal presupuesto) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.presupuesto = presupuesto;
    }
    public Proyecto(){}

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "idProyecto=" + idProyecto +
                ", nombre='" + nombre + '\'' +
                ", fecha_inicio=" + fecha_inicio +
                ", fecha_fin=" + fecha_fin +
                ", presupuesto=" + presupuesto +
                '}';
    }
}