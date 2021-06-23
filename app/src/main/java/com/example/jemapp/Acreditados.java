package com.example.jemapp;

public class Acreditados {
    String nombre, dni, acredestado;

    public Acreditados(String nombre, String dni, String acredestado) {
        this.nombre = nombre;
        this.dni = dni;
        this.acredestado = acredestado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getAcredestado() {
        return acredestado;
    }

    public void setAcredestado(String acredestado) {
        this.acredestado = acredestado;
    }
}
