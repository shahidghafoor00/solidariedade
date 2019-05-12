package com.byteshaft.solidariedadediria;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.byteshaft.solidariedadediria.account.AccountManager;
import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.User;
import com.byteshaft.solidariedadediria.sidebar_fragments.Home;
import com.byteshaft.solidariedadediria.sidebar_fragments.Institution;
import com.byteshaft.solidariedadediria.sidebar_fragments.Movements;
import com.byteshaft.solidariedadediria.sidebar_fragments.Support;
import com.byteshaft.solidariedadediria.sidebar_fragments.UserInfo;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountManager.getInstance() != null) {
            AccountManager.getInstance().finish();
        }
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
        saveUserDetails(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL), AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_PASSWORD));
        System.out.println("user Id is: " +  AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ID));
        registerBroadcastReceiver();
    }

    private void saveUserDetails(final String email, final String password) {
        class GetUser extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {
                User user = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao().getUser(email, password);
                System.out.println(user);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Invalid Password or Email", Toast.LENGTH_SHORT).show();
                } else {
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, user.getUsername());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_EMAIL, user.getEmail());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, user.getPassword());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_AMOUNT, user.getAmount());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_ID, String.valueOf(user.getId()));
                }
            }
        }

        GetUser getUser = new GetUser();
        getUser.execute();
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

        }
        else if (id == R.id.nav_institution) {
            loadFragment(new Institution());

        }
        else if (id == R.id.nav_movements) {
            loadFragment(new Movements());

        } else if (id == R.id.nav_user_info) {
            loadFragment(new UserInfo());

        } else if (id == R.id.nav_support) {
            loadFragment(new Support());
        } else if (id == R.id.nav_logout) {
            logOutDialog();
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

    private void logOutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirmation!");
        alertDialogBuilder.setMessage("Do you want to logout?")
                .setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppGlobals.clearSettings();
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), AccountManager.class));
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
