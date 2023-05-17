package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_results);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_results);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_dashboard) {
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_calendar) {
                    //have the calendar function run here
                    return true;
                } else if (item.getItemId() == R.id.navigation_results) {
                    startActivity(new Intent(getApplicationContext(), Results.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }
}