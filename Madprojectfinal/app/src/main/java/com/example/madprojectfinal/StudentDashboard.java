package com.example.madprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentDashboard extends AppCompatActivity {
    Button transfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dashboard);

        transfiguration = findViewById(R.id.Transfiguration);

        transfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.module = "transfiguration";
                startActivity(new Intent(StudentDashboard.this,StudentDashboard.class));
                finish();
            }
        });

    }
}