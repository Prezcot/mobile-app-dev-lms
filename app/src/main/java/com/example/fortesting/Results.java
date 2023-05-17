package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Results extends AppCompatActivity {
    TextView math,english;
    DatabaseReference dbreff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_results);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_results);
        
        TableLayout tableLayout = findViewById(R.id.results);

        TableRow row2 = (TableRow) tableLayout.getChildAt(1);
        math = (TextView) row2.getChildAt(1);

        TableRow row3 = (TableRow) tableLayout.getChildAt(2);
        english = (TextView) row3.getChildAt(1);

        dbreff = FirebaseDatabase.getInstance().getReference();

        try {
            dbreff.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String addmath = snapshot.child(MainActivity.username).child("math").getValue(String.class);
                    String addenglish = snapshot.child(MainActivity.username).child("english").getValue(String.class);
                    math.setText(addmath);
                    english.setText(addenglish);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }catch (Exception e){
            Toast.makeText(Results.this, "Please submit coursework",Toast.LENGTH_SHORT).show();
        }

        View includedLayout = findViewById(R.id.nav_bar);
        ImageButton back = includedLayout.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Results.this, Dashboard.class));
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_dashboard) {
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_calendar) {
                    startActivity(new Intent(getApplicationContext(), WebCalendar.class));
                    finish();
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