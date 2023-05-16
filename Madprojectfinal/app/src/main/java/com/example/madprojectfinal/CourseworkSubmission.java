package com.example.madprojectfinal;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CourseworkSubmission extends AppCompatActivity {

    TextView filename;

    Button selectfile,submit;

    String originalFileName;
    DatabaseReference dbreff;
    StorageReference cwreff;

    int upload_status = 0;

    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursework_submission);
        selectfile = findViewById(R.id.selectfile);
        submit = findViewById(R.id.submit);

        cwreff = FirebaseStorage.getInstance().getReference("Module/"+MainActivity.module+"/"+MainActivity.coursework_name+"/");
        dbreff = FirebaseDatabase.getInstance().getReference();

        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upload_status == 1){
                    upload(file);
                }
            }
        });
    }
    public void select(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    @SuppressLint("Range")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData()!= null){
            file = data.getData();
            upload_status = 1;
            if (file != null) {
                Cursor cursor = getContentResolver().query(file, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        originalFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        filename.setText(originalFileName);
                    }
                } finally {
                    cursor.close();
                }
            }

        }
    }

    @SuppressLint("Range")
    public void upload(Uri data){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();


        StorageReference reff = cwreff.child(originalFileName);
        reff.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CourseworkSubmission.this, "File uploaded",Toast.LENGTH_SHORT).show();
                dbreff.child("Module").child(MainActivity.module).child("submission").child(MainActivity.coursework_name).child("student_submmission").child(MainActivity.username).setValue("submitted");

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