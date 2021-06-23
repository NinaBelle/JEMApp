package com.example.jemapp;

public class DatosAsistF {
    private  int id;
    private String opciones;

    public DatosAsistF(int id, String opciones) {
        this.id = id;
        this.opciones = opciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }
}
