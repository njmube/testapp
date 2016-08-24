package com.app.luis.androidapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.fragments.RecyclerViewFragment;
import com.app.luis.androidapp.utils.Utils;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class MVPActivity extends AppCompatActivity {

    MaterialViewPager mViewPager;
    DrawerLayout drawerLayout;

    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.setDrawerListener(mDrawerToggle);

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    //case 0:
                    //    return RecyclerViewFragment.newInstance();
                    //case 1:
                    //    return RecyclerViewFragment.newInstance();
                    //case 2:
                    //    return WebViewFragment.newInstance();
                    default:
                        return RecyclerViewFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return "Novedades";
                    case 1:
                        return "Lugares";
                    case 2:
                        return "Precios";
                    case 3:
                        return "Eventos";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,
                                "https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/13413642_973218942797297_959563660214120468_n.jpg?oh=d35cdb01d389038bd6af63d4316ebc43&oe=5851356E");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,
                                "https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/12049709_536427233193902_8295308922045940099_n.jpg?oh=38da29e48655bc091de8d8f3dfd7f0f7&oe=585A7FF5");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,
                                "https://scontent-atl3-1.xx.fbcdn.net/v/t1.0-9/13307404_506780582779964_927891767074137979_n.jpg?oh=370020e0838156a33b97935fe9559847&oe=584B80A5");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,
                                "https://scontent-atl3-1.xx.fbcdn.net/t31.0-8/13735571_10153754588739033_2920905685425088319_o.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.tvHeaderHome);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }


}
