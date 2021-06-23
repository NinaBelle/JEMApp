package com.example.jemapp;

public class DatosCertif {
    private int id;
    private String nombT, estT, estCertif;

    public DatosCertif(int id, String nombT, String estT, String estCertif) {
        this.id = id;
        this.nombT = nombT;
        this.estT = estT;
        this.estCertif = estCertif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombT() {
        return nombT;
    }

    public void setNombT(String nombT) {
        this.nombT = nombT;
    }

    public String getEstT() {
        return estT;
    }

    public void setEstT(String estT) {
        this.estT = estT;
    }

    public String getEstCertif() {
        return estCertif;
    }

    public void setEstCertif(String estCertif) {
        this.estCertif = estCertif;
    }
}
