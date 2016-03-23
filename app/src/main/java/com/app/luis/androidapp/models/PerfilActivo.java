package com.app.luis.androidapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.app.luis.androidapp.enums.UsuarioEnum;
import com.app.luis.androidapp.utils.AppConstants;
import com.app.luis.androidapp.utils.Utils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Luis Macias on 23/03/2016.
 */
public class PerfilActivo {
    private static PerfilActivo instance = new PerfilActivo();

    private Usuario usuario;

    private PerfilActivo() {
    }

    public static PerfilActivo getInstance() {
        return instance;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void updateInfo(Context context) {
        if (usuario != null) {
            SharedPreferences preferences = context.getSharedPreferences(AppConstants.USER_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(UsuarioEnum.TOKEN.getValue(), usuario.getToken());
            editor.putString(UsuarioEnum.ID.getValue(), usuario.getId());
            editor.putString(UsuarioEnum.ID_FACEBOOK.getValue(), usuario.getId_facebook());
            editor.putString(UsuarioEnum.NOMBRE.getValue(), usuario.getNombre());
            editor.putString(UsuarioEnum.APELLIDO.getValue(), usuario.getApellido());
            editor.putString(UsuarioEnum.FECHA_NACIMIENTO.getValue(), usuario.getFecha_nacimiento().toString());
            editor.putString(UsuarioEnum.EMAIL.getValue(), usuario.getEmail());
            editor.putString(UsuarioEnum.SEXO.getValue(), usuario.getSexo() + "");
            editor.commit();
        }
    }

    public Usuario getFromSharedPreferences(Context context) {

        SharedPreferences shared = context.getSharedPreferences(AppConstants.USER_PREFERENCES, context.MODE_PRIVATE);
        String token = shared.getString(UsuarioEnum.TOKEN.getValue(), null);
        String id = shared.getString(UsuarioEnum.ID.getValue(), null);
        String id_facebook = shared.getString(UsuarioEnum.ID_FACEBOOK.getValue(), null);
        String nombre = shared.getString(UsuarioEnum.NOMBRE.getValue(), null);
        String apellido = shared.getString(UsuarioEnum.APELLIDO.getValue(), null);
        String fecha_nacimiento = shared.getString(UsuarioEnum.FECHA_NACIMIENTO.getValue(), null);
        String email = shared.getString(UsuarioEnum.EMAIL.getValue(), null);
        String sexo = shared.getString(UsuarioEnum.SEXO.getValue(), null);

        usuario = new Usuario();
        usuario.setToken(token);
        usuario.setId(id);
        usuario.setId_facebook(id_facebook);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFecha_nacimiento(fecha_nacimiento);
        usuario.setEmail(email);
        usuario.setSexo(sexo.charAt(0));

        return usuario;
    }
}
