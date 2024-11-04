package com.example.lab1_and103;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnEmail = findViewById(R.id.btnGmail);
        Button btnSdt = findViewById(R.id.Sdt);
        btnEmail.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Email.class));
            finish();
        });
        btnSdt.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Sdt.class));
            finish();
        });

    }
}