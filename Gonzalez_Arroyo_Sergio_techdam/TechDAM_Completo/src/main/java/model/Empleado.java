package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
 Clase modelo para Empleado con sus atributos, constructores,
 getters/setters y toString. Se usa en los DAO y en la aplicación.
*/
public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String departamento;
    private BigDecimal salario;
    private LocalDate fecha_contratacion;
    private boolean activo;

    public Empleado(int idEmpleado, String nombre,String departamento, BigDecimal salario, LocalDate fecha_contratacion, boolean activo){
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.fecha_contratacion = fecha_contratacion;
        this.activo = activo;
    }

    public Empleado(){}

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(LocalDate fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento(){
        return departamento;
    }

    public void setDepartamento(String departamento){
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", nombre='" + nombre + '\'' +
                ", departamento='" + departamento + '\'' +
                ", salario=" + salario +
                ", fecha_contratacion=" + fecha_contratacion +
                ", activo=" + activo +
                '}';
    }
}