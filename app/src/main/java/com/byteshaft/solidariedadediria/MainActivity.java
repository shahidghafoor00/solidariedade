package com.byteshaft.solidariedadediria;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.byteshaft.solidariedadediria.sidebar_fragments.Home;
import com.byteshaft.solidariedadediria.sidebar_fragments.Institution;
import com.byteshaft.solidariedadediria.sidebar_fragments.Movements;
import com.byteshaft.solidariedadediria.sidebar_fragments.Support;
import com.byteshaft.solidariedadediria.sidebar_fragments.UserInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new Home());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);

        BroadcastReceiver screenOnOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();

                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

                if (strAction.equals(Intent.ACTION_SCREEN_OFF) || strAction.equals(Intent.ACTION_SCREEN_ON)) {
                    if (myKM.inKeyguardRestrictedInputMode()) {
                        System.out.println("Screen off " + "LOCKED");
                        if (AdActivity.getInstance() == null) {
                            startActivity(new Intent(MainActivity.this, AdActivity.class));
                        }
                    } else {
                        if (AdActivity.getInstance() == null) {
                            System.out.println("Screen off " + "UNLOCKED");
                            startActivity(new Intent(MainActivity.this, AdActivity.class));
                        }
                    }
                }
            }
        };

        getApplicationContext().registerReceiver(screenOnOffReceiver, theFilter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new Home());
            // Handle the camera action
        } else if (id == R.id.nav_institution) {
            loadFragment(new Institution());

        } else if (id == R.id.nav_movements) {
            loadFragment(new Movements());

        } else if (id == R.id.nav_user_info) {
            loadFragment(new UserInfo());

        } else if (id == R.id.nav_support) {
            loadFragment(new Support());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void loadFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.commit();
    }
}
