package com.example.jemapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class menuOrganizadorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String iduser,tipouser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_organizador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Bienvenido IV JEM 2019!");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        iduser = getIntent().getExtras().getString("iduser");
        tipouser = getIntent().getExtras().getString("tipouser");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organizador, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        bundle.putString("iduser", iduser);
        bundle.putString("tipouser", tipouser);
        if (id == R.id.nav_perfil) {
            perfilFragment myFrag = new perfilFragment();
            myFrag.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.escenarioOrganizador,myFrag).addToBackStack(null).commit();
        }
        else
        if (id == R.id.nav_acreditar) {
            acreditacionFragment myFrag = new acreditacionFragment();
            myFrag.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.escenarioOrganizador,myFrag).addToBackStack(null).commit();

        } else if (id == R.id.nav_asistencias) {
            bundle.putString("opcion", "asistencias");
            AsistenciasOrganizFragment myFrag = new AsistenciasOrganizFragment();
            myFrag.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.escenarioOrganizador,myFrag).addToBackStack(null).commit();

        } else if (id == R.id.nav_talleres) {
            bundle.putString("opcion", "talleres");
            AsistenciasOrganizFragment myFrag = new AsistenciasOrganizFragment();
            myFrag.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.escenarioOrganizador,myFrag).addToBackStack(null).commit();

        } else if (id == R.id.nav_logout) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
