package com.app.luis.androidapp.models;

/**
 * Created by Luis on 24/08/2016.
 */
public class LugarExample {
    private String nombre;
    private String direccion;
    private String logo;

    public LugarExample(String nombre, String direccion, String logo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }
}


