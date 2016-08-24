package com.app.luis.androidapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.luis.androidapp.R;
import com.app.luis.androidapp.models.LugarExample;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {
    static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 10;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<LugarExample> mContentItems = new ArrayList<>();

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager;

        if (GRID_LAYOUT) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        mContentItems.add(new LugarExample("Che Café", "Un lugar para conversar con tranquilidad, disfrutar de la naturaleza (terraza ó aire libre), leer un libro, festejar ocasiones especiales, comer un bocadillo y saborear nuestras bebidas.", "https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/13962725_601497493353542_5186778811388003687_n.png?oh=c07b5edd8e29e520135771c9f4e8e812&oe=585E9ADD"));
        mContentItems.add(new LugarExample("La Esquina café", "El mejor lugar para tomar un auténtico café. Tés de importación de hoja suelta, tisanas y cervezas artesanales de alta calidad. Ambiente familiar.", "https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/13599825_985051001614091_7349077297967684586_n.jpg?oh=68a55d3a282f0094aa0ef0f0a55b2879&oe=5849A80D"));
        mContentItems.add(new LugarExample("Sushi Sai", "Es un Restaurante de comida Japonesa, Tailandesa y China perfecto para pasar un buen momento con tus personas favoritas.", "https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/10991193_771153742998467_3795393028075148441_n.jpg?oh=05a946da79062c93749067f2753e3b5e&oe=584FC39D"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mAdapter = new TestRecyclerViewAdapter(mContentItems);

        mRecyclerView.setAdapter(mAdapter);

//        {
//            for (int i = 0; i < ITEM_COUNT; ++i) {
//                mContentItems.add(new Object());
//            }
//            mAdapter.notifyDataSetChanged();
//        }
    }

}
