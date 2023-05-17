package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MathStudent extends AppCompatActivity {
    StorageReference folderRef,mfolderref;
    DatabaseReference dbreff;

    public String show_list;

    Button lectures, coursework,miscellaneous;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_module_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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


        dbreff = FirebaseDatabase.getInstance().getReference();
        folderRef = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/Lecture/");
        mfolderref = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/Miscellaneous/");
        ListView listView = findViewById(R.id.info);
        lectures = findViewById(R.id.leture);
        coursework = findViewById(R.id.coursework);
        miscellaneous= findViewById(R.id.miscellaneous);


        ArrayList<String> fileList = new ArrayList<>();

        View includedLayout = findViewById(R.id.nav_bar);
        ImageButton back = includedLayout.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MathStudent.this, Dashboard.class));
                finish();
            }
        });
        ImageButton logout = includedLayout.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        lectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ColorStateList colorStateList1 = ColorStateList.valueOf(Color.GRAY);
                lectures.setBackgroundTintList(colorStateList1);
                ColorStateList colorStateList2 = ColorStateList.valueOf(Color.TRANSPARENT);
                coursework.setBackgroundTintList(colorStateList2);
                ColorStateList colorStateList3 = ColorStateList.valueOf(Color.TRANSPARENT);
                miscellaneous.setBackgroundTintList(colorStateList3);


                fileList.clear();
                show_list = "lectures";
                folderRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            fileList.add(item.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MathStudent.this, android.R.layout.simple_list_item_1, fileList);

                        listView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        coursework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorStateList colorStateList1 = ColorStateList.valueOf(Color.TRANSPARENT);
                lectures.setBackgroundTintList(colorStateList1);
                ColorStateList colorStateList2 = ColorStateList.valueOf(Color.GRAY);
                coursework.setBackgroundTintList(colorStateList2);
                ColorStateList colorStateList3 = ColorStateList.valueOf(Color.TRANSPARENT);
                miscellaneous.setBackgroundTintList(colorStateList3);


                fileList.clear();

                show_list = "coursework";
                dbreff.child("Module").child(MainActivity.module).child("Coursework").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String fileName = childSnapshot.getKey();
                            fileList.add(fileName);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MathStudent.this, android.R.layout.simple_list_item_1, fileList);

                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ColorStateList colorStateList1 = ColorStateList.valueOf(Color.TRANSPARENT);
                lectures.setBackgroundTintList(colorStateList1);
                ColorStateList colorStateList2 = ColorStateList.valueOf(Color.TRANSPARENT);
                coursework.setBackgroundTintList(colorStateList2);
                ColorStateList colorStateList3 = ColorStateList.valueOf(Color.GRAY);
                miscellaneous.setBackgroundTintList(colorStateList3);


                fileList.clear();
                show_list = "Miscellaneous";
                mfolderref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            fileList.add(item.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MathStudent.this, android.R.layout.simple_list_item_1, fileList);

                        listView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = fileList.get(position);
                if(show_list.equals("lectures")){
                    StorageReference fileRef = folderRef.child(fileName);

                    // Create a local file to save the downloaded file

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Use the URL with Uri.parse()
                            String fileUrl = uri.toString();
                            Uri parsedUri = Uri.parse(fileUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setType("application/pdf");
                            intent.setData(parsedUri);
                            startActivity(intent);
                            Toast.makeText(MathStudent.this, "File downloaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MathStudent.this, "File download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (show_list.equals("coursework")){
                    MainActivity.coursework_name = fileName;
                    startActivity(new Intent(MathStudent.this,CourseworkSubmission.class));
                    finish();
                } else if (show_list.equals("Miscellaneous")) {
                    StorageReference fileRef = mfolderref.child(fileName);

                    // Create a local file to save the downloaded file

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Use the URL with Uri.parse()
                            String fileUrl = uri.toString();
                            Uri parsedUri = Uri.parse(fileUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setType("application/pdf");
                            intent.setData(parsedUri);
                            startActivity(intent);
                            Toast.makeText(MathStudent.this, "File downloaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MathStudent.this, "File download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }



}

