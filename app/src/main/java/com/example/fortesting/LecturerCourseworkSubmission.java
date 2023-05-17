package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LecturerCourseworkSubmission extends AppCompatActivity {
    TextView coursework;
    ListView listView;

    StorageReference cwreff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_course_work_view);



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

        coursework = findViewById(R.id.Courseworktitle);
        listView = findViewById(R.id.ListCoursework);

        ArrayList<String> fileList = new ArrayList<>();

        cwreff = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/Coursework/"+MainActivity.coursework_name+"/submissions/");

        cwreff.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {
                    fileList.add(item.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(LecturerCourseworkSubmission.this, android.R.layout.simple_list_item_1, fileList);

                listView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = fileList.get(position);
                StorageReference fileRef = cwreff.child(fileName);
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
                        Toast.makeText(LecturerCourseworkSubmission.this, "File downloaded", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LecturerCourseworkSubmission.this, "File download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}