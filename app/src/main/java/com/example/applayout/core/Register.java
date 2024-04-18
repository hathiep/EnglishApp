package com.example.applayout.core;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.applayout.core.exam.synthetic.Question;
import com.example.applayout.core.main.Exam;
import com.example.applayout.core.main.Exercise;
import com.example.applayout.core.main.Unit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    TextInputEditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextPasswordAgain;
    Button btnRegister;
    ImageView imV_back, imV_eye1, imV_eye2;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    Integer eye1, eye2;
    FirebaseDatabase database;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10}$");


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
        initUi();
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
        setUiEye(imV_eye1, editTextPassword, 1);
        setUiEye(imV_eye2, editTextPasswordAgain, 2);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy các giá trị từ input
                String name, email, phone, password, passwordagain;
                name = editTextName.getText().toString().trim();
                email = editTextEmail.getText().toString().trim();
                phone = editTextPhone.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                passwordagain = editTextPasswordAgain.getText().toString().trim();
                // Gọi đối tượng validate
                Validate validate = new Validate(Register.this);
                if(!validate.validateRegister(name, email, phone, password, passwordagain)) return;
                // Chạy vòng loading
                progressBar.setVisibility(View.VISIBLE);
                // Check đăng ký
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    insertUserToRealtimeDatabase();
                                    Toast.makeText(Register.this, "Tạo tài khoản thành công!",
                                            Toast.LENGTH_SHORT).show();
                                    //Back to login
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Register.this, "Email đã được sử dụng.Vui lòng thử nhập email khác!",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }


        });
    }
    private void initUi(){
        editTextEmail = findViewById(R.id.email);
        editTextName = findViewById(R.id.fullname);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        editTextPasswordAgain = findViewById(R.id.password_again);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);
        imV_back = findViewById(R.id.imV_back);
        imV_eye1 = findViewById(R.id.imV_eye1);
        imV_eye2 = findViewById(R.id.imV_eye2);
    }
    private void setUiEye(ImageView imv_eye, EditText edt, int x){
        imv_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int eye = 0;
                if(x==1) eye = eye1;
                if(x==2) eye = eye2;
                if(eye == 0){
                    // Chuyển icon unhide thành hide
                    imv_eye.setImageResource(R.drawable.icon_hide);
                    // Chuyển text từ hide thành unhide
                    edt.setInputType(InputType.TYPE_CLASS_TEXT);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    edt.setSelection(edt.getText().length());
                    // Đảo lại trạng thái mắt
                    if(x==1) eye1 = 1;
                    else eye2 = 1;
                }
                else {
                    // Chuyển icon hide thành unhide
                    imv_eye.setImageResource(R.drawable.icon_unhide);
                    // Chuyển text từ unhide thành hide
                    int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    edt.setInputType(inputType);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    edt.setSelection(edt.getText().length());
                    // Đảo lại trạng thái mắt
                    if(x==1) eye1 = 0;
                    else eye2 = 0;
                }
            }
        });
    }

    //Hàm khởi tạo thông tin User trên RealTimeDatabase
    private void insertUserToRealtimeDatabase(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Exam exam = new Exam(0,0,0,0,0);
                Unit unit = new Unit(0);
                Exercise exercise = new Exercise(unit, unit);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    return;
                }
                String uid = user.getUid();
                ref.child(uid).child("exam").setValue(exam);
                ref.child(uid).child("exercise").setValue(exercise);
                ref.child(uid).child("newword").child("0").setValue("0");
                ref.child(uid).child("name").setValue(editTextName.getText().toString().trim());
                ref.child(uid).child("phone").setValue(editTextPhone.getText().toString().trim());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}