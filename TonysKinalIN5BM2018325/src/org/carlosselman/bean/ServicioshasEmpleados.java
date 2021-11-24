package org.carlosselman.bean;
import java.util.Date;
public class ServicioshasEmpleados {
    
    private int codigoServicio;
    private int codigoEmpleado;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;

    public ServicioshasEmpleados() {
    }

    public ServicioshasEmpleados(int codigoServicio, int codigoEmpleado, Date fechaEvento, String horaEvento, String lugarEvento) {
        this.codigoServicio = codigoServicio;
        this.codigoEmpleado = codigoEmpleado;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }
}
