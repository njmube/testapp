package com.app.luis.androidapp.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Luis on 20/02/2016.
 */
public class Usuario {

    private String token;
    private String id;
    private String nombre;
    private String apellido;
    private Date fecha_nacimiento;
    private String email;
    private char sexo;

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.fecha_nacimiento = format.parse(fecha_nacimiento);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "UsuarioEnum{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fecha_nacimiento=" + fecha_nacimiento +
                ", email='" + email + '\'' +
                ", sexo=" + sexo +
                '}';
    }
}
