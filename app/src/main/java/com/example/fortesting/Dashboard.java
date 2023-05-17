package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    ImageButton Math;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Math = findViewById(R.id.math);
        View includedLayout = findViewById(R.id.nav_bar);
        TextView textView = includedLayout.findViewById(R.id.textView2);
        textView.setText(MainActivity.username);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
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


        Math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.module = "Mathematics";
                if(MainActivity.account_type.equals("admin")){
                    startActivity(new Intent(Dashboard.this, MathLecturer.class));
                    finish();
                }else{
                    startActivity(new Intent(Dashboard.this, MathStudent.class));
                    finish();
                }

            }
        });

    }
}