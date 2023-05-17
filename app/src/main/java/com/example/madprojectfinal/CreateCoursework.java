package com.example.madprojectfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class CreateCoursework extends AppCompatActivity {

    EditText title,description;
    Button selectguide,submit;
    TextView filename,date;
    int day,month_,year_;
    int status = 0;

    DatabaseReference dbreff;
    StorageReference cwreff;

    String originalFileName;
    String selectedDate;
    Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coursework);
        title = findViewById(R.id.entertitle);
        description = findViewById(R.id.enterdescripton);
        date = findViewById(R.id.enterdate);
        selectguide = findViewById(R.id.selectguide);
        submit = findViewById(R.id.createcoursework);
        filename = findViewById(R.id.file);

        dbreff = FirebaseDatabase.getInstance().getReference();
        cwreff = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/Coursework/");

        selectguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check_title = title.getText().toString();
                String check_description = description.getText().toString();
                String check_date = date.getText().toString();
                String check_filename = filename.toString();

                if(check_title.isEmpty() || check_description.isEmpty() || check_date.isEmpty()||check_filename.equals("No file")){
                    Toast.makeText(CreateCoursework.this, "Enter all fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    upload(file);

                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateCoursework.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        date.setText(selectedDate);
                        day = dayOfMonth;
                        month_ = month+1;
                        year_ = year;
                        Toast.makeText(CreateCoursework.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
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



        StorageReference reff = cwreff.child(title.getText().toString()+"/guidelines/"+ originalFileName);
        reff.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dbreff.child("Module").child(MainActivity.module).child("Coursework").child(title.getText().toString()).child("Description").setValue(description.getText().toString());
                dbreff.child("Module").child(MainActivity.module).child("Coursework").child(title.getText().toString()).child("Deadline").child("day").setValue(String.valueOf(day));
                dbreff.child("Module").child(MainActivity.module).child("Coursework").child(title.getText().toString()).child("Deadline").child("month").setValue(String.valueOf(month_));
                dbreff.child("Module").child(MainActivity.module).child("Coursework").child(title.getText().toString()).child("Deadline").child("year").setValue(String.valueOf(year_));
                Toast.makeText(CreateCoursework.this, "File uploaded",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateCoursework.this,StudentDashboard.class));
                finish();
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