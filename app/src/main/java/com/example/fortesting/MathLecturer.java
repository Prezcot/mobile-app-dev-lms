package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MathLecturer extends AppCompatActivity {

    StorageReference folderRef,mfolderref;
    DatabaseReference dbreff;

    public String show_list="none";

    Button lectures, coursework,miscellaneous,add;


    String originalFileName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_module_view);




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
        add = findViewById(R.id.add);


        ArrayList<String> fileList = new ArrayList<>();

        View includedLayout = findViewById(R.id.nav_bar);
        ImageButton back = includedLayout.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MathLecturer.this, Dashboard.class));
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_list.equals("lectures")||show_list.equals("Miscellaneous")){
                    selectpdf();
                }
                if(show_list.equals("coursework")){
                    startActivity(new Intent(MathLecturer.this,CreateCoursework.class));
                    finish();
                }

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


                String message = "Hello, world!";
                Log.d("Tag", message);
                fileList.clear();
                show_list = "lectures";
                folderRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            fileList.add(item.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MathLecturer.this, android.R.layout.simple_list_item_1, fileList);

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MathLecturer.this, android.R.layout.simple_list_item_1, fileList);

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

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MathLecturer.this, android.R.layout.simple_list_item_1, fileList);

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
                            Toast.makeText(MathLecturer.this, "File downloaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MathLecturer.this, "File download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (show_list.equals("coursework")){
                    MainActivity.coursework_name = fileName;
                    startActivity(new Intent(MathLecturer.this, LecturerCourseworkView.class));
                    finish();
                } else if (show_list.equals("miscellaneous")) {
                    StorageReference fileRef = mfolderref.child(fileName);



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
                            Toast.makeText(MathLecturer.this, "File downloaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MathLecturer.this, "File download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    public void selectpdf(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData()!= null){
            uploadPDF(data.getData());
        }
    }

    @SuppressLint("Range")
    public void uploadPDF(Uri data){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        if (data != null) {
            Cursor cursor = getContentResolver().query(data, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    originalFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        StorageReference reff;
        if(show_list.equals("lectures")){
            reff = folderRef.child(originalFileName);
        }else{
            reff = mfolderref.child(originalFileName);
        }

        reff.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MathLecturer.this, "File uploaded",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("uploading:"+(int)progress+"%");
                if (progress == 100){
                    progressDialog.dismiss();
                }

            }
        });
    }


}