package com.example.fortesting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    ImageButton Math;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Math = findViewById(R.id.math);
        Math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View includedLayout = findViewById(R.id.nav_bar);

                // Find the TextView within the included layout
                TextView textView = includedLayout.findViewById(R.id.textView2);

                // Set the new text value
                textView.setText(MainActivity.username);
                MainActivity.module = "Mathematics";
                startActivity(new Intent(Dashboard.this, MathStudent.class));
                finish();
            }
        });

    }
}