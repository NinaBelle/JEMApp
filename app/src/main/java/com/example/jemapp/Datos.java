package com.example.jemapp;

public class Datos {
    private  int id;
    private String Accion;
    private int icono;

    public Datos(int id, String accion, int icono) {
        this.id = id;
        Accion = accion;
        this.icono = icono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccion() {
        return Accion;
    }

    public void setAccion(String accion) {
        Accion = accion;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
