package org.carlosselman.bean;
public class ServicioshasPlatos {
    private int codigoServicio; 
    private int codigoPlato; 

    public ServicioshasPlatos() {
    }

    public ServicioshasPlatos(int codigoServicio, int codigoPlato) {
        this.codigoServicio = codigoServicio;
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    
}
