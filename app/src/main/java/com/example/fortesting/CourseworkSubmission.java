package com.example.fortesting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
import java.util.Calendar;

public class CourseworkSubmission extends AppCompatActivity {

    TextView filename,textView,description,deadline,guide;

    Button selectfile,submit;

    String originalFileName,day,month,year;
    DatabaseReference dbreff;
    StorageReference cwreff,guidereff;

    int upload_status = 0;


    Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_course_work_view);


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


        selectfile = findViewById(R.id.selectguide);
        submit = findViewById(R.id.createcoursework);

        cwreff = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/Coursework/");
        guidereff = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/Coursework/"+MainActivity.coursework_name+"/guidelines/");
        dbreff = FirebaseDatabase.getInstance().getReference();

        textView = findViewById(R.id.textView3);
        description = findViewById(R.id.description);
        filename = findViewById(R.id.studentUploadFileName);
        deadline = findViewById(R.id.deadline);
        guide = findViewById(R.id.guide);

        textView.setText(MainActivity.coursework_name);
        update_page();

        ArrayList<String> fileList = new ArrayList<>();

        View includedLayout = findViewById(R.id.nav_bar);
        ImageButton back = includedLayout.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.account_type.equals("admin")){
                    startActivity(new Intent(CourseworkSubmission.this, MathLecturer.class));
                    finish();
                }else{
                    startActivity(new Intent(CourseworkSubmission.this, MathStudent.class));
                    finish();
                }

            }
        });
        guidereff.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {
                    fileList.add(item.getName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String file_name = fileList.get(0);
                StorageReference fileRef = guidereff.child(file_name);
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
                        Toast.makeText(CourseworkSubmission.this, "File downloaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CourseworkSubmission.this, "File download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check_filename = filename.toString();

                if(check_filename.equals("No file")){
                    Toast.makeText(CourseworkSubmission.this, "Select file",Toast.LENGTH_SHORT).show();
                }
                else{
                    upload(file);

                }
            }
        });

    }
    public void select(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    @SuppressLint("Range")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData()!= null){
            file = data.getData();
            if (file != null) {
                Cursor cursor = getContentResolver().query(file, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        originalFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            }
            filename.setText(originalFileName);

        }
    }
    @SuppressLint("Range")
    public void upload(Uri data){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();



        StorageReference reff = cwreff.child(MainActivity.coursework_name+"/submissions/"+ originalFileName);
        reff.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Calendar calendar = Calendar.getInstance();
                String submission_type ="Not submitted";
                int year_ = calendar.get(Calendar.YEAR);
                int month_ = calendar.get(Calendar.MONTH) + 1;
                int day_ = calendar.get(Calendar.DAY_OF_MONTH);

                int check_day = Integer.parseInt(day);
                int check_month = Integer.parseInt(month);
                int check_year = Integer.parseInt(year);

                if(year_<check_year){
                    submission_type = "submitted";
                } else if (year_>check_year) {
                    submission_type = "late submission";
                } else {
                    if(month_<check_month){
                        submission_type = "submitted";
                    } else if (month_<check_month) {
                        submission_type = "late submission";
                    } else{
                        if(day_ <= check_day){
                            submission_type = "submitted";
                        }else {
                            submission_type = "late submission";
                        }
                    }
                }
                dbreff.child("Module").child(MainActivity.module).child("Coursework").child(MainActivity.coursework_name).child("submissionsmade").child(MainActivity.username).child("submissionstatus").setValue(submission_type);
                dbreff.child("Module").child(MainActivity.module).child("Coursework").child(MainActivity.coursework_name).child("submissionsmade").child(MainActivity.username).child("filename").setValue(originalFileName);
                Toast.makeText(CourseworkSubmission.this, "File uploaded",Toast.LENGTH_SHORT).show();
                update_page();
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
    public  void update_page(){
        dbreff.child("Module").child(MainActivity.module).child("Coursework").child(MainActivity.coursework_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                description.setText(snapshot.child("Description").getValue(String.class));
                day = snapshot.child("Deadline").child("day").getValue(String.class);
                month = snapshot.child("Deadline").child("month").getValue(String.class);
                year = snapshot.child("Deadline").child("year").getValue(String.class);
                String n = day+"/"+month+"/"+year;
                deadline.setText(n);
                try{
                    String check_status = snapshot.child("submissionsmade").child(MainActivity.username).child("submissionstatus").getValue(String.class);
                    String check_filename = snapshot.child("submissionsmade").child(MainActivity.username).child("filename").getValue(String.class);
                    if(check_filename.equals(check_status)){
                        filename.setText("No file");
                    }
                    else{
                        deadline.setText(check_status);
                        filename.setText(check_filename);
                    }



                }catch (Exception e){
                    Toast.makeText(CourseworkSubmission.this, "Please submit coursework",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}