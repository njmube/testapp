package com.app.luis.androidapp.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.fragments.HomeAdapter;
import com.app.luis.androidapp.models.PerfilActivo;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.Utils;
import com.astuetz.PagerSlidingTabStrip;

public class Home extends AbstractActivity {

    @Bind(R.id.toolbar_actionbar)
    Toolbar toolbar;

    private Usuario usuarioActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menú");

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new HomeAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setBackgroundColor(Utils.getColor(getApplicationContext(), R.color.primary));

        usuarioActivo = PerfilActivo.getInstance().getFromSharedPreferences(getApplicationContext());

        Toast.makeText(this, "Bienvenido " + usuarioActivo.getNombreCompleto(), Toast.LENGTH_LONG).show();
    }
}
