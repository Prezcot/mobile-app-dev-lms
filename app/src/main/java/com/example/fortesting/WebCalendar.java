package com.example.fortesting;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class WebCalendar extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.web_calendar);

            WebView webView = findViewById(R.id.timetableWV);
            webView.loadUrl("https://docs.google.com/spreadsheets/d/1Dy8undAm_zWwf0jb48mQb0UDboGVM9ZYxifLFIHORqs/edit?usp=sharing");
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
    }
    public void onBackPressed() {
        // Add your code here to handle going back to a different activity
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }
}

