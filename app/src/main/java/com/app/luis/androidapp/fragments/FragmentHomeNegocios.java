package com.app.luis.androidapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.luis.androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeNegocios extends Fragment {


    public FragmentHomeNegocios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_negocios,container,false);
    }
}
