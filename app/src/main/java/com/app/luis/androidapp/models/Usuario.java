package com.app.luis.androidapp.models;

/**
 * Created by Luis on 20/02/2016.
 */
public class Usuario {

    private String token;
    private String id;
    private String id_facebook;
    private String nombre;
    private String apellido;
    private String fecha_nacimiento;
    private String email;
    private char sexo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_facebook() {
        return id_facebook;
    }

    public void setId_facebook(String id_facebook) {
        this.id_facebook = id_facebook;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
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

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
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

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "apellido='" + apellido + '\'' +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                ", id_facebook='" + id_facebook + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fecha_nacimiento='" + fecha_nacimiento + '\'' +
                ", email='" + email + '\'' +
                ", sexo=" + sexo +
                '}';
    }

    public static class UserAttributes {
        public static final String TOKEN = "token";
        public static final String ID = "id";
        public static final String ID_FACEBOOK = "id_facebook";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO = "apellido";
        public static final String FECHA_NACIMIENTO = "fecha_nacimiento";
        public static final String EMAIL = "email";
        public static final String SEXO = "sexo";
    }

    public static class UserTagAttribute {
        public static final String TAG = "tag";
    }
}
