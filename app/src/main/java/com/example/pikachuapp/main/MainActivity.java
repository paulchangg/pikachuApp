package com.example.pikachuapp.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pikachuapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_main);

    }
}
