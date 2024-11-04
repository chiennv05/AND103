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

public class Email extends AppCompatActivity {
    EditText edteamil, edtmatkhau;
    Button btnLogin, btnsignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        edteamil = findViewById(R.id.edtemail);
        edtmatkhau = findViewById(R.id.edtmatkhau);
        btnsignup = findViewById(R.id.btnsignup);
        btnLogin = findViewById(R.id.btnlogin);
        Button btnquenmk = findViewById(R.id.btnquenmk);

        btnquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edteamil.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    edteamil.setError("Vui lòng nhập email để gửi yêu cầu đặt lại mật khẩu");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Email.this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Email.this, "Đã xảy ra lỗi. Vui lòng kiểm tra lại email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Email.this, DangKyTK.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edteamil.getText().toString();
                String matkhau = edtmatkhau.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    edteamil.setError("Vui lòng nhập email");
                    return;
                }
                if (TextUtils.isEmpty(matkhau)) {
                    edtmatkhau.setError("Vui lòng nhập mật khẩu");

                }
                mAuth.signInWithEmailAndPassword(email, matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Email.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Email.this, Home.class));
                            finish();
                        }else {
                            Toast.makeText(Email.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }
}