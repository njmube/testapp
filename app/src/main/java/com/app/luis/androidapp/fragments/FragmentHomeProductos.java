package com.app.luis.androidapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.models.PerfilActivo;
import com.app.luis.androidapp.models.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeProductos extends Fragment {

    private Usuario usuarioActivo;

    public FragmentHomeProductos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        usuarioActivo = PerfilActivo.getInstance().getUsuario();
        TextView textView = new TextView(getActivity());
        textView.setText(usuarioActivo.getNombreCompleto());
        return textView;
    }


}
