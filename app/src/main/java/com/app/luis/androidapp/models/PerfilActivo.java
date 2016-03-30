package com.app.luis.androidapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import com.app.luis.androidapp.utils.AppConstants;

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
            editor.putString(Usuario.UserAttributes.TOKEN, usuario.getToken());
            editor.putString(Usuario.UserAttributes.ID, usuario.getId());
            editor.putString(Usuario.UserAttributes.ID_FACEBOOK, usuario.getId_facebook());
            editor.putString(Usuario.UserAttributes.NOMBRE, usuario.getNombre());
            editor.putString(Usuario.UserAttributes.APELLIDO, usuario.getApellido());
            editor.putString(Usuario.UserAttributes.FECHA_NACIMIENTO, usuario.getFecha_nacimiento());
            editor.putString(Usuario.UserAttributes.EMAIL, usuario.getEmail());
            editor.putString(Usuario.UserAttributes.SEXO, usuario.getSexo() + "");
            editor.commit();
        }
    }

    public Usuario getFromSharedPreferences(Context context) {

        SharedPreferences shared = context.getSharedPreferences(AppConstants.USER_PREFERENCES, Context.MODE_PRIVATE);
        String token = shared.getString(Usuario.UserAttributes.TOKEN, null);
        String id = shared.getString(Usuario.UserAttributes.ID, null);
        String id_facebook = shared.getString(Usuario.UserAttributes.ID_FACEBOOK, null);
        String nombre = shared.getString(Usuario.UserAttributes.NOMBRE, null);
        String apellido = shared.getString(Usuario.UserAttributes.APELLIDO, null);
        String fecha_nacimiento = shared.getString(Usuario.UserAttributes.FECHA_NACIMIENTO, null);
        String email = shared.getString(Usuario.UserAttributes.EMAIL, null);
        String sexo = shared.getString(Usuario.UserAttributes.SEXO, null);

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
