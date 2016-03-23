package com.app.luis.androidapp.enums;

/**
 * Created by Luis on 06/03/2016.
 */
public enum UsuarioEnum {

    TOKEN("token"),
    ID("id"),
    ID_FACEBOOK("id_facebook"),
    NOMBRE("nombre"),
    APELLIDO("apellido"),
    FECHA_NACIMIENTO("fecha_nacimiento"),
    EMAIL("email"),
    SEXO("sexo");

    private final String value;

    UsuarioEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
