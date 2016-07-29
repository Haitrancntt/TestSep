package com.example.haitr.planed_12062016;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import Controller.Account_Control;
import Fragment.AccountFragment;
import Fragment.MainFragment;
import Fragment.ReportFragment;
import Fragment.TagFragment;
import Fragment.TaskFragment;
import Fragment.TimeFragment;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor logout;
    private Account_Control account_control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        account_control = new Account_Control(LoginActivity.db.getConnection());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                int id = item.getItemId();

                if (id == R.id.nav_task) {
                    fm.beginTransaction().replace(R.id.content_frame, new TaskFragment()).commit();
                } else if (id == R.id.nav_time) {
                    fm.beginTransaction().replace(R.id.content_frame, new TimeFragment()).commit();

                } else if (id == R.id.nav_account) {
                    fm.beginTransaction().replace(R.id.content_frame, new AccountFragment()).commit();
                } else if (id == R.id.nav_tag) {
                    fm.beginTransaction().replace(R.id.content_frame, new TagFragment()).commit();

                } else if (id == R.id.nav_report) {
                    fm.beginTransaction().replace(R.id.content_frame, new ReportFragment()).commit();
                } else if (id == R.id.nav_setting) {
                    Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_logout) {
                    LoginActivity.loginRememberEdit.putBoolean("logout", true);
                    LoginActivity.loginRememberEdit.commit();
                   /* sharedPreferences = getSharedPreferences("Current User", MODE_PRIVATE);
                    logout = sharedPreferences.edit();
                    logout.remove("Username");
                    logout.remove("Password");
                    logout.commit();*/
                  /*  if (LoginActivity.checkBox.isChecked()) {
                        account_control.RemoveRemember(LoginActivity.Account_Id);

                        LoginActivity.txtUS.setText("");
                        LoginActivity.txtPass.setText("");
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }*/
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Log out", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_home) {
                    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
