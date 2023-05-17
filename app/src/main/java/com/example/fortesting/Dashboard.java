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
        View includedLayout = findViewById(R.id.nav_bar);
        TextView textView = includedLayout.findViewById(R.id.textView2);
        textView.setText(MainActivity.username);

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