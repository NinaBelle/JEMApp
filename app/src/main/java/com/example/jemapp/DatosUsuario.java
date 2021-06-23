package com.example.jemapp;

public class DatosUsuario
{
    private String Apellido, Nombre, DNI, Titulo, Universidad, Correo, Domicilio;

     public String getApellido() {

        return this.Apellido;
    }
    public String getNombre() {

        return Nombre;
    }
    public String getDNI() {

        return DNI;
    }
    public String getTitulo() {

        return Titulo;
    }
    public String getUniversidad() {

        return Universidad;
    }
    public String getDomicilio() {
        return Domicilio;
    }
    public String setApellido(String Apellido) {

        return this.Apellido=Apellido;
    }
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    public void setDNI(String DNI) {
        this.DNI = DNI;
    }
    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }
    public void setUniversidad(String Universidad) {
        this.Universidad = Universidad;
    }
    public void setDomicilio(String Domicilio) {
        this.Domicilio = Domicilio;
    }
    public void setCorreo(String Correo){
        this.Correo = Correo;
    }
    public String getCorreo() {

        return Correo;
    }

}
