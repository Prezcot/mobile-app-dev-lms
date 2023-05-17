package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        // Handle click on the "Dashboard" item
                        // Navigate to the corresponding page or perform desired actions
                        return true;
                    case R.id.navigation_calendar:
                        // Handle click on the "Calendar" item
                        // Navigate to the corresponding page or perform desired actions
                        return true;
                    case R.id.navigation_results:
                        startActivity(new Intent(Dashboard.this,Results.class));
                        finish();
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