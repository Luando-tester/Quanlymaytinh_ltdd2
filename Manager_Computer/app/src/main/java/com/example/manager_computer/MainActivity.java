package com.example.manager_computer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkIfAlreadyhavePermission()) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        }

        navigationView = (NavigationView)findViewById(R.id.nav);

        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new BillFragment()).commit();

            navigationView.setCheckedItem(R.id.bill);

            setTitle("Quản lý máy tính - Phiếu phát sinh");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();

                finish();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.bill:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new BillFragment()).commit();

                menuItem.setChecked(true);

                setTitle("Quản lý máy tính - Phiếu phát sinh");
                break;
            case R.id.customer:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new CustomerFragment()).commit();

                menuItem.setChecked(true);

                setTitle("Quản lý máy tính - Khách hàng");
                break;
            case R.id.service:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ServiceFragment()).commit();

                menuItem.setChecked(true);

                setTitle("Quản lý máy tính - Dịch vụ");
                break;
            case R.id.revenue:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new RevenueFragment()).commit();

                menuItem.setChecked(true);

                setTitle("Quản lý máy tính - Thống kê doanh thu");
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
