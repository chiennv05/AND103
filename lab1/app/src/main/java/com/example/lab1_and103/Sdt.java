package com.example.lab1_and103;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Sdt extends AppCompatActivity {
    private EditText edtPhone, edtOtp; // Các trường nhập liệu cho số điện thoại và mã OTP
    private Button btnGetOtp, btnLogin; // Các nút để lấy OTP và xác nhận
    private FirebaseAuth mAuth; // Đối tượng FirebaseAuth
    private String mVerificationId; // ID xác minh

    // Định nghĩa biến callbacks
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdt); // Đảm bảo layout XML đã được định nghĩa

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo FirebaseAuth

        edtPhone = findViewById(R.id.edtphone);
        edtOtp = findViewById(R.id.edtotp);
        btnGetOtp = findViewById(R.id.btngetotb);
        btnLogin = findViewById(R.id.btnlogin_sdt);

        setupCallbacks(); // Thiết lập các callback

        btnGetOtp.setOnClickListener(v -> {
            String phoneNumber = edtPhone.getText().toString().trim();
            if (isPhoneNumberValid(phoneNumber)) {
                btnGetOtp.setEnabled(false); // Tắt nút
                sendVerificationCode(phoneNumber); // Gửi mã xác minh

                // Đặt lại nút sau 30 giây
                new android.os.Handler().postDelayed(() -> {
                    btnGetOtp.setEnabled(true); // Bật lại nút
                }, 30000); // 30 giây
            } else {
                Toast.makeText(Sdt.this, "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });


        // Xử lý sự kiện khi nhấn nút xác nhận
        btnLogin.setOnClickListener(v -> {
            String otp = edtOtp.getText().toString().trim();
            if (!otp.isEmpty() && mVerificationId != null) {
                verifyCode(otp); // Xác minh mã OTP
            } else {
                Toast.makeText(Sdt.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // Xác minh thành công
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Xác minh thất bại
                String errorMessage = "Xác minh thất bại.";
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorMessage = "Số điện thoại không hợp lệ.";
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    errorMessage = "Quá nhiều yêu cầu. Vui lòng thử lại sau.";
                }
                Toast.makeText(Sdt.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Mã xác minh đã được gửi
                mVerificationId = verificationId;
                Toast.makeText(Sdt.this, "Mã xác minh đã được gửi!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84"+ phoneNumber)       // Số điện thoại cần xác minh
                .setTimeout(60L, TimeUnit.SECONDS) // Thời gian chờ
                .setActivity(this)                 // Hoạt động cho callback
                .setCallbacks(mCallbacks)          // Gọi lại cho xác minh
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options); // Gọi phương thức xác minh
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code); // Tạo chứng chỉ từ mã OTP
        signInWithPhoneAuthCredential(credential); // Đăng nhập với chứng chỉ
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công
                        Toast.makeText(Sdt.this, "Xác minh thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Đăng nhập thất bại
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(Sdt.this, "Mã không hợp lệ.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // Kiểm tra định dạng số điện thoại (điều chỉnh theo nhu cầu)
        return phoneNumber.matches("\\+?[0-9]{10,15}");
    }
}
