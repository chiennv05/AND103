package com.example.lab1_and103;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DangKyTK extends AppCompatActivity {
    EditText edtemail, edtmatkkhau;
    Button btnsignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky_tk);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        edtemail = findViewById(R.id.edtemail_dky);
        edtmatkkhau = findViewById(R.id.edtmatkhau_dky);
        btnsignup = findViewById(R.id.btnsignup_dky);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String email = edtemail.getText().toString();
            String matkhau = edtmatkkhau.getText().toString();
            if(TextUtils.isEmpty(email)) {
                edtemail.setError("Vui lòng nhập email");
                return;
            }
            if(TextUtils.isEmpty(matkhau)) {
                edtmatkkhau.setError("Vui lòng nhập mật khẩu");
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user =  mAuth.getCurrentUser();
                    Toast.makeText(DangKyTK.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DangKyTK.this, Email.class));
                    finish();
                }else {
                    Toast.makeText(DangKyTK.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
                }
            });
            }
        });
    }
}