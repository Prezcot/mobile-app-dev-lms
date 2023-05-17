package com.example.madprojectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    EditText name,pass;
    Button loginbtn;

    DatabaseReference reff;

    public static String username;
    public static  String account_type;

    public static String module;

    public static String coursework_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name =findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        loginbtn = findViewById(R.id.login);
        reff = FirebaseDatabase.getInstance().getReference();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkname = name.getText().toString();
                String checkpassword = pass.getText().toString();

                if(checkname.isEmpty() || checkpassword.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter name and password",Toast.LENGTH_SHORT).show();
                }
                else{
                    reff.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(checkname)){
                                String password = snapshot.child(checkname).child("password").getValue(String.class);
                                if(password.equals(checkpassword)){
                                    String access = snapshot.child(checkname).child("account_type").getValue(String.class);
                                    username = checkname;
                                    account_type = access;
                                    Toast.makeText(MainActivity.this,access,Toast.LENGTH_SHORT).show();
                                    if(access.equals("admin")){
                                        startActivity(new Intent(MainActivity.this,LecturerDashboard.class));
                                        finish();
                                    }
                                    else{
                                        startActivity(new Intent(MainActivity.this,StudentDashboard.class));
                                        finish();
                                    }
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Incorrect user name",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}