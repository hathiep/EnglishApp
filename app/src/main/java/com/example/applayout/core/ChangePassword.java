package com.example.applayout.core;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.main_class.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {
    TextInputEditText edt_old_password, edt_new_password, edt_new_password_again;
    Button btn_change_password;
    ImageView imV_back, imV_eye1, imV_eye2, imV_eye3;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    Integer eye1, eye2, eye3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        eye1 = eye2 = eye3 = 0;
        setUiEye(imV_eye1, edt_old_password, 1);
        setUiEye(imV_eye2, edt_new_password, 2);
        setUiEye(imV_eye3, edt_new_password_again, 3);
        setOnClickListener();
    }
    private void initUi(){
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_new_password_again = findViewById(R.id.edt_new_password_again);
        btn_change_password = findViewById(R.id.btn_change_password);
        imV_back = findViewById(R.id.imV_back);
        imV_eye1 = findViewById(R.id.imV_eye1);
        imV_eye2 = findViewById(R.id.imV_eye2);
        imV_eye3 = findViewById(R.id.imV_eye3);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
    private void setUiEye(ImageView imv_eye, EditText edt, int x){
        imv_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int eye = 0;
                if(x==1) eye = eye1;
                if(x==2) eye = eye2;
                if(x==3) eye = eye3;
                if(eye == 0){
                    // Chuyển icon unhide thành hide
                    imv_eye.setImageResource(R.drawable.icon_hide);
                    // Chuyển text từ hide thành unhide
                    edt.setInputType(InputType.TYPE_CLASS_TEXT);
                    // Đặt con trỏ nháy ở cuối input đã nhập
                    edt.setSelection(edt.getText().length());
                    // Đảo lại trạng thái mắt
                    if(x==1) eye1 = 1;
                    else if(x==2) eye2 = 1;
                    else eye3 = 1;
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
                    else if(x==2) eye2 = 0;
                    else eye3 = 0;
                }
            }
        });
    }
    private void setOnClickListener(){
        imV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
                finish();
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi đối tượng validate
                Validate validate = new Validate(ChangePassword.this);
                if(!validate.validateChangePassword(getInput(edt_old_password),
                        getInput(edt_new_password), getInput(edt_new_password_again))) return;
                // Check đổi mật khẩu
                reAuthenticateUser();
            }


        });
    }
    private String getInput(EditText edt){
        return edt.getText().toString().trim();
    }

    private void onClickChangePassword(){
        user.updatePassword(getInput(edt_new_password))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            show_dialog("Đổi mật khẩu thành công!", 2);
                            //Back to login
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    private void reAuthenticateUser(){
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), getInput(edt_old_password));
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(getInput(edt_old_password).equals(getInput(edt_new_password))){
                                show_dialog("Vui lòng nhập mật khẩu mới khác mật khẩu cũ!", 2);
                                return;
                            }
                            onClickChangePassword();
                        }
                        else{
                            show_dialog("Mật khẩu cũ không đúng. Vui lòng nhập lại!", 2);
                        }
                    }
                });
    }
    // Hàm thông báo dialog
    private void show_dialog(String s, int time){
        ProgressDialog progressDialog = new ProgressDialog(ChangePassword.this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage(s);
        progressDialog.show();

        // Sử dụng Handler để gửi một tin nhắn hoạt động sau một khoảng thời gian
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn Dialog sau khi đã qua một khoảng thời gian nhất định
                progressDialog.dismiss();
            }
        }, time * 1000); // Số milliseconds bạn muốn Dialog biến mất sau đó
    }
}