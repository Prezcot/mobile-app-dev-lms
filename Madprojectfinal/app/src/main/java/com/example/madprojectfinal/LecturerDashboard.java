package com.example.madprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LecturerDashboard extends AppCompatActivity {

    Button transfiguration;

    public static String module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dashboard);

        transfiguration = findViewById(R.id.Transfiguration);

        transfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                module = "Transfiguration";
                startActivity(new Intent(LecturerDashboard.this,TransfigurationLecturer.class));
                finish();
            }
        });

    }
}