package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class jujac extends AppCompatActivity {
    ImageView jujac_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujac);
        jujac_arrow = findViewById(R.id.jujac_arrow);
        jujac_arrow.setOnClickListener(v -> finish());
    }
}
