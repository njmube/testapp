package com.app.luis.androidapp.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.luis.androidapp.R;
import com.app.luis.androidapp.fragments.HomeAdapter;
import com.app.luis.androidapp.models.PerfilActivo;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.Utils;
import com.astuetz.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;

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

        usuarioActivo = PerfilActivo.getInstance().getFromSharedPreferences(Home.this);

        Toast.makeText(this, "Bienvenido " + usuarioActivo.getNombreCompleto(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
