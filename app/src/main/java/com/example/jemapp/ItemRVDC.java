package com.example.jemapp;

public class ItemRVDC {
    private int id;
    private String NombDC, dniDC, codP;

    public ItemRVDC(int id, String nombDC, String dniDC, String codP) {
        this.id = id;
        NombDC = nombDC;
        this.dniDC = dniDC;
        this.codP=codP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodP() {
        return codP;
    }

    public void setCodP(String codP) {
        this.codP = codP;
    }

    public String getNombDC() {
        return NombDC;
    }

    public void setNombDC(String nombDC) {
        NombDC = nombDC;
    }

    public String getDniDC() {
        return dniDC;
    }

    public void setDniDC(String dniDC) {
        this.dniDC = dniDC;
    }
}
