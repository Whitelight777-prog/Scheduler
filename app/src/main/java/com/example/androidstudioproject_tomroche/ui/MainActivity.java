package com.example.androidstudioproject_tomroche.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidstudioproject_tomroche.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.login_btn);
        button.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, TermsActivity.class);
            startActivity(intent);
        });
    }
}

