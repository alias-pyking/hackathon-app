package com.example.shivam.project1;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragmentSos()).commit();
            navigationView.setCheckedItem(R.id.emergency);
        }


        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        MyReceiver myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.emergency:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentSos()).commit();
                break;

            case R.id.explore:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentExplore()).commit();
                break;

            case R.id.safetyaudit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentSafetyAudit()).commit();
                break;

            case R.id.followme:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentFollowMe()).commit();
                break;

            case R.id.myposts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentMyPosts()).commit();
                break;

            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentSettings()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

//    @Override
//    protected void onDestroy()
//    {
//        if (myReceiver != null)
//        {
//            unregisterReceiver(myReceiver);
//            myReceiver = null;
//        }
//        super.onDestroy();
//    }


}
