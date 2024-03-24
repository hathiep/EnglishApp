package com.example.applayout.core;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextPasswordAgain;
    Button btnRegister;
    ImageView imV_back, imV_eye1, imV_eye2;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    Integer eye1, eye2;


    //    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPasswordAgain = findViewById(R.id.password_again);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);
        imV_back = findViewById(R.id.imV_back);
        imV_eye1 = findViewById(R.id.imV_eye1);
        imV_eye2 = findViewById(R.id.imV_eye2);

        imV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        eye1 = 0;
        eye2 = 0;
        imV_eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eye1 == 0){
                    // Chuyển icon unhide thành hide
                    imV_eye1.setImageResource(R.drawable.icon_hide);
                    // Chuyển text từ hide thành unhide
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    editTextPassword.setSelection(editTextPassword.getText().length());
                    eye1 = 1;
                }
                else {
                    // Chuyển icon hide thành unhide
                    imV_eye1.setImageResource(R.drawable.icon_unhide);
                    // Chuyển text từ unhide thành hide
                    int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    editTextPassword.setInputType(inputType);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    editTextPassword.setSelection(editTextPassword.getText().length());
                    eye1 = 0;
                }
            }
        });

        imV_eye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eye2 == 0){
                    // Chuyển icon unhide thành hide
                    imV_eye2.setImageResource(R.drawable.icon_hide);
                    // Chuyển text từ hide thành unhide
                    editTextPasswordAgain.setInputType(InputType.TYPE_CLASS_TEXT);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    editTextPasswordAgain.setSelection(editTextPasswordAgain.getText().length());
                    eye2 = 1;
                }
                else {
                    // Chuyển icon hide thành unhide
                    imV_eye2.setImageResource(R.drawable.icon_hide);
                    // Chuyển text từ unhide thành hide
                    int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    editTextPasswordAgain.setInputType(inputType);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    editTextPasswordAgain.setSelection(editTextPasswordAgain.getText().length());
                    eye2 = 0;
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(Register.this, "Tạo tài khoản thành công!",
                                            Toast.LENGTH_SHORT).show();

                                    //Back to login
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Register.this, "Tạo tài khoản không thành công!",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }


        });
    }
}